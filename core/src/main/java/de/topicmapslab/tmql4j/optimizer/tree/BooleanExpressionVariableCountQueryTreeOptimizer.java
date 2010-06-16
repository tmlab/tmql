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
package de.topicmapslab.tmql4j.optimizer.tree;

import de.topicmapslab.tmql4j.common.core.exception.TMQLOptimizationException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.parser.core.expressions.BooleanExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.BooleanPrimitive;

/**
 * Special implementation of query tree optimizer for boolean-expression
 * implement optimization using the number of contained variables. Optimize the
 * sub-tree of the parser-tree by reordering ascending the expressions by their
 * variable count.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class BooleanExpressionVariableCountQueryTreeOptimizer
		extends VariableCountQueryTreeOptimizer<BooleanExpression> {

	/**
	 * {@inheritDoc}
	 */
	public BooleanExpression optimize(BooleanExpression expression)
			throws TMQLOptimizationException {

		/*
		 * optimize parser-tree according the number of contained variables
		 */
		expression = optimizeSubTree(expression);
		try {
			/*
			 * switch by grammar type
			 */
			switch (expression.getGrammarType()) {
			/*
			 * is boolean combination
			 */
			case BooleanExpression.TYPE_CONJUNCTION:
			case BooleanExpression.TYPE_DISJUNCTION: {
				/*
				 * iterate over all boolean-expressions
				 */
				for (BooleanExpression subExpression : expression
						.getExpressionFilteredByType(BooleanExpression.class)) {
					/*
					 * call optimizer to optimize sub-expression
					 */
					new BooleanExpressionVariableCountQueryTreeOptimizer()
							.optimize(subExpression);
				}
			}
				break;
			case BooleanExpression.TYPE_BOOLEAN_PRIMITIVE: {
				/*
				 * iterate over all boolean-primitives
				 */
				for (BooleanPrimitive subExpression : expression
						.getExpressionFilteredByType(BooleanPrimitive.class)) {
					/*
					 * call optimizer to optimize sub-expression
					 */
					new BooleanPrimitiveVariableCountQueryTreeOptimizer()
							.optimize(subExpression);
				}
			}
				break;
			}
		} catch (TMQLRuntimeException e) {
			new TMQLOptimizationException(e);
		}

		return expression;
	}

}
