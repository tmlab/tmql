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

import java.util.Map;
import java.util.Map.Entry;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.parser.core.expressions.FlwrExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.ForClause;
import de.topicmapslab.tmql4j.parser.core.expressions.OrderByClause;
import de.topicmapslab.tmql4j.parser.core.expressions.ReturnClause;
import de.topicmapslab.tmql4j.parser.core.expressions.WhereClause;

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
public class FlwrExpressionInterpreter extends
		ExpressionInterpreterImpl<FlwrExpression> {

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
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		QueryMatches results = interpreteForClauses(runtime);

		/*
		 * check if where clause exists and any variable is contained
		 */
		if (containsExpressionsType(WhereClause.class)) {
			results = interpreteWhereClause(runtime, results);
		}

		/*
		 * check if order-by clause exists and any variable is contained
		 */
		if (containsExpressionsType(OrderByClause.class)) {
			results = interpreteOrderByClause(runtime, results);
		}

		results = interpretReturnClause(runtime, results);

		/*
		 * set overall results on top of the stack
		 */
		runtime.getRuntimeContext().peek().setValue(
				VariableNames.QUERYMATCHES, results);

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
	 * @param tuples
	 *            the satisfying variable bindings of the where-clause
	 * @return the query matches containing the results of the interpretation of
	 *         the sub-expression
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpreteOrderByClause(TMQLRuntime runtime,
			QueryMatches tuples) throws TMQLRuntimeException {
		/*
		 * check if order-by clause exists and any variable is contained
		 */
		if (containsExpressionsType(OrderByClause.class) && !tuples.isEmpty()) {
			/*
			 * get order-by-clause
			 */
			IExpressionInterpreter<OrderByClause> orderByClause = getInterpretersFilteredByEypressionType(
					runtime, OrderByClause.class).get(0);

			/*
			 * set binding to set on top of the stack, which has to order
			 */
			runtime.getRuntimeContext().push().setValue(
					VariableNames.ORDER, tuples);

			/*
			 * call order-by-clause
			 */
			orderByClause.interpret(runtime);

			/*
			 * pop from stack
			 */
			Object obj = runtime.getRuntimeContext().pop().getValue(
					VariableNames.QUERYMATCHES);
			/*
			 * check result type, has to be an instance of Map<?,?>
			 */
			if (obj instanceof QueryMatches) {
				return (QueryMatches) obj;
			} else {
				throw new TMQLRuntimeException(
						"Invalid interpretation of expression orderBby-clause. Has to return an instance of QueryMatches.");
			}

		}
		return tuples;
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
	 * @param matches
	 *            the satisfying and ordered variable bindings of the
	 *            where-clause
	 * @return the query matches containing the results of the interpretation of
	 *         the sub-expression
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretReturnClause(TMQLRuntime runtime,
			QueryMatches matches) throws TMQLRuntimeException {

		/*
		 * only one select clause has to be contained
		 */
		if (!containsExpressionsType(ReturnClause.class)) {
			throw new TMQLRuntimeException(
					"Invalid structure. not return clause.");
		}

		/*
		 * extract the select clause
		 */
		IExpressionInterpreter<ReturnClause> returnClauseInterpreter = getInterpretersFilteredByEypressionType(
				runtime, ReturnClause.class).get(0);

		/*
		 * check if return-clause is dependent from variables and results is not
		 * empty
		 */
		boolean ignore = false;
		for (IExpressionInterpreter<ForClause> interpreter : getInterpretersFilteredByEypressionType(
				runtime, ForClause.class)) {
			if (matches.getPossibleValuesForVariable(
					interpreter.getTokens().get(1)).isEmpty()) {
				ignore = true;
				break;
			}

		}

		/*
		 * check if return-clause can ignored because of missing value for at
		 * least one variable
		 */
		if (!ignore) {
			/*
			 * push to the top of the stack
			 */
			runtime.getRuntimeContext().push();
			if (!matches.isEmpty()) {
				runtime.getRuntimeContext().peek().setValue(
						VariableNames.ITERATED_BINDINGS, matches);
			} else {
				runtime.getRuntimeContext().peek().remove(
						VariableNames.ITERATED_BINDINGS);
			}

			/*
			 * call sub expression
			 */
			returnClauseInterpreter.interpret(runtime);

			/*
			 * pop from top of the stack
			 */
			Object obj = runtime.getRuntimeContext().pop().getValue(
					VariableNames.QUERYMATCHES);
			/*
			 * check result type
			 */
			if (!(obj instanceof QueryMatches)) {
				throw new TMQLRuntimeException(
						"Invalid interpretation of expression return-clause. Has to return an instance of QueryMatches.");
			}

			return (QueryMatches) obj;
		}
		/*
		 * ignore return clause
		 */
		else {
			return new QueryMatches(runtime);
		}
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
	 * @param bindings
	 *            the variable bindings context defined by the for-clauses
	 * @return the query matches containing the results of the interpretation of
	 *         the sub-expression
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpreteWhereClause(TMQLRuntime runtime,
			QueryMatches bindings) throws TMQLRuntimeException {
		/*
		 * get where-clause
		 */
		IExpressionInterpreter<WhereClause> whereClause = getInterpretersFilteredByEypressionType(
				runtime, WhereClause.class).get(0);

		/*
		 * push on top of the stack
		 */
		runtime.getRuntimeContext().push();

		runtime.getRuntimeContext().peek().setValue(
				VariableNames.ITERATED_BINDINGS, bindings);
		/*
		 * call where-clause
		 */
		whereClause.interpret(runtime);

		/*
		 * pop from stack
		 */
		Object obj = runtime.getRuntimeContext().peek().getValue(
				VariableNames.QUERYMATCHES);

		/*
		 * check result type, has to be an instance of ITupleSequence<?>
		 */
		if (!(obj instanceof QueryMatches)) {
			throw new TMQLRuntimeException(
					"Invalid interpretation of expression where-clause. Has to return an instance of QueryMatches.");
		}
		QueryMatches matches = (QueryMatches) obj;

		QueryMatches result = new QueryMatches(runtime);
		/*
		 * iterate of all bindings if bindings and results are not empty
		 */
		if (!bindings.isEmpty()) {
			if (!matches.isEmpty()) {
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
						if (bindings.getOrderedKeys().contains(entry.getKey())) {
							satisfy = bindings.getPossibleValuesForVariable(
									entry.getKey()).contains(entry.getValue());
						}
						if (!satisfy) {
							break;
						}
					}
					if (satisfy) {
						result.add(tuple);
					}
				}
			}
		} else {
			result = matches;
		}
		return result;
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
	 * @return the query matches containing the results of the interpretation of
	 *         the sub-expression
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpreteForClauses(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * Cache to store all possible bindings of contained variables
		 */
		QueryMatches[] bindings = extractArguments(runtime, ForClause.class);
		return new QueryMatches(runtime, bindings);
	}
}
