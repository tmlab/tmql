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

import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Base implemenation of a {@link IRuntimeOptimizer}. A runtime optimizer try to
 * optimize the execution during the querying process.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 * @param <T>
 *            type of supported expressions
 */
public abstract class RuntimeOptimizer<T extends IExpression>
		extends QueryOptimizer<T> implements IRuntimeOptimizer {

	/**
	 * internal reference of the runtime
	 */
	private TMQLRuntime runtime;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL runtime
	 */
	public RuntimeOptimizer(final TMQLRuntime runtime) {
		this.runtime = runtime;
	}

	/**
	 * {@inheritDoc}
	 */
	public TMQLRuntime getRuntime() {
		return runtime;
	}
}
