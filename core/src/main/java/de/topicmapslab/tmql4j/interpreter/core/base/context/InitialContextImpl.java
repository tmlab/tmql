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
package de.topicmapslab.tmql4j.interpreter.core.base.context;

import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.interpreter.core.predefinition.Environment;
import de.topicmapslab.tmql4j.interpreter.core.predefinition.SystemVariableSet;
import de.topicmapslab.tmql4j.interpreter.model.context.IInitialContext;
import de.topicmapslab.tmql4j.interpreter.model.context.IVariableSet;

/**
 * Base implementation of {@link IInitialContext}.
 * <p>
 * The initial context provides information about the system-variables states at
 * the begin of querying process and about the predefined environment.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class InitialContextImpl implements IInitialContext {

	/**
	 * the system variables defined by the current TMQL draft
	 */
	private final SystemVariableSet systemVariableSet;
	/**
	 * the topic map system which is queried
	 */
	private final TopicMapSystem system;
	/**
	 * a topic maps to query
	 */
	private final TopicMap topicmap;
	/**
	 * the environment map specified by the current draft
	 */
	private final Environment environment;

	/**
	 * Constructor
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param system
	 *            the {@link TopicMapSystem} to queried
	 * @param topicmap
	 *            the {@link TopicMap} to queried
	 * @throws TMQLRuntimeException
	 *             thrown if initialization failed
	 */
	public InitialContextImpl(final ITMQLRuntime runtime,
			TopicMapSystem system, TopicMap topicmap)
			throws TMQLRuntimeException {
		this.system = system;
		this.topicmap = topicmap;
		this.systemVariableSet = new SystemVariableSet(runtime, this);
		this.environment = runtime.getEnvironment();
	}

	/**
	 * {@inheritDoc}
	 */
	public IVariableSet getPredefinedVariableSet() {
		return this.systemVariableSet;
	}

	/**
	 * {@inheritDoc}
	 */
	public TopicMapSystem getQueriedTopicMapSystem() {
		return this.system;
	}

	/**
	 * {@inheritDoc}
	 */
	public TopicMap getQueriedTopicMap() {
		return this.topicmap;
	}

	/**
	 * {@inheritDoc}
	 */
	public Environment getEnvironment() {
		return this.environment;
	}
}
