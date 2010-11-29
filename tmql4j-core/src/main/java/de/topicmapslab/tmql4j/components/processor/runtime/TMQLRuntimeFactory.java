/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.runtime;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * Factory class to create a new {@link ITMQLRuntime} instance to query a topic
 * map or a set of topic maps.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TMQLRuntimeFactory {

	/**
	 * private hidden constructor
	 */
	private TMQLRuntimeFactory() {

	}

	/**
	 * Creating a new {@link TMQLRuntimeFactory} used to create a new
	 * {@link ITMQLRuntime}
	 * 
	 * @return the new {@link TMQLRuntimeFactory}
	 */
	public static TMQLRuntimeFactory newFactory() {
		return new TMQLRuntimeFactory();
	}

	/**
	 * Create a new TMQL4J runtime
	 * 
	 * @return the created {@link ITMQLRuntime}
	 * @throws TMQLRuntimeException
	 *             thrown if the data-bridge instance cannot be instantiate
	 */
	public ITMQLRuntime newRuntime() throws TMQLRuntimeException {
		ServiceLoader<ITMQLRuntime> loader = ServiceLoader.load(ITMQLRuntime.class, TMQLRuntimeFactory.class.getClassLoader());
		Iterator<ITMQLRuntime> iterator = loader.iterator();
		if (iterator.hasNext()) {
			return iterator.next();
		}
		throw new TMQLRuntimeException("No implementation class for ITMQLRuntime found!");
	}

	/**
	 * Create a new TMQL4J runtime
	 * 
	 * 
	 * @param topicMapSystem
	 *            a topic maps system to create temporary maps
	 * @return the created {@link ITMQLRuntime}
	 * @throws TMQLRuntimeException
	 *             thrown if runtime cannot be created
	 * 
	 * @see ITMQLRuntime#setTopicMapSystem(TopicMapSystem)
	 */
	public ITMQLRuntime newRuntime(final TopicMapSystem topicMapSystem) throws TMQLRuntimeException {
		ITMQLRuntime runtime = newRuntime();
		runtime.setTopicMapSystem(topicMapSystem);
		return runtime;
	}

}
