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
import de.topicmapslab.tmql4j.parser.core.expressions.ExistsClause;
import de.topicmapslab.tmql4j.parser.core.expressions.ForAllClause;

/**
 * Special implementation of query tree optimizer for boolean-primitive implement optimization using
 * the number of contained variables. Optimize the sub-tree of the parser-tree
 * by reordering ascending the expressions by their variable count.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class BooleanPrimitiveVariableCountQueryTreeOptimizer
		extends VariableCountQueryTreeOptimizer<BooleanPrimitive> {

	/**
	 * {@inheritDoc}
	 */
	public BooleanPrimitive optimize(BooleanPrimitive expression)
			throws TMQLOptimizationException {

		/*
		 * optimize sub-tree according the number of contained variables
		 */
		expression = optimizeSubTree(expression);
		try {
			/*
			 * switch by grammar type
			 */
			switch (expression.getGrammarType()) {
			/*
			 * is not expression
			 */
			case BooleanPrimitive.TYPE_NOT_EXPRESSION: {
				/*
				 * call optimizer to optimize sub-expression
				 */
				new BooleanPrimitiveVariableCountQueryTreeOptimizer()
						.optimize(expression.getExpressionFilteredByType(
								BooleanPrimitive.class).get(0));

			}
				break;
				/*
				 * is boolean-expression
				 */
			case BooleanPrimitive.TYPE_BOOLEAN_EXPRESSION: {
				/*
				 * call optimizer to optimize sub-expression
				 */
				new BooleanExpressionVariableCountQueryTreeOptimizer()
						.optimize(expression.getExpressionFilteredByType(
								BooleanExpression.class).get(0));

			}
				break;
				/*
				 * is exists-clause
				 */
			case BooleanPrimitive.TYPE_EXISTS_CLAUSE: {
				/*
				 * call optimizer to optimize sub-expression
				 */
				new ExistsClauseVariableCountQueryTreeOptimizer()
						.optimize(expression.getExpressionFilteredByType(
								ExistsClause.class).get(0));

			}
				break;
				/*
				 * is every-clause
				 */
			case BooleanPrimitive.TYPE_EVERY_CLAUSE: {
				/*
				 * call optimizer to optimize sub-expression
				 */
				new ForAllClauseVariableCountQueryTreeOptimizer()
						.optimize(expression.getExpressionFilteredByType(
								ForAllClause.class).get(0));

			}
				break;
			}
		} catch (TMQLRuntimeException e) {
			new TMQLOptimizationException(e);
		}

		return expression;
	}

}
