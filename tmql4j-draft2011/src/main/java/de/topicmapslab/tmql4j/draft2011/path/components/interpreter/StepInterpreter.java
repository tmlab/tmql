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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.NavigationRegistry;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.model.INavigationAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.model.ITypeHierarchyNavigationAxis;
import de.topicmapslab.tmql4j.draft2011.path.exception.NavigationException;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisTypes;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.MoveForward;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisInstances;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.Anchor;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.Step;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

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
	private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

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
	@Override
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		try {

			/*
			 * try to resolve Construct
			 */
			Object anchor = null;
			Object optional = null;

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
				anchor = runtime.getConstructResolver().getConstructByIdentifier(context, getTokens().get(0));
				if (anchor == null) {
					logger.warn("Cannot find optional type argument " + getTokens().get(0));
				}
				/*
				 * set types axis
				 */
				axis = NavigationRegistry.buildHandler().lookup(AxisTypes.class);
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
				anchor = context.getCurrentNode();
				/*
				 * set axis
				 */
				axis = NavigationRegistry.buildHandler().lookup(getTmqlTokens().get(1));
				/*
				 * set optional if exists
				 */
				if (!getExpression().getExpressions().isEmpty()) {
					QueryMatches optionals = extractArguments(runtime, Anchor.class, 0, context, optionalArguments);
					if (optionals.isEmpty()) {
						logger.warn("Cannot find optional type argument " + getTokens().get(2));
						return QueryMatches.emptyMatches();
					}
					optional = optionals.getFirstValue();
					// final String optional_ = getTokens().get(2);
					// if (optional_.startsWith("$")) {
					// if (context.getContextBindings() != null) {
					// List<Object> optionals =
					// context.getContextBindings().getPossibleValuesForVariable(optional_);
					// if (!optionals.isEmpty()) {
					// optional = (Construct) optionals.get(0);
					// }
					// } else if (context.getCurrentTuple() != null ||
					// context.getCurrentTuple().containsKey(optional_)) {
					// optional = (Construct)
					// context.getCurrentTuple().get(optional_);
					// }
					// } else else {
					// optional =
					// runtime.getConstructResolver().getConstructByIdentifier(context,
					// optional_);
					// }
					// /*
					// * optional should not be null if optional argument is
					// used
					// */
					if (optional == null) {
						logger.warn("Cannot find optional type argument " + getTokens().get(0));
						return QueryMatches.emptyMatches();
					}

				}
				/*
				 * set direction
				 */
				forward = getTmqlTokens().get(0).equals(MoveForward.class);
			}

			if (anchor == null) {
				logger.warn("Anchor is missing!");
				return QueryMatches.emptyMatches();
			}
			/*
			 * set topic map to navigation axis
			 */
			axis.setTopicMap(context.getQuery().getTopicMap());
			if (axis instanceof ITypeHierarchyNavigationAxis) {
				((ITypeHierarchyNavigationAxis) axis).setTransitivity(context.isTransitive());
			}
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
			return QueryMatches.asQueryMatchNS(runtime, navigationResults.toArray());
		} catch (TMQLRuntimeException ex) {
			throw ex;
		} catch (NavigationException e) {
			logger.warn("The following navigation error occured!", e);
			return new QueryMatches(runtime);
		} catch (Exception ex) {
			throw new TMQLRuntimeException("An error occured during runtime!", ex);
		}
	}
}
