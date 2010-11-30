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
package de.topicmapslab.tmql4j.path.components.interpreter;

import java.util.List;
import java.util.Map;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.util.HashUtil;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.components.processor.core.Context;
import de.topicmapslab.tmql4j.path.grammar.productions.SelectClause;
import de.topicmapslab.tmql4j.path.grammar.productions.ValueExpression;

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
public class SelectClauseInterpreter extends ExpressionInterpreterImpl<SelectClause> {

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
	// TODO refactor
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * variable mapping cache
		 */
		final Map<String, String> mapping = HashUtil.getHashMap();

		QueryMatches results = new QueryMatches(runtime);
		QueryMatches queryMatchesForCount = new QueryMatches(runtime);
		/*
		 * check if context is given by upper expression ( where-clause )
		 */
		if (context.getContextBindings() != null) {
			/*
			 * temporary store of variable bound to fn:count(...)
			 */
			String countedVariable = null;
			/*
			 * iterate over all tuples
			 */
			for (Map<String, Object> tuple : context.getContextBindings()) {
				Map<String, Object> resultTuple = HashUtil.getHashMap();
				Map<String, Object> countableTuple = HashUtil.getHashMap();
				int index = 0;
				/*
				 * iterate over all value-expression
				 */
				List<IExpressionInterpreter<ValueExpression>> interpreters = getInterpretersFilteredByEypressionType(runtime, ValueExpression.class);
				for (int i = 0; i < interpreters.size(); i++) {
					IExpressionInterpreter<ValueExpression> ex = interpreters.get(i);
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
						Context newContext = new Context(context);
						newContext.setContextBindings(null);
						newContext.setCurrentNode(match);
						newContext.setCurrentTuple(tuple);
						newContext.setCurrentIndex(i);
						index = updateResultTuple(runtime, mapping, resultTuple, countableTuple, index, ex, variable, newContext, optionalArguments);
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
				results = fnCount(runtime, results, queryMatchesForCount, context.getContextBindings(), mapping, countedVariable);
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
			List<IExpressionInterpreter<ValueExpression>> interpreters = getInterpretersFilteredByEypressionType(runtime, ValueExpression.class);
			for (int i = 0; i < interpreters.size(); i++) {
				IExpressionInterpreter<ValueExpression> interpreter = interpreters.get(i);
				/*
				 * get variable of current value-expression or set to non-scoped
				 * variable
				 */
				String variable = QueryMatches.getNonScopedVariable();
				if (!interpreter.getVariables().isEmpty()) {
					variable = interpreter.getVariables().get(0);
				}

				/*
				 * check if current value-expression is function-invocation of
				 * fn:count
				 */
				if (interpreter.getTokens().contains("fn:count")) {
					resultTuple.put("fn:count", index);
				}
				/*
				 * value-expression is not fn:count
				 */
				else {
					index = updateResultTuple(runtime, mapping, resultTuple, countableTuple, index, interpreter, variable, context, optionalArguments);
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
		return results;
	}

	/**
	 * Internal method to update the result set by execute a select-clause entry
	 * 
	 * @param runtime
	 *            the runtime
	 * @param mapping
	 *            the mapping of variable names need to rename after execution
	 * @param resultTuple
	 *            the result tuple to update
	 * @param countableTuple
	 *            the tuple to count
	 * @param index
	 *            the current index
	 * @param interpreter
	 *            the interpreter to call
	 * @param variable
	 *            the variable to check
	 * @param context
	 *            the current context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the new tuple index
	 */
	private int updateResultTuple(ITMQLRuntime runtime, final Map<String, String> mapping, Map<String, Object> resultTuple, Map<String, Object> countableTuple, int index,
			IExpressionInterpreter<ValueExpression> interpreter, String variable, IContext context, Object... optionalArguments) {
		/*
		 * call sub-expression
		 */
		QueryMatches matches = interpreter.interpret(runtime, context, optionalArguments);
		List<Object> possibleValuesForVariable = matches.getPossibleValuesForVariable();
		/*
		 * check if values are bound to non-scoped variable
		 */
		if (possibleValuesForVariable.isEmpty()) {
			/*
			 * is multi-line-result
			 */
			if (!matches.isEmpty()) {
				/*
				 * iterate over variables
				 */
				for (String var : matches.getOrderedKeys()) {
					/*
					 * check if variable is indexed-variable
					 */
					if (var.matches("\\$[0-9]+")) {
						/*
						 * store results
						 */
						resultTuple.put("$" + index, matches.getPossibleValuesForVariable(var));
						countableTuple.put("$" + index, matches.getPossibleValuesForVariable(var));
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
			resultTuple.put("$" + index, possibleValuesForVariable);
			index++;
		}
		return index;
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
	private final QueryMatches fnCount(final ITMQLRuntime runtime, QueryMatches results, QueryMatches tuples, QueryMatches origin, final Map<String, String> mapping, final String countedVariable)
			throws TMQLRuntimeException {
		QueryMatches queryMatches = new QueryMatches(runtime);
		for (int index = 0; index < tuples.size(); index++) {
			Map<String, Object> tuple = tuples.getMatches().get(index);
			Map<String, Object> result = results.getMatches().get(index);

			Map<String, Object> newTuple = HashUtil.getHashMap();
			newTuple.putAll(result);
			Object i = newTuple.get("fn:count");
			newTuple.remove("fn:count");
			newTuple.put("$" + i, origin.count(tuple, mapping, countedVariable));
			queryMatches.add(newTuple);
		}
		return queryMatches;
	}
}
