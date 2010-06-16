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
package de.topicmapslab.tmql4j.preprocessing.base;

import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.process.ProcessingTaskImpl;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.model.process.IInitializationTask;
import de.topicmapslab.tmql4j.interpreter.core.base.context.InitialContextImpl;
import de.topicmapslab.tmql4j.interpreter.model.context.IInitialContext;

/**
 * Base implementation of {@link IInitializationTask}. During the initialization
 * the environment map will be created and the initial context will be
 * instantiated.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class InitializationTask extends ProcessingTaskImpl implements
		IInitializationTask {

	/**
	 * the initial context
	 */
	private IInitialContext context;
	/**
	 * the topic map system
	 */
	private final TopicMapSystem system;
	/**
	 * a collection of topic maps which shall be queried
	 */
	private final TopicMap topicmap;

	/**
	 * the TMQL4J runtime
	 */
	private final TMQLRuntime runtime;

	/**
	 * Base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param system
	 *            the topic map system used to interpret CTM and XTM content
	 * @param topicmap
	 *            a topic maps which shall be queried
	 */
	public InitializationTask(final TMQLRuntime runtime,
			final TopicMapSystem system, final TopicMap topicmap) {
		this.runtime = runtime;
		this.system = system;
		this.topicmap = topicmap;
	}

	/**
	 * {@inheritDoc}
	 */
	public void doRun() throws TMQLRuntimeException {
		context = new InitialContextImpl(runtime, system, topicmap);
	}

	/**
	 * {@inheritDoc}
	 */
	public IInitialContext getInitialContext() throws TMQLRuntimeException {
		if (isFinish()) {
			return context;
		}
		throw new TMQLRuntimeException(
				"process not finished, please use run() method first.");
	}

}
