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
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.SelectExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.model.context.IVariableSet;
import de.topicmapslab.tmql4j.optimizer.model.IRuntimeOptimizer;
import de.topicmapslab.tmql4j.parser.core.expressions.SelectExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.WhereClause;

/**
 * Special implementation of a iteration optimizer which is applicable for
 * boolean-primitives.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SelectExpressionIterationOptimizer extends
		IterationOptimizer<SelectExpression> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL runtime
	 */
	public SelectExpressionIterationOptimizer(TMQLRuntime runtime) {
		super(runtime);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean ignoreIteration(SelectExpression expression,
			String variable, final Object binding)
			throws TMQLOptimizationException {
		try {
			/*
			 * call optimizer to check if binding can be ignored
			 */
			return new WhereClauseIterationOptimizer(getRuntime())
					.ignoreIteration(expression.getExpressionFilteredByType(
							WhereClause.class).get(0), variable, binding);
		} catch (TMQLRuntimeException e) {
			throw new TMQLOptimizationException(e);
		}
	}

	/**
	 * Special optimization method, which will be called by the
	 * {@link IRuntimeOptimizer#optimize(IExpressionInterpreter, Object...)}
	 * method after extracting and transforming the given arguments.
	 * 
	 * Method tries to optimize the variable binding of the given expression
	 * represented by the interpreter argument.
	 * 
	 * @param runtime
	 *            the TMQL runtime
	 * @param interpreter
	 *            the interpreter to check
	 * @return <code>true</code> if at least one binding results in empty
	 *         result, <code>false</code> otherwise
	 * @throws TMQLOptimizationException
	 *             thrown if optimization fails
	 */
	public static final boolean ignoreIteration(TMQLRuntime runtime,
			SelectExpressionInterpreter interpreter)
			throws TMQLOptimizationException {
		try {
			IVariableSet set = runtime.getRuntimeContext().peek();
			for (String variable : set.getVariables()) {
				if (!runtime.isSystemVariable(variable)) {
					if ((Boolean) new SelectExpressionIterationOptimizer(
							runtime).optimize(interpreter, variable, set
							.getValue(variable))) {
						return true;
					}
				}
			}
			return false;
		} catch (TMQLRuntimeException e) {
			throw new TMQLOptimizationException(e);
		}
	}

}
