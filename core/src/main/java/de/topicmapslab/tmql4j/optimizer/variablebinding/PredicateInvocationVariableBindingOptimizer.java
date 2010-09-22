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
import de.topicmapslab.tmql4j.parser.core.expressions.PredicateInvocation;
import de.topicmapslab.tmql4j.parser.core.expressions.PredicateInvocationRolePlayerExpression;

/**
 * Special implementation of a variable optimizer which is applicable for
 * predicate-invocations. The variable bindings are ordered in a way that
 * unsuccessful bindings will detect as fast as possible.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PredicateInvocationVariableBindingOptimizer extends
		VariableBindingOptimizer<PredicateInvocation> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL runtime
	 */
	public PredicateInvocationVariableBindingOptimizer(TMQLRuntime runtime) {
		super(runtime);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Set<?> optimize(PredicateInvocation expression, String variable)
			throws TMQLOptimizationException {

		try {

			Set<Object> bindings = HashUtil.getHashSet();

			/*
			 * extract association type
			 */
			final String associationType = expression.getTokens().get(0);

			/*
			 * iterate over contained role-player-constraints
			 */
			for (PredicateInvocationRolePlayerExpression predicateInvocationRolePlayerExpression : expression
					.getExpressionFilteredByType(PredicateInvocationRolePlayerExpression.class)) {
				/*
				 * check if role-player-constraint is not ellipsis and if the
				 * variable is contained
				 */
				if (predicateInvocationRolePlayerExpression.getGrammarType() == PredicateInvocationRolePlayerExpression.TYPE_ROLE_PLAYER_COMBINATION
						&& predicateInvocationRolePlayerExpression
								.getVariables().contains(variable)) {
					/*
					 * call optimizer to optimize variable binding
					 */
					bindings
							.addAll(new PredicateInvocationRolePlayerExpressionVariableBindingOptimizer(
									getRuntime(), associationType).optimize(
									predicateInvocationRolePlayerExpression,
									variable));
				}
			}

			return bindings;
		} catch (TMQLRuntimeException e) {
			throw new TMQLOptimizationException(e);
		}

	}

}
