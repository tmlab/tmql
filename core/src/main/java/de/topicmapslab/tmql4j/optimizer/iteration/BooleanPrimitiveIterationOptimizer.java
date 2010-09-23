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

import java.util.Map;

import de.topicmapslab.tmql4j.common.core.exception.TMQLOptimizationException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.optimizer.cache.UnsuccesfulBindingsCache;
import de.topicmapslab.tmql4j.parser.core.expressions.BooleanExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.BooleanPrimitive;

/**
 * Special implementation of a iteration optimizer which is applicable for
 * boolean-primitives.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class BooleanPrimitiveIterationOptimizer extends
		IterationOptimizer<BooleanPrimitive> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL runtime
	 */
	public BooleanPrimitiveIterationOptimizer(TMQLRuntime runtime) {
		super(runtime);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean ignoreIteration(BooleanPrimitive expression,
			String variable, final Object binding)
			throws TMQLOptimizationException {
		try {
			switch (expression.getGrammarType()) {
			/*
			 * is exists-clause
			 */
			case BooleanPrimitive.TYPE_EXISTS_CLAUSE: {
				/*
				 * check if expression only contains the variable to check
				 */
				if (expression.getVariables().size() == 1
						&& expression.getVariables().get(0).equalsIgnoreCase(
								variable)) {
					/*
					 * get cache instance
					 */
					UnsuccesfulBindingsCache cache = UnsuccesfulBindingsCache
							.getUnsuccesfulBindingsCache();
					/*
					 * create binding
					 */
					Map<String, Object> bindings = HashUtil.getHashMap();
					bindings.put(variable, getRuntime().getRuntimeContext()
							.peek().getValue(variable));
					/*
					 * check if binding is known as unsuccessful
					 */
					return cache.isCached(expression, bindings);
				}
			}
				break;
			/*
			 * is not-expression
			 */
			case BooleanPrimitive.TYPE_NOT_EXPRESSION: {
				/*
				 * call optimizer to check if binding can be ignored
				 */
				return !new BooleanPrimitiveIterationOptimizer(getRuntime())
						.ignoreIteration(expression
								.getExpressionFilteredByType(
										BooleanPrimitive.class).get(0),
								variable, binding);
			}
				/*
				 * is boolean-expression
				 */
			case BooleanPrimitive.TYPE_BOOLEAN_EXPRESSION: {
				/*
				 * call optimizer to check if binding can be ignored
				 */
				return new BooleanExpressionIterationOptimizer(getRuntime())
						.ignoreIteration(expression
								.getExpressionFilteredByType(
										BooleanExpression.class).get(0),
								variable, binding);
			}
			}

		} catch (TMQLRuntimeException e) {
			throw new TMQLOptimizationException(e);
		}
		return false;
	}
}
