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

import static de.topicmapslab.tmql4j.path.grammar.productions.FilterPostfix.TYPE_BOOLEAN_EXPRESSION;
import static de.topicmapslab.tmql4j.path.grammar.productions.FilterPostfix.TYPE_BOUNDS_FILTER;
import static de.topicmapslab.tmql4j.path.grammar.productions.FilterPostfix.TYPE_INDEX_FILTER;
import static de.topicmapslab.tmql4j.path.grammar.productions.FilterPostfix.TYPE_SCOPE_FILTER;
import static de.topicmapslab.tmql4j.path.grammar.productions.FilterPostfix.TYPE_SHORTCUT_BOUNDS_FILTER;
import static de.topicmapslab.tmql4j.path.grammar.productions.FilterPostfix.TYPE_SHORTCUT_INDEX_FILTER;
import static de.topicmapslab.tmql4j.path.grammar.productions.FilterPostfix.TYPE_SHORTCUT_SCOPE_FILTER;
import static de.topicmapslab.tmql4j.path.grammar.productions.FilterPostfix.TYPE_SHORTCUT_TYPE_FILTER;
import static de.topicmapslab.tmql4j.path.grammar.productions.FilterPostfix.TYPE_TYPE_FILTER;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
import de.topicmapslab.tmql4j.path.components.navigation.NavigationAxis;
import de.topicmapslab.tmql4j.path.components.navigation.NavigationHandler;
import de.topicmapslab.tmql4j.path.components.navigation.model.INavigationAxis;
import de.topicmapslab.tmql4j.path.components.processor.core.Context;
import de.topicmapslab.tmql4j.path.exception.NavigationException;
import de.topicmapslab.tmql4j.path.grammar.productions.BooleanExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.FilterPostfix;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * 
 * Special interpreter class to interpret filter-postfixes.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * filter-postfix ::= [ boolean-expression ]
 * </p>
 * <p>
 * filter-postfix ::= // anchor ==> [ ^ anchor ]
 * </p>
 * <p>
 * filter-postfix ::= [ integer ] ==> [ $# == integer ]
 * </p>
 * <p>
 * filter-postfix ::= [ integer-1 .. integer-2 ] ==> [ integer-1 <= $# & $# <
 * integer-2 ]
 * </p>
 * <p>
 * filter-postfix ::= [ @ scope ] | @ scope
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class FilterPostfixInterpreter extends ExpressionInterpreterImpl<FilterPostfix> {

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
	public FilterPostfixInterpreter(FilterPostfix ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		switch (getGrammarTypeOfExpression()) {
		/*
		 * is boolean-expression
		 */
		case TYPE_BOOLEAN_EXPRESSION: {
			return interpretBooleanExpression(runtime, context, optionalArguments);
		}
			/*
			 * is @ anchor or [ @ anchor]
			 */
		case TYPE_SHORTCUT_SCOPE_FILTER:
		case TYPE_SCOPE_FILTER: {
			return interpreScopeFilter(runtime, context, optionalArguments);
		}
			/*
			 * is // anchor or [ ^ anchor]
			 */
		case TYPE_SHORTCUT_TYPE_FILTER:
		case TYPE_TYPE_FILTER: {
			return interpretTypesAnchor(runtime, context, optionalArguments);
		}
			/*
			 * is [integer] or [$# == #integer]
			 */
		case TYPE_SHORTCUT_INDEX_FILTER:
		case TYPE_INDEX_FILTER: {
			return interpretIntegerIndex(runtime, context, optionalArguments);
		}
			/*
			 * is [integer .. integer] or [#integer <= $# & $# < #integer]
			 */
		case TYPE_BOUNDS_FILTER:
		case TYPE_SHORTCUT_BOUNDS_FILTER: {
			return interpretIntegerBounds(runtime, context, optionalArguments);
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
	private QueryMatches interpretBooleanExpression(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * return empty matches if context is empty
		 */
		if (context.getContextBindings() == null) {
			return QueryMatches.emptyMatches();
		}
		/*
		 * Extract Boolean-expression
		 */
		IExpressionInterpreter<BooleanExpression> ex = getInterpretersFilteredByEypressionType(runtime, BooleanExpression.class).get(0);
		/*
		 * Iterate over all tuple of current sequence
		 */
		QueryMatches storedMatches = context.getContextBindings();
		QueryMatches filteredMatches = new QueryMatches(runtime);

		for (int index = 0; index < storedMatches.getMatches().size(); index++) {

			Map<String, Object> filteredTuple = HashUtil.getHashMap();

			for (final String variable : storedMatches.getOrderedKeys()) {
				Object object = storedMatches.getMatches().get(index).get(variable);
				List<Object> results = HashUtil.getList();
				Context newContext = new Context(context);
				newContext.setCurrentIndex(index);			
				newContext.setContextBindings(null);
				/*
				 * check if value is a sequence
				 */
				if (object instanceof Collection<?>) {
					/*
					 * iterate over sequence
					 */
					for (Object object_ : (Collection<?>) object) {
						newContext.setCurrentNode(object_);
						/*
						 * check boolean expression
						 */
						QueryMatches iteration = ex.interpret(runtime, newContext, optionalArguments);
						if (!iteration.isEmpty()) {
							results.add(object_);
						}
					}
				}
				/*
				 * value is not a sequence and not null
				 */
				else if (object != null) {
					newContext.setCurrentNode(object);
					/*
					 * check boolean expression
					 */
					QueryMatches iteration = ex.interpret(runtime, newContext, optionalArguments);
					if (!iteration.isEmpty()) {
						results.add(object);
					}
				}

				/*
				 * check if tuple-sequence is not empty
				 */
				if (!results.isEmpty()) {
					filteredTuple.put(variable, results.size() == 1 ? results.get(0) : results);
				}
			}
			/*
			 * check if tuple is empty
			 */
			if (!filteredTuple.isEmpty()) {
				filteredMatches.add(filteredTuple);
			}
		}
		return filteredMatches;
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
	private QueryMatches interpretTypesAnchor(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * Extract value of anchor specified the type to match
		 */
		String anchor = null;
		/*
		 * is // anchor
		 */
		if (getGrammarTypeOfExpression() == TYPE_SHORTCUT_TYPE_FILTER) {
			anchor = getTokens().get(1);
		}
		/*
		 * is [ ^ anchor ]
		 */
		else {
			anchor = getTokens().get(2);
		}

		/*
		 * Try to resolve item of current topic map
		 */
		Construct construct = runtime.getConstructResolver().getConstructByIdentifier(context, anchor);
		if (construct == null) {
			logger.warn("Cannot interpret given anchor '" + anchor + "'.");
			return QueryMatches.emptyMatches();
		}

		/*
		 * Iterate over all values of tuple sequence and match to resolved type
		 */
		if (context.getContextBindings() != null) {
			try {
				INavigationAxis axis = NavigationHandler.buildHandler().lookup(NavigationAxis.types);
				List<Object> values = HashUtil.getList();
				/*
				 * iterate over all values of non-scoped variable
				 */
				for (Object object : context.getContextBindings().getPossibleValuesForVariable()) {
					/*
					 * check if value is a topic and a type of the current item
					 */
					if (axis.navigateForward(object).contains(construct)) {
						values.add(object);
					}
				}
				return QueryMatches.asQueryMatchNS(runtime, values.toArray());

			} catch (NavigationException e) {
				throw new TMQLRuntimeException(e);
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
	private QueryMatches interpreScopeFilter(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * Extract value of anchor specified the type to match
		 */
		String anchor = null;
		/*
		 * is @ anchor
		 */
		if (getGrammarTypeOfExpression() == TYPE_SHORTCUT_SCOPE_FILTER) {
			anchor = getTokens().get(1);
		}
		/*
		 * is [ @ anchor ]
		 */
		else {
			anchor = getTokens().get(2);
		}

		/*
		 * Try to resolve item of current topic map
		 */
		Construct construct = runtime.getConstructResolver().getConstructByIdentifier(context, anchor);
		if (construct == null) {
			logger.warn("Cannot interpret given anchor '" + anchor + "'.");
			return QueryMatches.emptyMatches();
		}

		/*
		 * Iterate over all values of tuple sequence and match to resolved type
		 */
		if (context.getContextBindings() != null) {
			List<Object> values = HashUtil.getList();
			/*
			 * iterate over all values of non-scoped variable
			 */
			for (Object object : context.getContextBindings().getPossibleValuesForVariable()) {
				/*
				 * check if value is a scoped item and the scope contains the
				 * given theme
				 */
				if (object instanceof Scoped && ((Scoped) object).getScope().contains(construct)) {
					values.add(object);
				}
			}
			return QueryMatches.asQueryMatchNS(runtime, values.toArray());
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
	private QueryMatches interpretIntegerIndex(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		int index = -1;
		/*
		 * extract index by grammar type
		 */
		if (getGrammarTypeOfExpression() == TYPE_SHORTCUT_INDEX_FILTER) {
			index = Integer.parseInt(getTokens().get(1));
		} else {
			index = Integer.parseInt(getTokens().get(3));
		}
		/*
		 * extract sequence from the top of the stack
		 */
		if (context.getContextBindings() != null) {
			if (index < context.getContextBindings().size() && index >= 0) {
				return context.getContextBindings().select(index, index + 1);
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
	private QueryMatches interpretIntegerBounds(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {

		int upper = -1;
		int lower = -1;
		/*
		 * extract index range by grammar type
		 */
		if (getGrammarTypeOfExpression() == TYPE_SHORTCUT_BOUNDS_FILTER) {
			lower = Integer.parseInt(getTokens().get(1));
			upper = Integer.parseInt(getTokens().get(3));
		} else {
			lower = Integer.parseInt(getTokens().get(1));
			upper = Integer.parseInt(getTokens().get(7));
		}
		/*
		 * extract sequence from the top of the stack
		 */
		if (context.getContextBindings() != null) {
			return context.getContextBindings().select(lower, upper);
		}
		return QueryMatches.emptyMatches();
	}
}
