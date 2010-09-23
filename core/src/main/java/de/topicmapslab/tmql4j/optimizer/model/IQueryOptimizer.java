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

import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Interface definition of a query optimizer of the TMQL4J engine. A optimizer
 * try to optimize a short processing step during the querying process, like the
 * variable-binding, or the reordering or boolean-expression.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IQueryOptimizer {

	/**
	 * Method returns a list of expression the optimizer is applicable for.
	 * 
	 * @return the list of applicable expressions
	 */
	public Class<? extends IExpression> applicableFor();
}
