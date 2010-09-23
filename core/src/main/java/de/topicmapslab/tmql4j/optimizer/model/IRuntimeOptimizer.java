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
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;

/**
 * Extension of the base {@link IQueryOptimizer} interface definition to model
 * an optimizer which try to optimize the execution during the querying process.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IRuntimeOptimizer extends IQueryOptimizer {

	/**
	 * Optimize the execution of the given query. Some implementation can reduce
	 * the iterations removing unsuccessful bindings known because of other
	 * executions.
	 * 
	 * @param interpreter
	 *            the interpreter to optimizer
	 * @param objects
	 *            a list of arguments used to optimize
	 * @return the optimization result
	 * @throws TMQLOptimizationException
	 *             thrown if optimization fails
	 */
	public Object optimize(IExpressionInterpreter<?> interpreter,
			Object... objects) throws TMQLOptimizationException;

	/**
	 * Method returns the internal reference of the current TMQL runtime.
	 * 
	 * @return the reference of the TMQL runtime
	 */
	public TMQLRuntime getRuntime();

}
