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
package de.topicmapslab.tmql4j.optimizer.variablebinding;

import java.util.Set;

import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.common.core.exception.TMQLOptimizationException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.optimizer.model.IRuntimeOptimizer;
import de.topicmapslab.tmql4j.optimizer.model.RuntimeOptimizer;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Special runtime optimizer class to optimize the variable binding to save
 * execution time. The variable bindings are ordered in a way that unsuccessful
 * bindings will detect as fast as possible.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 * @param <T>
 *            the type of supported expressions
 */
public abstract class VariableBindingOptimizer<T extends IExpression> extends
		RuntimeOptimizer<T> {

	/**
	 * a set containing all topics of the topic map
	 */
	private final Set<Topic> topics;
	/**
	 * the topic map to query
	 */
	private final TopicMap topicMap;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL runtime
	 */
	public VariableBindingOptimizer(TMQLRuntime runtime) {
		super(runtime);
		topics = HashUtil.getHashSet();

		/*
		 * try to load current topic map from stack
		 */
		Object o = null;
		try {
			o = runtime.getRuntimeContext().peek().getValue(
					VariableNames.CURRENT_MAP);
		} catch (TMQLRuntimeException e) {
			e.printStackTrace();
		}
		/*
		 * try to load topic map from runtime
		 */
		if (o == null || !(o instanceof TopicMap)) {
			topicMap = runtime.getTopicMap();
		}
		/*
		 * save loaded topic map
		 */
		else {
			topicMap = (TopicMap) o;
		}

		/*
		 * get all topics
		 */
		topics.addAll(topicMap.getTopics());
	}

	/**
	 * Method tries to optimize the variable binding of the given expression
	 * represented by the interpreter argument. The only necessary argument will
	 * be the name of the variable to optimize. The binding will be optimized by
	 * their binding count and number of dependencies. <br />
	 * <br /> {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Object optimize(IExpressionInterpreter interpreter,
			Object... objects) throws TMQLOptimizationException {

		/*
		 * check if arguments are empty
		 */
		if (objects == null || objects.length == 0) {
			throw new TMQLOptimizationException(
					"objects cannot be null or empty.");
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
		return optimize((T) expression, (String) object);
	}

	/**
	 * Method return the maximum binding of a variable. The method currently
	 * return all topics of the topic map.
	 * 
	 * @return all possible bindings
	 */
	public Set<Topic> getMaximumBindings() {
		return topics;
	}

	/**
	 * Special optimization method, which will be called by the
	 * {@link IRuntimeOptimizer#optimize(IExpressionInterpreter, Object...)}
	 * method after extracting and transforming the given arguments.
	 * 
	 * Method tries to optimize the variable binding of the given expression
	 * represented by the interpreter argument. The only necessary argument will
	 * be the name of the variable to optimize. The binding will be optimized by
	 * their binding count and number of dependencies.
	 * 
	 * @param expression
	 *            the expression to optimize
	 * @param variable
	 *            the variable to optimize
	 * @return the optimized variable binding
	 * @throws TMQLOptimizationException
	 *             thrown if optimization fails
	 */
	protected abstract Set<?> optimize(T expression, String variable)
			throws TMQLOptimizationException;

	/**
	 * Method return the internal reference of the topic map.
	 * 
	 * @return the topic map
	 */
	public TopicMap getTopicMap() {
		return topicMap;
	}

}
