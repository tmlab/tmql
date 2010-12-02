/**
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2009 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.insert.components.interpreter;

import java.util.Map;
import java.util.logging.Logger;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.insert.grammar.productions.InsertClause;
import de.topicmapslab.tmql4j.insert.grammar.productions.InsertExpression;
import de.topicmapslab.tmql4j.insert.grammar.productions.WhereClause;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * 
 * Special interpreter class to interpret insert-expressions.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 *  insert-expression ::= INSERT tm-content [ WHERE boolean-expression ]
 * </code>
 * </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class InsertExpressionInterpreter extends
		ExpressionInterpreterImpl<InsertExpression> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public InsertExpressionInterpreter(InsertExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		QueryMatches results = new QueryMatches(runtime);
		/**
		 * Log it
		 */
		Logger.getLogger(getClass().getSimpleName()).info("Start");

		/**
		 * check if where clause exists and any variable is contained
		 */
		if (containsExpressionsType(WhereClause.class)) {
			results = interpreteWhereClause(runtime);
		}

		results = interpretInsertClause(runtime, results);

		/**
		 * set overall results on top of the stack
		 */
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);

		/**
		 * Log it
		 */
		Logger.getLogger(getClass().getSimpleName()).info(
				"Finished. Results: " + results);

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
	 *            the satisfying variable bindings of the where-clause
	 * @return the query matches containing the results of the interpretation of
	 *         the sub-expression
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretInsertClause(TMQLRuntime runtime,
			QueryMatches matches) throws TMQLRuntimeException {

		/*
		 * only one select clause has to be contained
		 */
		if (!containsExpressionsType(InsertClause.class)) {
			throw new TMQLRuntimeException(
					"Invalid structure. no insert clause.");
		}

		/*
		 * extract the select clause
		 */
		IExpressionInterpreter<InsertClause> interpreter = getInterpretersFilteredByEypressionType(
				runtime, InsertClause.class).get(0);

		/*
		 * push to the top of the stack
		 */
		runtime.getRuntimeContext().push();
		if (!matches.isEmpty()) {
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.ITERATED_BINDINGS, matches);
		}

		/*
		 * call sub expression
		 */
		interpreter.interpret(runtime);

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
					"Invalid interpretation of expression insert-clause. Has to return an instance of QueryMatches.");
		}

		QueryMatches ctmFragments = (QueryMatches) obj;

		/*
		 * call insert-handler to add new content and store number of added
		 * items
		 */
		InsertHandler insertHandler = new InsertHandler(runtime);
		long count = insertHandler.insert(ctmFragments
				.getPossibleValuesForVariable("$0"));

		QueryMatches results = new QueryMatches(runtime);
		Map<String, Object> tuple = HashUtil.getHashMap();
		tuple.put("$0", count);
		results.add(tuple);

		return results;
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
	private QueryMatches interpreteWhereClause(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * get where-clause
		 */
		IExpressionInterpreter<WhereClause> whereClause = getInterpretersFilteredByEypressionType(
				runtime, WhereClause.class).get(0);

		/*
		 * push on top of the stack
		 */
		runtime.getRuntimeContext().push();

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
		result = matches;

		return result;
	}

}
