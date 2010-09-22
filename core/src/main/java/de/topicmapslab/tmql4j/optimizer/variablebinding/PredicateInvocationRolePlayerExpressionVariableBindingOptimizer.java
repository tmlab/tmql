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

import java.util.Set;

import de.topicmapslab.tmql4j.common.core.exception.TMQLOptimizationException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.optimizer.util.TMQLOptimizerUtility;
import de.topicmapslab.tmql4j.parser.core.expressions.PredicateInvocationRolePlayerExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.ValueExpression;

/**
 * Special implementation of a variable optimizer which is applicable for
 * predicate-invocations-role-player-expressions. The variable bindings are
 * ordered in a way that unsuccessful bindings will detect as fast as possible.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PredicateInvocationRolePlayerExpressionVariableBindingOptimizer
		extends
		VariableBindingOptimizer<PredicateInvocationRolePlayerExpression> {

	/**
	 * string representation of the association type
	 */
	private final String associationType;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL runtime
	 * @param associationType
	 *            the string representation of the association type
	 */
	public PredicateInvocationRolePlayerExpressionVariableBindingOptimizer(
			TMQLRuntime runtime, final String associationType) {
		super(runtime);
		this.associationType = associationType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Set<?> optimize(
			PredicateInvocationRolePlayerExpression expression, String variable)
			throws TMQLOptimizationException {

		try {
			/*
			 * extract value expression representing the role-player
			 */
			ValueExpression valueExpression = expression
					.getExpressionFilteredByType(ValueExpression.class).get(0);
			/*
			 * extract string representation of the role-type
			 */
			final String anchor = expression.getTokens().get(0);
			Set<Object> bindings = HashUtil.getHashSet();
			/*
			 * check if player is simple expression
			 */
			if (valueExpression.getTokens().size() == 1) {
				/*
				 * execute TMQL query to extract possible players of detected
				 * role-type
				 */
				bindings.addAll(new TMQLOptimizerUtility(getRuntime()).execute(
						anchor + " << roles " + associationType
								+ " >> players " + anchor)
						.getPossibleValuesForVariable());
			}
			return bindings;
		} catch (TMQLRuntimeException e) {
			throw new TMQLOptimizationException(e);
		}
	}

}
