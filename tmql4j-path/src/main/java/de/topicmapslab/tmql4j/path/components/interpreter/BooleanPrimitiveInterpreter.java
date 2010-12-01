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
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.Construct;
import org.tmapi.core.Scoped;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.components.processor.core.Context;
import de.topicmapslab.tmql4j.path.grammar.productions.BooleanExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.BooleanPrimitive;
import de.topicmapslab.tmql4j.path.grammar.productions.ExistsClause;
import de.topicmapslab.tmql4j.path.grammar.productions.FilterPostfix;
import de.topicmapslab.tmql4j.path.grammar.productions.ForAllClause;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * 
 * Special interpreter class to interpret boolean-primitives.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * boolean-primitive ::= '(' boolean-expression ')'
 * </p>
 * 
 * <p>
 * boolean-primitive ::= 'not' boolean-primitive
 * </p>
 * 
 * <p>
 * boolean-primitive ::= forall-clause
 * </p>
 * 
 * <p>
 * boolean-primitive ::= exists-clause
 * </p>
 * 
 * <p>
 * boolean-primitive ::= '@' anchor
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class BooleanPrimitiveInterpreter extends ExpressionInterpreterImpl<BooleanPrimitive> {

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
	public BooleanPrimitiveInterpreter(BooleanPrimitive ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		switch (getGrammarTypeOfExpression()) {
		/*
		 * is cramped-boolean expression
		 */
		case BooleanPrimitive.TYPE_BOOLEAN_EXPRESSION: {
			return interpretCrampedBooleanExpression(runtime, context, optionalArguments);
		}
			/*
			 * is not-expression
			 */
		case BooleanPrimitive.TYPE_NOT_EXPRESSION: {
			return interpretNotExpression(runtime, context, optionalArguments);
		}
			/*
			 * is for-all-clause
			 */
		case BooleanPrimitive.TYPE_EVERY_CLAUSE: {
			return interpretForAllExpression(runtime, context, optionalArguments);
		}
			/*
			 * is exists-clause
			 */
		case BooleanPrimitive.TYPE_EXISTS_CLAUSE: {
			return interpretExsistsExpression(runtime, context, optionalArguments);
		}
			/*
			 * is scoped-expression
			 */
		case BooleanPrimitive.TYPE_SCOPED_EXPRESSION: {
			return interpretScopedExpression(runtime, context, optionalArguments);
		}
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
	 * @return the query matches
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretCrampedBooleanExpression(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {

		/*
		 * redirect to sub-expression
		 */
		IExpressionInterpreter<BooleanExpression> ex = getInterpretersFilteredByEypressionType(runtime, BooleanExpression.class).get(0);
		return ex.interpret(runtime, context, optionalArguments);
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
	 * @return the query matches
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	// TODO refactor
	private QueryMatches interpretNotExpression(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		Context newContext = new Context(context);
		/*
		 * get interpreter of sub-expression
		 */
		IExpressionInterpreter<?> ex = getInterpreters(runtime).get(0);
		newContext.setContextBindings(QueryMatches.emptyMatches());
		/*
		 * Call subexpression
		 */
		QueryMatches results = ex.interpret(runtime, newContext, optionalArguments);
		QueryMatches negation = new QueryMatches(runtime);
		/*
		 * solution a: check if iteration results of pre-proceeded expression
		 * are available
		 */
		if (context.getContextBindings() != null) {
			/*
			 * iterate over pre-proceeded results and remove matches of
			 * contained boolean-expression
			 */
			for (Map<String, Object> tuple : context.getContextBindings()) {
				boolean satisfy = true;
				for (Entry<String, Object> entry : tuple.entrySet()) {
					if (results.getPossibleValuesForVariable(entry.getKey()).contains(entry.getValue())) {
						satisfy = false;
						break;
					}
				}
				if (satisfy) {
					negation.add(tuple);
				}
			}

		}
		/*
		 * check if contained boolean-expression returns negative matches
		 */
		else if (!results.getNegation().isEmpty()) {
			negation.add(results.getNegation());
		}
		/*
		 * 
		 */
		else {
			Set<QueryMatches> matches = HashUtil.getHashSet();
			/*
			 * iterate over all variables
			 */
			for (final String variable : getVariables()) {
				/*
				 * add possible variable bindings
				 */
				QueryMatches match = new QueryMatches(runtime);
				// match.convertToTuples((Set<Object>) new
				// BooleanPrimitiveVariableBindingOptimizer(runtime).optimize(ex,
				// variable), variable);
				match.convertToTuples(context.getQuery().getTopicMap().getTopics(), variable);
				matches.add(match);
			}

			/*
			 * iterate over all possible variable bindings
			 */
			for (Map<String, Object> tuple : new QueryMatches(runtime, matches)) {
				if (results.getMatches().contains(tuple)) {
					continue;
				}
				/*
				 * push new set on the top of the stack and set @_
				 */
				newContext = new Context(context);
				newContext.setCurrentTuple(tuple);
				/*
				 * call sub-expression
				 */
				QueryMatches set = ex.interpret(runtime, newContext, optionalArguments);
				if (!set.isEmpty()) {
					negation.add(set);
				}
			}
		}
		return negation;
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
	 * @return the query matches
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretForAllExpression(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * redirect to sub-expression
		 */
		IExpressionInterpreter<ForAllClause> ex = getInterpretersFilteredByEypressionType(runtime, ForAllClause.class).get(0);
		return ex.interpret(runtime, context, optionalArguments);
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
	 * @return the query matches
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretExsistsExpression(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * redirect to sub-expression
		 */
		IExpressionInterpreter<ExistsClause> ex = getInterpretersFilteredByEypressionType(runtime, ExistsClause.class).get(0);
		IContext newContext = new Context(context);
		QueryMatches results = ex.interpret(runtime, newContext, optionalArguments);
		if (!results.isEmpty()) {
			/*
			 * called by filter
			 */
			if (getExpression().isChildOf(FilterPostfix.class) && context.getCurrentNode() != null) {
				return QueryMatches.asQueryMatchNS(runtime, context.getCurrentNode());
			}
			return results;
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
	 * @return the query matches
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretScopedExpression(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * Extract anchor specifying the scope
		 */
		String anchor = getTokens().get(0);
		/*
		 * Try to resolve Topic as scope
		 */
		Construct scope = runtime.getConstructResolver().getConstructByIdentifier(context, anchor);
		if (scope == null) {
			logger.warn("Cannot find specified scope " + anchor);
			return QueryMatches.emptyMatches();
		}

		/*
		 * Extract value of @_
		 */
		Object obj = context.getCurrentNode();
		if (obj != null && obj instanceof Scoped) {
			/*
			 * Check if scopes containing the specified theme
			 */
			if (((Scoped) obj).getScope().contains(scope)) {
				return QueryMatches.asQueryMatchNS(runtime, obj);
			}
		}
		return QueryMatches.emptyMatches();
	}

}
