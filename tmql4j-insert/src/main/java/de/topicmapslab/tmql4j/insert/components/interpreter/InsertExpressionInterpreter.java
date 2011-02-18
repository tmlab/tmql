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

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.insert.grammar.productions.InsertClause;
import de.topicmapslab.tmql4j.insert.grammar.productions.InsertExpression;
import de.topicmapslab.tmql4j.insert.grammar.productions.WhereClause;
import de.topicmapslab.tmql4j.insert.util.InsertHandler;

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
public class InsertExpressionInterpreter extends ExpressionInterpreterImpl<InsertExpression> {
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
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		Context newContext = new Context(context);
		/*
		 * check if where clause exists and any variable is contained
		 */
		if (containsExpressionsType(WhereClause.class)) {
			QueryMatches matches = interpreteWhereClause(runtime, newContext, optionalArguments);
			newContext.setContextBindings(matches);
		}
		return interpretInsertClause(runtime, newContext, optionalArguments);
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
	 *            the querying context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the query matches containing the results of the interpretation of
	 *         the sub-expression
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretInsertClause(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * only one select clause has to be contained
		 */
		if (!containsExpressionsType(InsertClause.class)) {
			throw new TMQLRuntimeException("Invalid structure. no insert clause.");
		}

		/*
		 * extract the select clause
		 */
		IExpressionInterpreter<InsertClause> interpreter = getInterpretersFilteredByEypressionType(runtime, InsertClause.class).get(0);
		/*
		 * call sub expression
		 */
		QueryMatches matches = interpreter.interpret(runtime, context, optionalArguments);
		Context newContext = new Context(context);
		newContext.setContextBindings(matches);
		/*
		 * call insert-handler to add new content and store number of added
		 * items
		 */
		InsertHandler insertHandler = new InsertHandler(runtime, newContext);
		return QueryMatches.asQueryMatchNS(runtime, insertHandler.insert(matches.getPossibleValuesForVariable()));
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
	 *            the querying context
	 * @param optionalArguments
	 *            any optional arguments
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
		return whereClause.interpret(runtime, context, optionalArguments);
	}

}
