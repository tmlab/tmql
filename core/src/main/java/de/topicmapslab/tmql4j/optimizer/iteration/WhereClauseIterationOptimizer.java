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
package de.topicmapslab.tmql4j.optimizer.iteration;

import de.topicmapslab.tmql4j.common.core.exception.TMQLOptimizationException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.parser.core.expressions.BooleanExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.WhereClause;

/**
 * Special implementation of a iteration optimizer which is applicable for
 * boolean-primitives.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class WhereClauseIterationOptimizer
		extends IterationOptimizer<WhereClause> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL runtime
	 */
	public WhereClauseIterationOptimizer(TMQLRuntime runtime) {
		super(runtime);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean ignoreIteration(WhereClause expression, String variable, final Object binding)
			throws TMQLOptimizationException {
		try {
			/*
			 * call optimizer to check if binding can be ignored
			 */
			return new BooleanExpressionIterationOptimizer(getRuntime())
					.ignoreIteration(expression.getExpressionFilteredByType(
							BooleanExpression.class).get(0), variable, binding);
		} catch (TMQLRuntimeException e) {
			throw new TMQLOptimizationException(e);
		}

	}

}
