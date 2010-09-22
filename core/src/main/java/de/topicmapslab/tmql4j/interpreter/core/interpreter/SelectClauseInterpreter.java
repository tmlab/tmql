/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.interpreter.core.interpreter;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.model.tuplesequence.ITupleSequence;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.parser.core.expressions.SelectClause;
import de.topicmapslab.tmql4j.parser.core.expressions.ValueExpression;

/**
 * 
 * Special interpreter class to interpret select-clauses.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * select-clause ::= SELECT    < value-expression >
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SelectClauseInterpreter extends
		ExpressionInterpreterImpl<SelectClause> {

	/**
	 * the Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public SelectClauseInterpreter(SelectClause ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		logger.info("Start.");

		/*
		 * variable mapping cache
		 */
		final Map<String, String> mapping = HashUtil.getHashMap();

		QueryMatches results = new QueryMatches(runtime);
		QueryMatches queryMatchesForCount = new QueryMatches(runtime);
		/*
		 * check if context is given by upper expression ( where-clause )
		 */
		if (runtime.getRuntimeContext().peek().contains(
				VariableNames.ITERATED_BINDINGS)) {
			QueryMatches matches = (QueryMatches) runtime.getRuntimeContext()
					.peek().getValue(VariableNames.ITERATED_BINDINGS);
			/*
			 * temporary store of variable bound to fn:count(...)
			 */
			String countedVariable = null;
			/*
			 * iterate over all tuples
			 */
			for (Map<String, Object> tuple : matches) {
				Map<String, Object> resultTuple = HashUtil.getHashMap();
				Map<String, Object> countableTuple = HashUtil.getHashMap();
				int index = 0;
				/*
				 * iterate over all value-expression
				 */
				List<IExpressionInterpreter<ValueExpression>> interpreters = getInterpretersFilteredByEypressionType(
						runtime, ValueExpression.class);
				for (int i = 0; i < interpreters.size(); i++) {
					IExpressionInterpreter<ValueExpression> ex = interpreters
							.get(i);
					/*
					 * get current variable name
					 */
					String variable = QueryMatches.getNonScopedVariable();
					if (!ex.getVariables().isEmpty()) {
						variable = ex.getVariables().get(0);
					}
					/*
					 * get current value of variable
					 */
					Object match = tuple.get(variable);

					/*
					 * check if variable is function-invocation of function
					 * fn:count
					 */
					if (ex.getTokens().contains("fn:count")) {
						/*
						 * store variable for later reuse
						 */
						countedVariable = ex.getVariables().get(0);
						resultTuple.put("fn:count", index);
					}
					/*
					 * value-expression isn't fn:count
					 */
					else {
						/*
						 * prepare stack for sub-expression call
						 */
						runtime.getRuntimeContext().push();
						runtime.getRuntimeContext().peek().remove(
								VariableNames.ITERATED_BINDINGS);
						runtime.getRuntimeContext().peek().setValue(
								VariableNames.CURRENT_TUPLE, match);
						runtime.getRuntimeContext().peek().setValue(variable,
								match);

						/*
						 * call sub-expression
						 */
						ex.interpret(runtime);

						Object obj = runtime.getRuntimeContext().pop()
								.getValue(VariableNames.QUERYMATCHES);
						if (!(obj instanceof QueryMatches)) {
							throw new TMQLRuntimeException(
									"Invalid interpretation of expression value-expression. Has to return an instance of QueryMatches.");
						}
						ITupleSequence<Object> sequence = ((QueryMatches) obj)
								.getPossibleValuesForVariable();
						/*
						 * check if values are bound to non-scoped variable
						 */
						if (sequence.isEmpty()) {
							/*
							 * is multi-line-result
							 */
							QueryMatches m = (QueryMatches) obj;
							if (!m.isEmpty()) {
								/*
								 * iterate over all variables of result
								 */
								for (String var : m.getOrderedKeys()) {
									/*
									 * check if variable is indexed-variable $#
									 */
									if (var.matches("\\$[0-9]+")) {
										/*
										 * store values
										 */
										resultTuple
												.put(
														"$" + index,
														m
																.getPossibleValuesForVariable(var));
										countableTuple
												.put(
														"$" + index,
														m
																.getPossibleValuesForVariable(var));
										index++;
									}
								}
							}
						}
						/*
						 * values are bound to non-scoped variable
						 */
						else {
							/*
							 * check if mapping contains value variable
							 * transformation
							 */
							if (!mapping.containsKey("$" + index)) {
								mapping.put("$" + index, variable);
							}
							/*
							 * store values
							 */
							countableTuple.put("$" + index, match);
							if (sequence.size() == 1) {
								resultTuple.put("$" + index, sequence.get(0));
							} else {
								resultTuple.put("$" + index, sequence);
							}
							index++;
						}
					}
				}
				/*
				 * check tuple size ( only store tuples containing non-empty
				 * results )
				 */
				if (resultTuple.size() >= getInterpreters(runtime).size()) {
					results.add(resultTuple);
					queryMatchesForCount.add(countableTuple);
				}
				resultTuple = HashUtil.getHashMap();
				countableTuple = HashUtil.getHashMap();
			}
			/*
			 * check if fn:count was contained
			 */
			if (countedVariable != null) {
				results = fnCount(runtime, results, queryMatchesForCount,
						matches, mapping, countedVariable);
			}
		}
		/*
		 * no context given by upper-expression
		 */
		else {
			/*
			 * create temporary tuples
			 */
			Map<String, Object> resultTuple = HashUtil.getHashMap();
			Map<String, Object> countableTuple = HashUtil.getHashMap();
			int index = 0;
			/*
			 * iterate over value-expressions
			 */
			List<IExpressionInterpreter<ValueExpression>> interpreters = getInterpretersFilteredByEypressionType(
					runtime, ValueExpression.class);
			for (int i = 0; i < interpreters.size(); i++) {
				IExpressionInterpreter<ValueExpression> ex = interpreters
						.get(i);
				/*
				 * get variable of current value-expression or set to non-scoped
				 * variable
				 */
				String variable = QueryMatches.getNonScopedVariable();
				if (!ex.getVariables().isEmpty()) {
					variable = ex.getVariables().get(0);
				}

				/*
				 * check if current value-expression is function-invocation of
				 * fn:count
				 */
				if (ex.getTokens().contains("fn:count")) {
					resultTuple.put("fn:count", index);
				}
				/*
				 * value-expression is not fn:count
				 */
				else {
					runtime.getRuntimeContext().push();

					/*
					 * call sub-expression
					 */
					ex.interpret(runtime);

					Object obj = runtime.getRuntimeContext().pop().getValue(
							VariableNames.QUERYMATCHES);
					if (!(obj instanceof QueryMatches)) {
						throw new TMQLRuntimeException(
								"Invalid interpretation of expression value-expression. Has to return an instance of QueryMatches.");
					}
					ITupleSequence<Object> sequence = ((QueryMatches) obj)
							.getPossibleValuesForVariable();
					/*
					 * check if values are bound to non-scoped variable
					 */
					if (sequence.isEmpty()) {
						/*
						 * is multi-line-result
						 */
						QueryMatches m = (QueryMatches) obj;
						if (!m.isEmpty()) {
							/*
							 * iterate over variables
							 */
							for (String var : m.getOrderedKeys()) {
								/*
								 * check if variable is indexed-variable
								 */
								if (var.matches("\\$[0-9]+")) {
									/*
									 * store results
									 */
									resultTuple.put("$" + index, m
											.getPossibleValuesForVariable(var));
									countableTuple.put("$" + index, m
											.getPossibleValuesForVariable(var));
									index++;
								}
							}
						}
					}
					/*
					 * values are bound to non-scoped variable
					 */
					else {
						/*
						 * store mapping if necessary
						 */
						if (!mapping.containsKey("$" + index)) {
							mapping.put("$" + index, variable);
						}
						/*
						 * store results
						 */
						resultTuple.put("$" + index, sequence);
						index++;
					}
				}
			}
			/*
			 * check tuple size ( store only non-empty results )
			 */
			if (resultTuple.size() >= getInterpreters(runtime).size()) {
				results.add(resultTuple);
				queryMatchesForCount.add(countableTuple);
			}
		}

		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);

		logger.info("Finished. Results: " + results);

	}

	/**
	 * Internal method realize the interpretation of the function fn:count of
	 * the current TMQL draft.
	 * <p>
	 * count isa function http://psi.topicmaps.org/tmql/1.0/functions/count
	 * profile: "fn:count (s : tuple-sequence) return integer" description:
	 * "returns the number of tuples in the sequence"
	 * 
	 * </p>
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param results
	 *            the results of interpret the select-clause
	 * @param tuples
	 *            the countable tuple
	 * @param origin
	 *            the overall results before select-interpretation
	 * @param mapping
	 *            optional variable mapping
	 * @param countedVariable
	 *            the variable to count
	 * @return a new query match containing the origin results except form the
	 *         fn:count reference. This reference will be replaced with the
	 *         counted variable.
	 * @throws TMQLRuntimeException
	 *             thrown if function interpretation fails
	 */
	private final QueryMatches fnCount(final TMQLRuntime runtime,
			QueryMatches results, QueryMatches tuples, QueryMatches origin,
			final Map<String, String> mapping, final String countedVariable)
			throws TMQLRuntimeException {
		QueryMatches queryMatches = new QueryMatches(runtime);
		for (int index = 0; index < tuples.size(); index++) {
			Map<String, Object> tuple = tuples.getMatches().get(index);
			Map<String, Object> result = results.getMatches().get(index);

			Map<String, Object> newTuple = HashUtil.getHashMap();
			newTuple.putAll(result);
			Object i = newTuple.get("fn:count");
			newTuple.remove("fn:count");
			newTuple
					.put("$" + i, origin.count(tuple, mapping, countedVariable));
			queryMatches.add(newTuple);
		}
		return queryMatches;
	}
}
