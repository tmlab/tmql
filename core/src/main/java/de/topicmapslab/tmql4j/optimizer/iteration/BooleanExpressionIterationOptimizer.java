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
import de.topicmapslab.tmql4j.parser.core.expressions.BooleanPrimitive;

/**
 * Special implementation of a iteration optimizer which is applicable for
 * boolean-expressions.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class BooleanExpressionIterationOptimizer
		extends IterationOptimizer<BooleanExpression> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL runtime
	 */
	public BooleanExpressionIterationOptimizer(TMQLRuntime runtime) {
		super(runtime);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean ignoreIteration(BooleanExpression expression,
			String variable, final Object binding) throws TMQLOptimizationException {
		try {
			/*
			 * switch by grammar type
			 */
			switch (expression.getGrammarType()) {
			/*
			 * is boolean primitve
			 */
			case BooleanExpression.TYPE_BOOLEAN_PRIMITIVE: {
				/*
				 * call optimizer to check if iteration can be ignored
				 */
				return new BooleanPrimitiveIterationOptimizer(getRuntime())
						.ignoreIteration(expression
								.getExpressionFilteredByType(
										BooleanPrimitive.class).get(0),
								variable, binding);
			}
				/*
				 * is boolean conjunction
				 */
			case BooleanExpression.TYPE_CONJUNCTION: {
				for (BooleanExpression booleanExpression : expression
						.getExpressionFilteredByType(BooleanExpression.class)) {
					/*
					 * if one boolean-expression is independent from the
					 * variable, it is not necessary to check it
					 */
					if (!booleanExpression.getVariables().contains(variable)) {
						continue;
					}
					/*
					 * call optimizer to check if iteration can be ignored
					 */
					if (new BooleanExpressionIterationOptimizer(getRuntime())
							.ignoreIteration(booleanExpression, variable,
									binding)) {
						return true;
					}
				}
			}
				break;
			/*
			 * is boolean disjunction
			 */
			case BooleanExpression.TYPE_DISJUNCTION: {
				for (BooleanExpression booleanExpression : expression
						.getExpressionFilteredByType(BooleanExpression.class)) {
					/*
					 * if one boolean-expression is independent from the
					 * variable, it is not possible to known the result
					 */
					if (!booleanExpression.getVariables().contains(variable)) {
						return false;
					}
					/*
					 * call optimizer to check if iteration can be ignored
					 */
					if (!(new BooleanExpressionIterationOptimizer(getRuntime())
							.ignoreIteration(booleanExpression, variable,
									binding))) {
						return false;
					}
				}
				return true;
			}
			}
		} catch (TMQLRuntimeException e) {
			throw new TMQLOptimizationException(e);
		}
		return false;
	}

}
