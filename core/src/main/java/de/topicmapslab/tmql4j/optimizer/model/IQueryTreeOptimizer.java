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
package de.topicmapslab.tmql4j.optimizer.model;

import de.topicmapslab.tmql4j.common.core.exception.TMQLOptimizationException;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Extension of the base {@link IQueryOptimizer} interface definition to model
 * an optimizer which reorders a sub-tree of the generated parsing tree. A tree
 * optimizer reorder for example the boolean-expressions used within a
 * conjunction or disjunction by their dependencies.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 * @param <T>
 *            the expression type, the optimizer is adaptive for
 */
public interface IQueryTreeOptimizer<T extends IExpression> extends
		IQueryOptimizer {

	/**
	 * Optimize the given expression by reordering the sub-tree of the
	 * parser-tree using the given expression as new root node.
	 * 
	 * @param expression
	 *            the expression to optimize
	 * @return the optimized expression
	 * @throws TMQLOptimizationException
	 *             thrown if optimization fails
	 */
	public T optimize(T expression) throws TMQLOptimizationException;

}
