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
 * Base implementation of a {@link IQueryTreeOptimizer}. The class only inherit
 * from the {@link QueryOptimizer} class.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 * @param <T>
 *            the type of supported expressions
 */
public abstract class QueryTreeOptimizer<T extends IExpression>
		extends QueryOptimizer<T> implements IQueryTreeOptimizer<T> {
}
