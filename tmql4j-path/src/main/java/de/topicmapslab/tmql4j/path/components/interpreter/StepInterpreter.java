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

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.Construct;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.exception.DataBridgeException;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.lexical.MoveForward;
import de.topicmapslab.tmql4j.path.grammar.lexical.ShortcutAxisInstances;
import de.topicmapslab.tmql4j.path.grammar.productions.Step;

/**
 * 
 * Special interpreter class to interpret steps.
 * <p>
 * step ::= ( >> | << ) axis [ anchor ]
 * </p>
 * <p>
 * step ::= // anchor
 * </p>
 * </code> </p>*
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class StepInterpreter extends ExpressionInterpreterImpl<Step> {

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
	public StepInterpreter(Step ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		/*
		 * log it :)
		 */
		logger.info("Start");

		try {

			/*
			 * try to resolve Construct
			 */
			Object anchor = null;
			Construct optional = null;

			/*
			 * is shortcut of types axis
			 */
			INavigationAxis axis = null;
			boolean forward = true;

			/*
			 * is // anchor
			 */
			if (getTmqlTokens().get(0).equals(ShortcutAxisInstances.class)) {

				/*
				 * set anchor ( topic-type )
				 */
				try {
					anchor = runtime.getDataBridge().getConstructByIdentifier(
							runtime, getTokens().get(0));
				} catch (DataBridgeException e) {
					logger.warn("Cannot find optional type argument "
							+ getTokens().get(0));
				}
				/*
				 * set types axis
				 */
				axis = runtime.getDataBridge().getImplementationOfTMQLAxis(
						runtime, "types");
				/*
				 * set direction
				 */
				forward = true;
			}
			/*
			 * is ( >> | << ) axis [ anchor ]
			 */
			else {

				/*
				 * Describing what to do:
				 * 
				 * 1. Fetch the value of variable @_ ( representing the current
				 * tuple [topic map construct] to navigate))
				 * 
				 * 2. Look up navigation axis by name
				 * 
				 * 3. If optional anchor exists --> try to resolve construct
				 * 
				 * 4. Navigate in given direction
				 * 
				 * 5. add result to current RuntimeEnvironment system variable
				 * %%%___
				 */

				/*
				 * peek set from stack and read @_ and set as anchor
				 */
				anchor = runtime.getRuntimeContext().peek().getValue(
						VariableNames.CURRENT_TUPLE);
				/*
				 * set axis
				 */
				axis = runtime.getDataBridge().getImplementationOfTMQLAxis(
						runtime, getTokens().get(1));
				/*
				 * set optional if exists
				 */
				if (getTokens().size() == 3) {
					final String optional_ = getTokens().get(2);
					if (optional_.startsWith("$")) {
						optional = (Construct) runtime.getRuntimeContext()
								.peek().getValue(optional_);
					} else {
						try {
							optional = runtime.getDataBridge()
									.getConstructByIdentifier(runtime,
											getTokens().get(2));
						} catch (DataBridgeException e) {
							logger.warn("Cannot find optional type argument "
									+ getTokens().get(0));
							runtime.getRuntimeContext().peek().setValue(
									VariableNames.QUERYMATCHES, new QueryMatches(runtime));
							return;
						}
					}

				}
				/*
				 * set direction
				 */
				forward = getTmqlTokens().get(0).equals(MoveForward.class);
			}

			if (anchor == null) {
				runtime.getRuntimeContext().peek().setValue(
						VariableNames.QUERYMATCHES, new QueryMatches(runtime));
				return;
			}

			if (axis == null) {
				throw new TMQLRuntimeException(
						"Cannot find axis implementation for identifier '"
								+ getTokens().get(1) + "'");
			}

			/*
			 * set topic map to navigation axis
			 */
			axis.setTopicMap(runtime.getTopicMap());
			axis.setEnvironment(runtime.getInitialContext().getEnvironment()
					.getTopicMap());
			if (axis instanceof ITypeHierarchyNavigationAxis) {
				((ITypeHierarchyNavigationAxis) axis).setTransitivity(runtime
						.isActsTransitive());
			}

			/*
			 * create query-matches
			 */
			QueryMatches matches = new QueryMatches(runtime);
			/*
			 * execute navigation by calling navigation API
			 */
			Collection<?> navigationResults;
			if (forward) {
				navigationResults = axis.navigateForward(anchor, optional);
			} else {
				navigationResults = axis.navigateBackward(anchor, optional);
			}
			/*
			 * convert navigation results to tuple-sequence and store
			 */
			matches.convertToTuples(navigationResults);
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.QUERYMATCHES, matches);

		} catch (TMQLRuntimeException ex) {
			throw ex;
		} catch (Exception ex) {
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.QUERYMATCHES, new QueryMatches(runtime));
		}
		/**
		 * log it :)
		 */
		Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
		logger.info("Finishing! Results: "
				+ runtime.getRuntimeContext().peek().getValue(
						VariableNames.QUERYMATCHES));
	}
}
