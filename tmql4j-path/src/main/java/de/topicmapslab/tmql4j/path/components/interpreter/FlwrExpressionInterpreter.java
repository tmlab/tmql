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

import java.util.Map;
import java.util.Map.Entry;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.components.processor.core.Context;
import de.topicmapslab.tmql4j.path.grammar.productions.FlwrExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.ForClause;
import de.topicmapslab.tmql4j.path.grammar.productions.OrderByClause;
import de.topicmapslab.tmql4j.path.grammar.productions.ReturnClause;
import de.topicmapslab.tmql4j.path.grammar.productions.WhereClause;

/**
 * 
 * Special interpreter class to interpret flwr-expressions.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * 	flwr-expression	::=	[  FOR   binding-set ] [  WHERE   boolean-expression ] [  ORDER BY  < value-expression > ] RETURN  content
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class FlwrExpressionInterpreter extends ExpressionInterpreterImpl<FlwrExpression> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public FlwrExpressionInterpreter(FlwrExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		Context newContext = new Context(context);
		if (containsExpressionsType(ForClause.class)) {
			QueryMatches results = interpreteForClauses(runtime, context, optionalArguments);
			newContext.setContextBindings(results);
			if (results.isEmpty()) {
				return QueryMatches.emptyMatches();
			}
		}
		/*
		 * check if where clause exists and any variable is contained
		 */
		if (containsExpressionsType(WhereClause.class)) {
			QueryMatches results = interpreteWhereClause(runtime, newContext, optionalArguments);
			newContext.setContextBindings(results);
			if (results.isEmpty()) {
				return QueryMatches.emptyMatches();
			}
		}

		/*
		 * check if order-by clause exists and any variable is contained
		 */
		if (containsExpressionsType(OrderByClause.class)) {
			QueryMatches results = interpreteOrderByClause(runtime, newContext, optionalArguments);
			newContext.setContextBindings(results);
			if (results.isEmpty()) {
				return QueryMatches.emptyMatches();
			}
		}	
		return interpretReturnClause(runtime, newContext, optionalArguments);
	}

	/**
	 * The method is called to interpret the given sub-expression by using the
	 * given runtime. The interpretation will call the sub-expression if the
	 * given expression isn't a leaf in parsing-tree.
	 * 
	 * <p>
	 * The interpretation will transform the value on top of the stack and put
	 * its results also on top.
	 * </p>
	 * 
	 * @param runtime
	 *            the runtime which contains all necessary information for
	 *            querying process
	 * @param context
	 *            the current querying context
	 * @param optionalArguments
	 *            optional arguments
	 * @return the query matches containing the results of the interpretation of
	 *         the sub-expression
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpreteOrderByClause(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * check if order-by clause exists and any variable is contained
		 */
		if (context.getContextBindings() != null) {
			/*
			 * get order-by-clause
			 */
			IExpressionInterpreter<OrderByClause> orderByClause = getInterpretersFilteredByEypressionType(runtime, OrderByClause.class).get(0);

			/*
			 * call order-by-clause
			 */
			return orderByClause.interpret(runtime, context, optionalArguments);
		}
		return QueryMatches.emptyMatches();
	}

	/**
	 * The method is called to interpret the given sub-expression by using the
	 * given runtime. The interpretation will call the sub-expression if the
	 * given expression isn't a leaf in parsing-tree.
	 * 
	 * <p>
	 * The interpretation will transform the value on top of the stack and put
	 * its results also on top.
	 * </p>
	 * 
	 * @param runtime
	 *            the runtime which contains all necessary information for
	 *            querying process
	 * @param context
	 *            the current querying context
	 * @param optionalArguments
	 *            optional arguments
	 * @return the query matches containing the results of the interpretation of
	 *         the sub-expression
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretReturnClause(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * only one select clause has to be contained
		 */
		if (!containsExpressionsType(ReturnClause.class)) {
			throw new TMQLRuntimeException("Invalid structure. not return clause.");
		}

		/*
		 * extract the select clause
		 */
		IExpressionInterpreter<ReturnClause> returnClauseInterpreter = getInterpretersFilteredByEypressionType(runtime, ReturnClause.class).get(0);

		/*
		 * check if return-clause is dependent from variables and results is not
		 * empty
		 */
		for (IExpressionInterpreter<ForClause> interpreter : getInterpretersFilteredByEypressionType(runtime, ForClause.class)) {
			if (context.getContextBindings().getPossibleValuesForVariable(interpreter.getTokens().get(1)).isEmpty()) {
				return QueryMatches.emptyMatches();
			}

		}
		/*
		 * call sub expression
		 */
		return returnClauseInterpreter.interpret(runtime, context, optionalArguments);
	}

	/**
	 * The method is called to interpret the given sub-expression by using the
	 * given runtime. The interpretation will call the sub-expression if the
	 * given expression isn't a leaf in parsing-tree.
	 * 
	 * <p>
	 * The interpretation will transform the value on top of the stack and put
	 * its results also on top.
	 * </p>
	 * 
	 * @param runtime
	 *            the runtime which contains all necessary information for
	 *            querying process
	 * @param context
	 *            the current querying context
	 * @param optionalArguments
	 *            optional arguments
	 * @return the query matches containing the results of the interpretation of
	 *         the sub-expression
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpreteWhereClause(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * get where-clause
		 */
		IExpressionInterpreter<WhereClause> whereClause = getInterpretersFilteredByEypressionType(runtime, WhereClause.class).get(0);
		/*
		 * call where-clause
		 */
		// TODO use for iteration results as iteration bindings
		Context newContext = new Context(context);
//		newContext.setContextBindings(null);
		QueryMatches matches = whereClause.interpret(runtime, newContext, optionalArguments);
		/*
		 * iterate of all bindings if bindings and results are not empty
		 */
		if (!matches.isEmpty()) {
			if (context.getContextBindings() != null) {
				QueryMatches results = new QueryMatches(runtime);
				/*
				 * iterate over tuples
				 */
				for (Map<String, Object> tuple : matches) {
					boolean satisfy = true;
					/*
					 * iterate over values
					 */
					for (Entry<String, Object> entry : tuple.entrySet()) {
						/*
						 * check if possible bindings of the where-clause are
						 * contained by the for-clauses-bindings
						 */
						if (context.getContextBindings().getOrderedKeys().contains(entry.getKey())) {
							satisfy = context.getContextBindings().getPossibleValuesForVariable(entry.getKey()).contains(entry.getValue());
						}
						if (!satisfy) {
							break;
						}
					}
					if (satisfy) {
						results.add(tuple);
					}
				}
				return results;
			}
			return matches;
		}
		return QueryMatches.emptyMatches();
	}

	/**
	 * The method is called to interpret the given sub-expression by using the
	 * given runtime. The interpretation will call the sub-expression if the
	 * given expression isn't a leaf in parsing-tree.
	 * 
	 * <p>
	 * The interpretation will transform the value on top of the stack and put
	 * its results also on top.
	 * </p>
	 * 
	 * @param runtime
	 *            the runtime which contains all necessary information for
	 *            querying process
	 * @param context
	 *            the current querying context
	 * @param optionalArguments
	 *            optional arguments
	 * @return the query matches containing the results of the interpretation of
	 *         the sub-expression
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpreteForClauses(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * Cache to store all possible bindings of contained variables
		 */
		QueryMatches[] bindings = extractArguments(runtime, ForClause.class, context, optionalArguments);
		return new QueryMatches(runtime, bindings);
	}
}
