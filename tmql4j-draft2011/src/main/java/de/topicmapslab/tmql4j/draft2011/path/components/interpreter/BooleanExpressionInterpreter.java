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
package de.topicmapslab.tmql4j.draft2011.path.components.interpreter;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.BooleanExpression;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * 
 * Special interpreter class to interpret boolean-expression.
 * 
 * <p>
 * The grammar production rule of the expression is: <code> * 
 * <p>
 *   boolean-expression ::= boolean-expression | boolean-expression |
 * </p>
 * <p>
 * boolean-expression & boolean-expression | boolean-primitive
 * </p>
 * <p>
 * boolean-expression ::= forall-clause
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class BooleanExpressionInterpreter extends ExpressionInterpreterImpl<BooleanExpression> {

	/**
	 * the Logger
	 */
	private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public BooleanExpressionInterpreter(BooleanExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		switch (getGrammarTypeOfExpression()) {
			/*
			 * is boolean-expression ::= boolean-expression | boolean-expression
			 */
			case BooleanExpression.TYPE_DISJUNCTION: {
				return interpretDisjunction(runtime, context, optionalArguments);
			}
				/*
				 * is boolean-expression ::= boolean-expression & boolean-expression
				 */
			case BooleanExpression.TYPE_CONJUNCTION: {
				return interpretConjunction(runtime, context, optionalArguments);
			}
				/*
				 * is boolean-expression ::= boolean-primitive
				 */
			case BooleanExpression.TYPE_BOOLEAN_PRIMITIVE: {
				return interpretBooleanPrimitve(runtime, context, optionalArguments);
			}
				/*
				 * is boolean-expression ::= every binding-set satisfies boolean-expression
				 */
			case BooleanExpression.TYPE_FORALL_CLAUSE: {
				return interpretForAllClause(runtime, context, optionalArguments);
			}
			default:
				throw new TMQLRuntimeException("The state of this expression is invalid!");
		}
	}

	/**
	 * The method is called to interpret the given sub-expression by using the given runtime. The interpretation will
	 * call the sub-expression if the given expression isn't a leaf in parsing-tree.
	 * 
	 * <p>
	 * The interpretation will transform the value on top of the stack and put its results also on top.
	 * </p>
	 * 
	 * @param runtime
	 *            the runtime which contains all necessary information for querying process
	 * @param context
	 *            the current querying context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the results of children execution
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretDisjunction(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * cache of overall results
		 */
		QueryMatches queryMatches = new QueryMatches(runtime);

		/*
		 * Iterate over all boolean-expression
		 */
		for (IExpressionInterpreter<?> ex : getInterpreters(runtime)) {
			IContext newContext = new Context(context);
			/*
			 * call subexpression
			 */
			QueryMatches matches = ex.interpret(runtime, newContext, optionalArguments);
			if (!matches.isEmpty()) {
				if (queryMatches.isEmpty()) {
					queryMatches.add(matches);
				} else {
					queryMatches.disjunction(matches);
				}
			}
		}
		logger.info("Finished! Boolean expression finished successful!");
		return queryMatches;
	}

	/**
	 * The method is called to interpret the given sub-expression by using the given runtime. The interpretation will
	 * call the sub-expression if the given expression isn't a leaf in parsing-tree.
	 * 
	 * <p>
	 * The interpretation will transform the value on top of the stack and put its results also on top.
	 * </p>
	 * 
	 * @param runtime
	 *            the runtime which contains all necessary information for querying process
	 * @param context
	 *            the current querying context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the results of children execution
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	@SuppressWarnings("unchecked")
	private QueryMatches interpretConjunction(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {

		/*
		 * cache of overall results
		 */
		QueryMatches queryMatches = new QueryMatches(runtime);

		boolean satisfy = true;
		Context newContext = new Context(context);
		/*
		 * Iterate over all boolean-expression
		 */
		for (IExpressionInterpreter<?> ex : getInterpreters(runtime)) {
			/*
			 * call subexpression
			 */
			QueryMatches matches = ex.interpret(runtime, newContext, optionalArguments);
			if (matches.isEmpty()) {
				satisfy = false;
				return QueryMatches.emptyMatches();
			} else if (queryMatches.isEmpty()) {
				queryMatches.add(matches);
			} else {
				boolean missingVariable = false;
				for (String key : ex.getVariables()) {
					List<Object> matchesObject = matches.getPossibleValuesForVariable(key);
					if (matchesObject.isEmpty() || (matchesObject.get(0) instanceof Collection<?> && ((Collection<Object>) matchesObject.get(0)).isEmpty())) {
						missingVariable = true;
					}
				}
				if (missingVariable) {
					queryMatches = new QueryMatches(runtime);
					break;
				} else {
					queryMatches.conjunction(matches);
				}
			}

			/*
			 * Store for later reuse
			 */
			newContext.setContextBindings(queryMatches);

			/*
			 * check if result is empty -> boolean-expression returns empty tuple sequence
			 */
			if (queryMatches.isEmpty() || !satisfy) {
				/*
				 * log it :)
				 */
				logger.info("Finished! Boolean expression return an empty query match!");
				return QueryMatches.emptyMatches();
			}
		}
		return queryMatches;
	}

	/**
	 * The method is called to interpret the given sub-expression by using the given runtime. The interpretation will
	 * call the sub-expression if the given expression isn't a leaf in parsing-tree.
	 * 
	 * <p>
	 * The interpretation will transform the value on top of the stack and put its results also on top.
	 * </p>
	 * 
	 * @param runtime
	 *            the runtime which contains all necessary information for querying process
	 * @param context
	 *            the current querying context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the results of children execution
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretBooleanPrimitve(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * redirect to sub-expression
		 */
		IExpressionInterpreter<?> ex = getInterpreters(runtime).get(0);
		return ex.interpret(runtime, context, optionalArguments);

	}

	/**
	 * The method is called to interpret the given sub-expression by using the given runtime. The interpretation will
	 * call the sub-expression if the given expression isn't a leaf in parsing-tree.
	 * 
	 * <p>
	 * The interpretation will transform the value on top of the stack and put its results also on top.
	 * </p>
	 * 
	 * @param runtime
	 *            the runtime which contains all necessary information for querying process
	 * @param context
	 *            the current querying context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the results of children execution
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretForAllClause(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * redirect to subexpression
		 */
		IExpressionInterpreter<?> ex = getInterpreters(runtime).get(0);
		return ex.interpret(runtime, context, optionalArguments);
	}

}
