/**
 * TMQL4J - Javabased TMQL Engine
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.delete.components.interpreter;

import java.util.List;
import java.util.Map;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.delete.grammar.productions.DeleteClause;
import de.topicmapslab.tmql4j.delete.util.DeletionHandler;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.HashUtil;

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
public class DeleteClauseInterpreter extends ExpressionInterpreterImpl<DeleteClause> {

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
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		QueryMatches results = new QueryMatches(runtime);
		/*
		 * check if stack contains information from delete-expression ( results
		 * of where-clause )
		 */
		if (context.getContextBindings() != null) {
			/*
			 * iterate over all tuples
			 */
			for (Map<String, Object> tuple : context.getContextBindings()) {
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

					Context newContext = new Context(context);
					newContext.setContextBindings(null);
					newContext.setCurrentTuple(tuple);
					newContext.setCurrentIndex(index);
					newContext.setCurrentNode(match);
					/*
					 * interpret sub-expression
					 */
					QueryMatches matches = interpreter.interpret(runtime, newContext, optionalArguments);
					List<Object> possibleValuesForVariable = matches.getPossibleValuesForVariable();
					if (possibleValuesForVariable.isEmpty()) {
						/*
						 * is multi-line-result
						 */
						if (!matches.isEmpty()) {
							/*
							 * iterate over results
							 */
							for (String var : matches.getOrderedKeys()) {
								if (var.matches("\\$[0-9]+")) {
									possibleValuesForVariable = matches.getPossibleValuesForVariable(var);
									/*
									 * call deletion-handler to remove content
									 * and store the number of removed items
									 */
									long amount = new DeletionHandler(runtime, context).delete(possibleValuesForVariable, cascade);
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
						long amount = new DeletionHandler(runtime, context).delete(possibleValuesForVariable, cascade);
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

				QueryMatches matches = interpreter.interpret(runtime, context, optionalArguments);
				List<Object> possibleValuesForVariable = matches.getPossibleValuesForVariable();
				if (possibleValuesForVariable.isEmpty()) {
					/*
					 * is multi-line-result
					 */
					if (!matches.isEmpty()) {
						/*
						 * iterate over all values
						 */
						for (String var : matches.getOrderedKeys()) {
							if (var.matches("\\$[0-9]+")) {
								possibleValuesForVariable = matches.getPossibleValuesForVariable(var);
								/*
								 * call deletion-handler to remove content and
								 * store the number of removed items
								 */
								long amount = new DeletionHandler(runtime, context).delete(possibleValuesForVariable, cascade);
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
					long amount = new DeletionHandler(runtime, context).delete(possibleValuesForVariable, cascade);
					resultTuple.put("$" + index, amount);
					index++;
				}
			}
			if (resultTuple.size() >= getInterpreters(runtime).size()) {
				results.add(resultTuple);
			}
			resultTuple = HashUtil.getHashMap();
		}
		return results;
	}
}
