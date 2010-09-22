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
import de.topicmapslab.tmql4j.parser.core.expressions.FlwrExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.QueryExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.SelectExpression;

/**
 * Special implementation of query tree optimizer for query-expressions implement optimization using
 * the number of contained variables. Optimize the sub-tree of the parser-tree
 * by reordering ascending the expressions by their variable count.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class QueryExpressionVariableCountQueryTreeOptimizer
		extends VariableCountQueryTreeOptimizer<QueryExpression> {

	/**
	 * {@inheritDoc}
	 */
	public QueryExpression optimize(QueryExpression expression)
			throws TMQLOptimizationException {
		/*
		 * do not reorder itself -> redirect to internal expressions
		 */
		try {
			/*
			 * switch by grammar type
			 */
			switch (expression.getGrammarType()) {
			/*
			 * is flwr-expression
			 */
			case QueryExpression.TYPE_FLWR_EXPRESSION: {
				/*
				 * call optimizer to optimize sub-expression
				 */
				new FlwrExpressionVariableCountQueryTreeOptimizer()
						.optimize(expression.getExpressionFilteredByType(
								FlwrExpression.class).get(0));

			}
				break;
				/*
				 * is select-expression
				 */
			case QueryExpression.TYPE_SELECT_EXPRESSION: {
				/*
				 * call optimizer to optimize sub-expression
				 */
				new SelectExpressionVariableCountQueryTreeOptimizer()
						.optimize(expression.getExpressionFilteredByType(
								SelectExpression.class).get(0));

			}
				break;
			}
		} catch (TMQLRuntimeException e) {
			new TMQLOptimizationException(e);
		}

		return expression;
	}

}
