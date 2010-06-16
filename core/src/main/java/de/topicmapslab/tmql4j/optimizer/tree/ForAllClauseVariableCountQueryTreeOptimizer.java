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
import de.topicmapslab.tmql4j.parser.core.expressions.ForAllClause;

/**
 * Special implementation of query tree optimizer for for-all-clauses implement
 * optimization using the number of contained variables. Optimize the sub-tree
 * of the parser-tree by reordering ascending the expressions by their variable
 * count.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ForAllClauseVariableCountQueryTreeOptimizer
		extends VariableCountQueryTreeOptimizer<ForAllClause> {

	/**
	 * {@inheritDoc}
	 */
	public ForAllClause optimize(ForAllClause expression)
			throws TMQLOptimizationException {
		/*
		 * do not reorder itself -> redirect to contained boolean-expression
		 */
		try {
			/*
			 * call optimizer to optimize sub-expression
			 */
			new BooleanExpressionVariableCountQueryTreeOptimizer()
					.optimize(expression.getExpressionFilteredByType(
							BooleanExpression.class).get(0));

		} catch (TMQLRuntimeException e) {
			new TMQLOptimizationException(e);
		}

		return expression;
	}

}
