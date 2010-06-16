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

import java.util.List;
import java.util.Set;

import de.topicmapslab.tmql4j.common.core.exception.TMQLOptimizationException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.parser.core.expressions.BooleanExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.FlwrExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.WhereClause;

/**
 * Special implementation of a variable optimizer which is applicable for
 * flwr-expressions. The variable bindings are ordered in a way that
 * unsuccessful bindings will detect as fast as possible.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class FlwrExpressionVariableBindingOptimizer extends
		VariableBindingOptimizer<FlwrExpression> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL runtime
	 */
	public FlwrExpressionVariableBindingOptimizer(TMQLRuntime runtime) {
		super(runtime);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Set<?> optimize(FlwrExpression expression, String variable)
			throws TMQLOptimizationException {
		try {
			/*
			 * check if flwr-expression contains a where-clause
			 */
			List<WhereClause> whereClauses = expression
					.getExpressionFilteredByType(WhereClause.class);

			if (whereClauses.isEmpty()) {
				return HashUtil.getHashSet();
			}

			/*
			 * check if current variable is contained by where-clause
			 */
			WhereClause whereClause = whereClauses.get(0);
			if (!whereClause.getVariables().contains(variable)) {
				return HashUtil.getHashSet();
			}

			/*
			 * extract top-level boolean-expression of the contained
			 * where-clause
			 */
			BooleanExpression booleanExpression = whereClause
					.getExpressionFilteredByType(BooleanExpression.class)
					.get(0);
			/*
			 * optimize variable binding by calling optimizer
			 */
			return new BooleanExpressionVariableBindingOptimizer(getRuntime())
					.optimize(booleanExpression, variable);
		} catch (TMQLRuntimeException ex) {
			throw new TMQLOptimizationException("Internal error", ex);
		}
	}

}
