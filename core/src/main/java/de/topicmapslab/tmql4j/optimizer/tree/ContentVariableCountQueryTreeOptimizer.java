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
import de.topicmapslab.tmql4j.parser.core.expressions.Content;
import de.topicmapslab.tmql4j.parser.core.expressions.QueryExpression;

/**
 * Special implementation of query tree optimizer for content implement optimization using
 * the number of contained variables. Optimize the sub-tree of the parser-tree
 * by reordering ascending the expressions by their variable count.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ContentVariableCountQueryTreeOptimizer
		extends VariableCountQueryTreeOptimizer<Content> {

	/**
	 * {@inheritDoc}
	 */
	public Content optimize(Content expression) throws TMQLOptimizationException {
		/*
		 * do not reorder itself -> redirect to sub-expressions
		 */
		try {
			/*
			 * switch by grammar type 
			 */
			switch (expression.getGrammarType()) {
			/*
			 * is content combination
			 */
			case Content.TYPE_SET_OPERATION: {
				/*
				 * iterate over sub-expressions
				 */
				for (Content content : expression
						.getExpressionFilteredByType(Content.class)) {
					/*
					 * call optimizer to optimize sub-expression
					 */
					new ContentVariableCountQueryTreeOptimizer()
							.optimize(content);
				}
			}
				break;
				/*
				 * is query-expression
				 */
			case Content.TYPE_QUERY_EXPRESSION: {
				/*
				 * call optimizer to optimize sub-expression
				 */
				new QueryExpressionVariableCountQueryTreeOptimizer()
						.optimize(expression.getExpressionFilteredByType(
								QueryExpression.class).get(0));

			}
				break;
			}
		} catch (TMQLRuntimeException e) {
			new TMQLOptimizationException(e);
		}

		return expression;
	}

}
