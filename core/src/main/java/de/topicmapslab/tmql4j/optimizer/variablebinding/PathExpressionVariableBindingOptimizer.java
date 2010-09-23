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
package de.topicmapslab.tmql4j.optimizer.variablebinding;

import java.util.Collection;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.Construct;
import org.tmapi.core.Topic;
import org.tmapi.index.TypeInstanceIndex;

import de.topicmapslab.tmql4j.api.exceptions.DataBridgeException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLOptimizationException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.navigation.axis.SupertypesNavigationAxis;
import de.topicmapslab.tmql4j.navigation.exception.NavigationException;
import de.topicmapslab.tmql4j.parser.core.expressions.AKOExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.ISAExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.PathExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.PredicateInvocation;
import de.topicmapslab.tmql4j.parser.core.expressions.SimpleContent;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Special implementation of a variable optimizer which is applicable for
 * path-expressions. The variable bindings are ordered in a way that
 * unsuccessful bindings will detect as fast as possible.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PathExpressionVariableBindingOptimizer extends
		VariableBindingOptimizer<PathExpression> {

	/**
	 * the Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL runtime
	 */
	public PathExpressionVariableBindingOptimizer(TMQLRuntime runtime) {
		super(runtime);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Set<?> optimize(PathExpression expression, String variable)
			throws TMQLOptimizationException {
		try {
			/*
			 * extract first contained expression
			 */
			IExpression subExpression = expression.getExpressions().get(0);
			/*
			 * switch by grammar type
			 */
			switch (expression.getGrammarType()) {
			/*
			 * is a-kind-of-expression
			 */
			case PathExpression.TYPE_AKO_EXPRESSION: {
				/*
				 * interpret expression to extract possible bindings
				 */
				AKOExpression akoExpression = (AKOExpression) subExpression;
				SimpleContent simpleContent1 = akoExpression
						.getExpressionFilteredByType(SimpleContent.class)
						.get(0);
				SimpleContent simpleContent2 = akoExpression
						.getExpressionFilteredByType(SimpleContent.class)
						.get(1);
				/*
				 * check if a-kind-of-expression only contains TMQL elements
				 */
				if (simpleContent1.getTokens().size() == 1
						&& simpleContent2.getTokens().size() == 1) {
					/*
					 * get type construct
					 */
					Construct construct = null;
					try {
						construct = getRuntime().getDataBridge()
								.getConstructByIdentifier(getRuntime(),
										simpleContent2.getTokens().get(0));
					} catch (DataBridgeException e) {
						logger.warn("Cannot find type by identifier "
								+ simpleContent2.getTokens().get(0));
					}
					/*
					 * check if construct is a topic
					 */
					if (construct instanceof Topic) {
						Set<Topic> bindings = HashUtil.getHashSet();
						try {
							bindings
									.addAll((Collection<Topic>) new SupertypesNavigationAxis()
											.navigateForward(construct));
						} catch (NavigationException e) {
							throw new TMQLOptimizationException(e);
						}
						return bindings;
					}
				}
				return getMaximumBindings();
			}
				/*
				 * is instance-of-expression
				 */
			case PathExpression.TYPE_ISA_EXPRESSION: {
				/*
				 * interpret expression to extract possible bindings
				 */
				ISAExpression isaExpression = (ISAExpression) subExpression;
				SimpleContent simpleContent1 = isaExpression
						.getExpressionFilteredByType(SimpleContent.class)
						.get(0);
				SimpleContent simpleContent2 = isaExpression
						.getExpressionFilteredByType(SimpleContent.class)
						.get(1);
				/*
				 * check if instance-of-expression only contains TMQL elements
				 */
				if (simpleContent1.getTokens().size() == 1
						&& simpleContent2.getTokens().size() == 1) {
					/*
					 * get type construct
					 */
					Construct construct = null;
					try {
						construct = getRuntime().getDataBridge()
								.getConstructByIdentifier(getRuntime(),
										simpleContent2.getTokens().get(0));
					} catch (DataBridgeException e) {
						logger.warn("Cannot find type by identifier "
								+ simpleContent2.getTokens().get(0));
					}
					/*
					 * check if construct is a topic
					 */
					if (construct instanceof Topic) {
						TypeInstanceIndex index = construct.getTopicMap()
								.getIndex(TypeInstanceIndex.class);
						Set<Topic> bindings = HashUtil.getHashSet();
						bindings.addAll(index.getTopics((Topic) construct));
						return bindings;
					}
				}
				return getMaximumBindings();
			}
				/*
				 * is predicate invocation
				 */
			case PathExpression.TYPE_PREDICATE_INVOCATION: {
				/*
				 * call optimizer to optimize variable binding
				 */
				return new PredicateInvocationVariableBindingOptimizer(
						getRuntime()).optimize(
						(PredicateInvocation) subExpression, variable);
			}
			default:
				return getMaximumBindings();
			}
		} catch (TMQLRuntimeException e) {
			throw new TMQLOptimizationException(e);
		}
	}
}
