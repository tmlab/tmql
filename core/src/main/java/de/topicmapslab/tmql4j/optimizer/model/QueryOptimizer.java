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

import java.lang.reflect.ParameterizedType;

import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Base implementation of a {@link IQueryOptimizer}. A optimizer try to optimize
 * a short processing step during the querying process, like the
 * variable-binding, or the reordering or boolean-expression.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 * @param <T>
 *            the type of applicable expressions
 */
public abstract class QueryOptimizer<T extends IExpression> implements
		IQueryOptimizer {

	/**
	 * the type of the supported expressions
	 */
	private final Class<T> clazz;

	/**
	 * base constructor to create a new instance
	 */
	@SuppressWarnings("unchecked")
	public QueryOptimizer() {
		clazz = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends IExpression> applicableFor() {
		return clazz;
	}

}
