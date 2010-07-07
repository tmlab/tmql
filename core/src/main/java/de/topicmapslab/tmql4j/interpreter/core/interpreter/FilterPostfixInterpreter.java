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

import static de.topicmapslab.tmql4j.parser.core.expressions.FilterPostfix.TYPE_BOOLEAN_EXPRESSION;
import static de.topicmapslab.tmql4j.parser.core.expressions.FilterPostfix.TYPE_BOUNDS_FILTER;
import static de.topicmapslab.tmql4j.parser.core.expressions.FilterPostfix.TYPE_INDEX_FILTER;
import static de.topicmapslab.tmql4j.parser.core.expressions.FilterPostfix.TYPE_SCOPE_FILTER;
import static de.topicmapslab.tmql4j.parser.core.expressions.FilterPostfix.TYPE_SHORTCUT_BOUNDS_FILTER;
import static de.topicmapslab.tmql4j.parser.core.expressions.FilterPostfix.TYPE_SHORTCUT_INDEX_FILTER;
import static de.topicmapslab.tmql4j.parser.core.expressions.FilterPostfix.TYPE_SHORTCUT_SCOPE_FILTER;
import static de.topicmapslab.tmql4j.parser.core.expressions.FilterPostfix.TYPE_SHORTCUT_TYPE_FILTER;
import static de.topicmapslab.tmql4j.parser.core.expressions.FilterPostfix.TYPE_TYPE_FILTER;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.Construct;
import org.tmapi.core.Scoped;

