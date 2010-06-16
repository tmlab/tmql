/**
 * TMQL4J - Javabased TMQL Engine
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.extension.tmml.interpreter;

import java.util.Map;
import java.util.logging.Logger;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.model.tuplesequence.ITupleSequence;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.DeleteClause;
import de.topicmapslab.tmql4j.extension.tmml.util.DeletionHandler;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;

/**
 * Special interpreter class to interpret delete-clauses.
 * <p>
 * The grammar production rule of the expression is: <code>
 *  delete-clause ::= DELETE &lt;value-expression&gt;
 * </code>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class DeleteClauseInterpreter extends
		ExpressionInterpreterImpl<DeleteClause> {

	/**
	 * internal flag of keyword CASCADE is contained
	 */
	private final boolean cascade;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public DeleteClauseInterpreter(DeleteClause ex) {
		super(ex);
		if (ex instanceof DeleteClause) {
			this.cascade = ((DeleteClause) ex).isCascade();
		} else {
			this.cascade = false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		Logger.getLogger(getClass().getSimpleName()).info("Start.");
		QueryMatches results = new QueryMatches(runtime);
		/*
		 * check if stack contains information from delete-expression ( results
		 * of where-clause )
		 */
		if (runtime.getRuntimeContext().peek().contains(
				VariableNames.ITERATED_BINDINGS)) {
			QueryMatches matches = (QueryMatches) runtime.getRuntimeContext()
					.peek().getValue(VariableNames.ITERATED_BINDINGS);
			/*
			 * iterate over all tuples
			 */
			for (Map<String, Object> tuple : matches) {
				Map<String, Object> resultTuple = HashUtil.getHashMap();
				int index = 0;
				/*
				 * iterate over all value-expressions
				 */
				for (IExpressionInterpreter<?> interpreter : getInterpreters(runtime)) {
					String variable = QueryMatches.getNonScopedVariable();
					if (!interpreter.getVariables().isEmpty()) {
						variable = interpreter.getVariables().get(0);
					}
					Object match = tuple.get(variable);

					/*
					 * clean variable stack
					 */
					runtime.getRuntimeContext().push();
					runtime.getRuntimeContext().peek().remove(
							VariableNames.ITERATED_BINDINGS);
					runtime.getRuntimeContext().peek().setValue(
							VariableNames.CURRENT_TUPLE, match);
					runtime.getRuntimeContext().peek()
							.setValue(variable, match);

					/*
					 * interpret sub-expression
					 */
					interpreter.interpret(runtime);

					Object obj = runtime.getRuntimeContext().pop().getValue(
							VariableNames.QUERYMATCHES);
					if (!(obj instanceof QueryMatches)) {
						throw new TMQLRuntimeException(
								"Invalid interpretation of expression value-expression. Has to return an instance of QueryMatches.");
					}
					ITupleSequence<Object> sequence = ((QueryMatches) obj)
							.getPossibleValuesForVariable();
					if (sequence.isEmpty()) {
						/*
						 * is multi-line-result
						 */
						QueryMatches m = (QueryMatches) obj;
						if (!m.isEmpty()) {
							/*
							 * iterate over results
							 */
							for (String var : m.getOrderedKeys()) {
								if (var.matches("\\$[0-9]+")) {
									sequence = ((QueryMatches) obj)
											.getPossibleValuesForVariable(var);
									/*
									 * call deletion-handler to remove content
									 * and store the number of removed items
									 */
									long amount = new DeletionHandler(runtime)
											.delete(sequence, cascade);
									resultTuple.put("$" + index, amount);
									index++;
								}
							}
						}
					} else {
						/*
						 * call deletion-handler to remove content and store the
						 * number of removed items
						 */
						long amount = new DeletionHandler(runtime).delete(
								sequence, cascade);
						resultTuple.put("$" + index, amount);
						index++;
					}
				}
				if (resultTuple.size() >= getInterpreters(runtime).size()) {
					results.add(resultTuple);
				}
				resultTuple = HashUtil.getHashMap();
			}
		}
		/*
		 * no context given by delete-expression
		 */
		else {
			Map<String, Object> resultTuple = HashUtil.getHashMap();
			int index = 0;
			/*
			 * iterate over all value-expression
			 */
			for (IExpressionInterpreter<?> interpreter : getInterpreters(runtime)) {
				runtime.getRuntimeContext().push();
				interpreter.interpret(runtime);
				Object obj = runtime.getRuntimeContext().pop().getValue(
						VariableNames.QUERYMATCHES);
				if (!(obj instanceof QueryMatches)) {
					throw new TMQLRuntimeException(
							"Invalid interpretation of expression value-expression. Has to return an instance of QueryMatches.");
				}
				ITupleSequence<Object> sequence = ((QueryMatches) obj)
						.getPossibleValuesForVariable();
				if (sequence.isEmpty()) {
					/*
					 * is multi-line-result
					 */
					QueryMatches m = (QueryMatches) obj;
					if (!m.isEmpty()) {
						/*
						 * iterate over all values
						 */
						for (String var : m.getOrderedKeys()) {
							if (var.matches("\\$[0-9]+")) {
								sequence = ((QueryMatches) obj)
										.getPossibleValuesForVariable(var);
								/*
								 * call deletion-handler to remove content and
								 * store the number of removed items
								 */
								long amount = new DeletionHandler(runtime)
										.delete(sequence, cascade);
								resultTuple.put("$" + index, amount);
								index++;
							}
						}
					}
				} else {
					/*
					 * call deletion-handler to remove content and store the
					 * number of removed items
					 */
					long amount = new DeletionHandler(runtime).delete(sequence,
							cascade);
					resultTuple.put("$" + index, amount);
					index++;
				}
			}
			if (resultTuple.size() >= getInterpreters(runtime).size()) {
				results.add(resultTuple);
			}
			resultTuple = HashUtil.getHashMap();
		}

		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);

		Logger.getLogger(getClass().getSimpleName()).info(
				"Finished. Results: " + results);

	}
}
