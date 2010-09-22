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

package de.topicmapslab.tmql4j.optimizer.iteration;

import de.topicmapslab.tmql4j.common.core.exception.TMQLOptimizationException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.optimizer.model.IRuntimeOptimizer;
import de.topicmapslab.tmql4j.optimizer.model.RuntimeOptimizer;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Special runtime optimizer to check if a special variable binding can be
 * ignored during the iteration process.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 * @param <T>
 *            the type of supported expressions
 */
public abstract class IterationOptimizer<T extends IExpression>
		extends RuntimeOptimizer<T> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL runtime
	 */
	public IterationOptimizer(TMQLRuntime runtime) {
		super(runtime);
	}

	/**
	 * Method tries to optimize the variable binding of the given expression
	 * represented by the interpreter argument. The only necessary arguments
	 * will be the name of the variable and the bindings to check. <br />
	 * <br /> {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Object optimize(IExpressionInterpreter interpreter,
			Object... objects) throws TMQLOptimizationException {
		/*
		 * check if arguments are empty
		 */
		if (objects == null || objects.length != 2) {
			throw new TMQLOptimizationException(
					"objects cannot be null or less than 2.");
		}
		/*
		 * check type of the first argument
		 */
		Object object = objects[0];
		if (!(object instanceof String)) {
			throw new TMQLOptimizationException(
					"first argument has to be an instance of String.");
		}
		/*
		 * check if optimizer is applicable for given expression
		 */
		IExpression expression = interpreter.getExpression();
		if (!(applicableFor().isInstance(expression))) {
			throw new TMQLOptimizationException(
					"Expression type is not supported by optimizer.");
		}
		/*
		 * call optimizer
		 */
		return ignoreIteration((T) expression, (String) object, objects[1]);
	}

	/**
	 * Special optimization method, which will be called by the
	 * {@link IRuntimeOptimizer#optimize(IExpressionInterpreter, Object...)}
	 * method after extracting and transforming the given arguments.
	 * 
	 * Method tries to optimize the variable binding of the given expression
	 * represented by the interpreter argument. The only necessary arguments
	 * will be the name of the variable and the bindings to check.
	 * 
	 * @param expression
	 *            the expression to optimize
	 * @param variable
	 *            the variable to optimize
	 * @param binding
	 *            the binding to check
	 * @return <code>true</code> if the binding can be ignored,
	 *         <code>false</code> otherwise.
	 * @throws TMQLOptimizationException
	 *             thrown if optimization fails
	 */
	protected abstract boolean ignoreIteration(final T expression,
			final String variable, final Object binding)
			throws TMQLOptimizationException;

}