import de.topicmapslab.java.navigation.exception.NavigationException;
import de.topicmapslab.java.navigation.model.INavigationAxis;
import de.topicmapslab.tmql4j.api.exceptions.DataBridgeException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.model.tuplesequence.ITupleSequence;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.parser.core.expressions.BooleanExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.FilterPostfix;

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
public class FilterPostfixInterpreter extends
		ExpressionInterpreterImpl<FilterPostfix> {

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
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		switch (getGrammarTypeOfExpression()) {
		/*
		 * is boolean-expression
		 */
		case TYPE_BOOLEAN_EXPRESSION: {
			interpretBooleanExpression(runtime);
		}
			break;
		/*
		 * is @ anchor or [ @ anchor]
		 */
		case TYPE_SHORTCUT_SCOPE_FILTER:
		case TYPE_SCOPE_FILTER: {
			interpreScopeFilter(runtime);
			break;
		}
			/*
			 * is // anchor or [ ^ anchor]
			 */
		case TYPE_SHORTCUT_TYPE_FILTER:
		case TYPE_TYPE_FILTER: {
			interpretTypesAnchor(runtime);
		}
			break;
		/*
		 * is [integer] or [$# == #integer]
		 */
		case TYPE_SHORTCUT_INDEX_FILTER:
		case TYPE_INDEX_FILTER: {
			interpretIntegerIndex(runtime);
		}
			break;
		/*
		 * is [integer .. integer] or [#integer <= $# & $# < #integer]
		 */
		case TYPE_BOUNDS_FILTER:
		case TYPE_SHORTCUT_BOUNDS_FILTER: {
			interpretIntegerBounds(runtime);
		}
			break;
		default:
			throw new TMQLRuntimeException("Unknown state");
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
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private void interpretBooleanExpression(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * Extract Boolean-expression
		 */
		IExpressionInterpreter<BooleanExpression> ex = getInterpretersFilteredByEypressionType(
				runtime, BooleanExpression.class).get(0);
		/*
		 * Extract current tuple-sequence
		 */
		Object seq = runtime.getRuntimeContext().peek().getValue(
				VariableNames.POSTFIXED);
		/*
		 * Iterate over all tuple of current sequence
		 */
		if (seq instanceof QueryMatches) {

			QueryMatches storedMatches = (QueryMatches) seq;
			QueryMatches filteredMatches = new QueryMatches(runtime);

			for (int index = 0; index < storedMatches.getMatches().size(); index++) {

				Map<String, Object> filteredTuple = HashUtil.getHashMap();

				for (final String variable : storedMatches.getOrderedKeys()) {
					Object object = storedMatches.getMatches().get(index).get(
							variable);
					ITupleSequence<Object> results = runtime.getProperties()
							.newSequence();

					/*
					 * create new variable set on top of the stack
					 */
					runtime.getRuntimeContext().push();
					runtime.getRuntimeContext().peek().remove(
							VariableNames.ITERATED_BINDINGS);

					/*
					 * check if value is a sequence
					 */
					if (object instanceof ITupleSequence<?>) {
						boolean firstCall = true;
						/*
						 * iterate over sequence
						 */
						for (Object object_ : (ITupleSequence<?>) object) {
							if (!firstCall) {
								runtime.getRuntimeContext().push();
								runtime.getRuntimeContext().peek().remove(
										VariableNames.ITERATED_BINDINGS);
							}
							/*
							 * Set value to $#
							 */
							runtime.getRuntimeContext().peek().setValue(
									VariableNames.CURRENT_POISTION, index);

							/*
							 * set current tuple to @_
							 */
							runtime.getRuntimeContext().peek().setValue(
									VariableNames.CURRENT_TUPLE, object_);
							/*
							 * check boolean expression
							 */
							ex.interpret(runtime);

							/*
							 * pop from stack
							 */
							Object obj = runtime.getRuntimeContext().pop()
									.getValue(VariableNames.QUERYMATCHES);

							/*
							 * check result type, has to be an instance of
							 * ITupleSequence<?>
							 */
							if (obj == null || !(obj instanceof QueryMatches)) {
								throw new TMQLRuntimeException(
										"Invalid interpretation of expression boolean-expression, has to return a result of QueryMatches");
							}

							if (!((QueryMatches) obj).isEmpty()) {
								results.add(object_);
							}
							firstCall = false;
						}
					}
					/*
					 * value is not a sequence and not null
					 */
					else if (object != null) {

						/*
						 * Set value to $#
						 */
						runtime.getRuntimeContext().peek().setValue(
								VariableNames.CURRENT_POISTION, index);
						/*
						 * set current tuple to @_
						 */
						runtime.getRuntimeContext().peek().setValue(
								VariableNames.CURRENT_TUPLE, object);
						/*
						 * check boolean expression
						 */
						ex.interpret(runtime);

						/*
						 * pop from stack
						 */
						Object obj = runtime.getRuntimeContext().pop()
								.getValue(VariableNames.QUERYMATCHES);

						/*
						 * check result type, has to be an instance of
						 * ITupleSequence<?>
						 */
						if (obj == null || !(obj instanceof QueryMatches)) {
							throw new TMQLRuntimeException(
									"Invalid interpretation of expression boolean-expression, has to return a result of QueryMatches");
						}
						if (!((QueryMatches) obj).isEmpty()) {
							results.add(object);
						}
					}

					/*
					 * check if tuple-sequence is not empty
					 */
					if (!results.isEmpty()) {
						filteredTuple.put(variable,
								results.size() == 1 ? results.get(0) : results);
					}
				}
				/*
				 * check if tuple is empty
				 */
				if (!filteredTuple.isEmpty()) {
					filteredMatches.add(filteredTuple);
				}

			}
			/*
			 * set overall results to variable stack
			 */
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.QUERYMATCHES, filteredMatches);
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
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private void interpretTypesAnchor(TMQLRuntime runtime)
			throws TMQLRuntimeException {
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
		Construct construct = null;
		try {
			construct = runtime.getDataBridge().getConstructByIdentifier(
					runtime, anchor);
		} catch (DataBridgeException e) {
			logger.warn("Cannot find specified type " + anchor);
		}
		/*
		 * return empty result if type is unknown
		 */
		if (construct == null) {
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.QUERYMATCHES, new QueryMatches(runtime));
			return;
		}

		/*
		 * Iterate over all values of tuple sequence and match to resolved type
		 */
		Object seq = runtime.getRuntimeContext().peek().getValue(
				VariableNames.POSTFIXED);
		QueryMatches results = new QueryMatches(runtime);
		if (seq instanceof QueryMatches) {
			QueryMatches matches = (QueryMatches) seq;
			INavigationAxis axis = runtime.getDataBridge()
					.getImplementationOfTMQLAxis(runtime, "types");
			/*
			 * iterate over all values of non-scoped variable
			 */
			for (Object object : matches.getPossibleValuesForVariable()) {
				/*
				 * check if value is a topic and a type of the current item
				 */
				try {
					if (axis.navigateForward(object).contains(construct)) {
						Map<String, Object> tuple = HashUtil.getHashMap();
						tuple.put(QueryMatches.getNonScopedVariable(), object);
						results.add(tuple);
					}
				} catch (NavigationException e) {
					throw new TMQLRuntimeException(e);
				}
			}
		}
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);
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
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private void interpreScopeFilter(TMQLRuntime runtime)
			throws TMQLRuntimeException {
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
		Construct construct = null;
		try {
			construct = runtime.getDataBridge().getConstructByIdentifier(
					runtime, anchor);
		} catch (DataBridgeException e) {
			logger.warn("Cannot interpret given anchor '" + anchor + "'.");
		}
		QueryMatches results = new QueryMatches(runtime);
		if (construct != null) {
			/*
			 * Iterate over all values of tuple sequence and match to resolved
			 * type
			 */
			Object seq = runtime.getRuntimeContext().peek().getValue(
					VariableNames.POSTFIXED);
			if (seq instanceof QueryMatches) {
				QueryMatches matches = (QueryMatches) seq;
				/*
				 * iterate over all values of non-scoped variable
				 */
				for (Object object : matches.getPossibleValuesForVariable()) {
					/*
					 * check if value is a scoped item and the scope contains
					 * the given theme
					 */
					if (object instanceof Scoped
							&& ((Scoped) object).getScope().contains(construct)) {
						Map<String, Object> tuple = HashUtil.getHashMap();
						tuple.put(QueryMatches.getNonScopedVariable(), object);
						results.add(tuple);
					}
				}
			}
		}
		/*
		 * add values to new instance of tuple sequence
		 */
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);
		/*
		 * log it :)
		 */
		logger.info("Finished! Results: " + results);
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
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private void interpretIntegerIndex(TMQLRuntime runtime)
			throws TMQLRuntimeException {

		int index = -1;
		/*
		 * extract index by grammar type
		 */
		if (getGrammarTypeOfExpression() == TYPE_SHORTCUT_INDEX_FILTER) {
			index = Integer.parseInt(getTokens().get(1));
		} else {
			index = Integer.parseInt(getTokens().get(3));
		}

		QueryMatches results = new QueryMatches(runtime);
		/*
		 * extract sequence from the top of the stack
		 */
		Object seq = runtime.getRuntimeContext().peek().getValue(
				VariableNames.POSTFIXED);
		if (seq instanceof QueryMatches) {
			QueryMatches matches = (QueryMatches) seq;
			if (index < matches.size() && index >= 0 ) {
				/*
				 * create new sequence and add the tuple at the specific index
				 */
				results.add(matches.get(index));
			}
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.QUERYMATCHES, results);
		} else {
			throw new TMQLRuntimeException("unsupported results type");
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
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private void interpretIntegerBounds(TMQLRuntime runtime)
			throws TMQLRuntimeException {

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
		Object seq = runtime.getRuntimeContext().peek().getValue(
				VariableNames.POSTFIXED);
		if (seq instanceof QueryMatches) {
			/*
			 * create a new sequence and add all tuples in the selection-window
			 */
			QueryMatches matches = (QueryMatches) seq;
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.QUERYMATCHES,
					matches.select(lower, upper));
		} else {
			throw new TMQLRuntimeException("unsupported results type");
		}
	}
}
