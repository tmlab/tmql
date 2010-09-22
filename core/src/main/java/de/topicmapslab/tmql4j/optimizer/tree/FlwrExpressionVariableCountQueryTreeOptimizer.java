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

import java.util.List;

import de.topicmapslab.tmql4j.common.core.exception.TMQLOptimizationException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.parser.core.expressions.FlwrExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.WhereClause;

/**
 * Special implementation of query tree optimizer for flwr-expressions implement optimization using
 * the number of contained variables. Optimize the sub-tree of the parser-tree
 * by reordering ascending the expressions by their variable count.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class FlwrExpressionVariableCountQueryTreeOptimizer
		extends VariableCountQueryTreeOptimizer<FlwrExpression> {

	/**
	 * {@inheritDoc}
	 */
	public FlwrExpression optimize(FlwrExpression expression)
			throws TMQLOptimizationException {
		/*
		 * do not reorder itself -> redirect to where-clause
		 */
		try {
			/*
			 * check if where-clause is contained
			 */
			List<WhereClause> whereClauses = expression
					.getExpressionFilteredByType(WhereClause.class);
			if (!whereClauses.isEmpty()) {
				/*
				 * call optimizer to optimize sub-expression
				 */
				new WhereClauseVariableCountQueryTreeOptimizer()
						.optimize(whereClauses.get(0));
			}
		} catch (TMQLRuntimeException e) {
			new TMQLOptimizationException(e);
		}

		return expression;
	}

}
