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

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import de.topicmapslab.tmql4j.common.core.exception.TMQLOptimizationException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.optimizer.model.QueryTreeOptimizer;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Implementation of Query-Tree-Optimization. Optimize the sub-tree of the
 * parser-tree by reordering ascending the expressions by their variable count.
 * 
 * @author Sven Krosse
 * 
 * @param <T>
 *            the supported expression type
 */
public abstract class VariableCountQueryTreeOptimizer<T extends IExpression>
		extends QueryTreeOptimizer<T> {

	/**
	 * Iteration step of optimization one sub-tree
	 * 
	 * @param expression
	 *            the sub-tree-root-node
	 * @return the modified sub-tree-root-node
	 * @throws TMQLOptimizationException
	 */
	protected final T optimizeSubTree(T expression)
			throws TMQLOptimizationException {
		/*
		 * Add sub-tree to new list
		 */
		List<IExpression> expressions = new LinkedList<IExpression>();
		expressions.addAll(expression.getExpressions());
		/*
		 * sort by variable count
		 */
		Collections.sort(expressions, new VariableCountComparator());
		try {
			/*
			 * set new order
			 */
			expression.setExpressions(expressions);
		} catch (TMQLRuntimeException e) {
			throw new TMQLOptimizationException(e);
		}
		/*
		 * finish iteration
		 */
		return expression;
	}

	/**
	 * Comparator for parser tree expressions.
	 * 
	 * @author Sven Krosse
	 * 
	 */
	class VariableCountComparator implements Comparator<IExpression> {
		/**
		 * Comparison method
		 * 
		 * @param o1
		 *            first expression
		 * @param o2
		 *            second expression
		 * @return a negative integer if variable count of first expression is
		 *         lower than the variable count of second expression, zero if
		 *         the variable count of both expression is equal or a positive
		 *         integer if the variable count of the first expression is
		 *         higher than the variable count of second expression.
		 */
		public int compare(IExpression o1, IExpression o2) {
			return o1.getVariables().size() - o2.getVariables().size();
		}
	}

}
