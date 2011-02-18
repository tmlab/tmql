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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.osgi.TMQLActivator;

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
	 * the Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
	
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
		try {
			for (ITMQLRuntime r : TMQLActivator.getDefault().getTmqlRuntimes()) {
				return r;
			}
		} catch (Throwable t) {
			// we do nothing, cause we are not in an OSGi environment
			logger.warn("No Osgi Bundle found");
		}
		
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

	/**
	 * Create a new TMQL4J runtime
	 * 
	 * @param languageName
	 *            the name of the language which should be supported by the
	 *            runtime
	 * @return the created {@link ITMQLRuntime}
	 * @throws TMQLRuntimeException
	 *             thrown if the data-bridge instance cannot be instantiate
	 */
	public ITMQLRuntime newRuntime(final String languageName) throws TMQLRuntimeException {
		try {
			for (ITMQLRuntime runtime : TMQLActivator.getDefault().getTmqlRuntimes()) {
				if (runtime.getLanguageName().equalsIgnoreCase(languageName)) {
					return runtime;
				}
			}
		} catch (Throwable t) {
			// we do nothing, cause we are not in an OSGi environment
			logger.warn("No Osgi Bundle found");
		}
		
		ServiceLoader<ITMQLRuntime> loader = ServiceLoader.load(ITMQLRuntime.class, TMQLRuntimeFactory.class.getClassLoader());
		Iterator<ITMQLRuntime> iterator = loader.iterator();
		while (iterator.hasNext()) {
			ITMQLRuntime runtime = iterator.next();
			if (runtime.getLanguageName().equalsIgnoreCase(languageName)) {
				return runtime;
			}
		}
		throw new TMQLRuntimeException("No implementation class for ITMQLRuntime found, which supports the language '" + languageName + "'!");
	}

	/**
	 * Create a new TMQL4J runtime
	 * 
	 * 
	 * @param topicMapSystem
	 *            a topic maps system to create temporary maps
	 * @param languageName
	 *            the name of the language which should be supported by the
	 *            runtime
	 * @return the created {@link ITMQLRuntime}
	 * @throws TMQLRuntimeException
	 *             thrown if runtime cannot be created
	 * 
	 * @see ITMQLRuntime#setTopicMapSystem(TopicMapSystem)
	 */
	public ITMQLRuntime newRuntime(final TopicMapSystem topicMapSystem, final String languageName) throws TMQLRuntimeException {
		ITMQLRuntime runtime = newRuntime(languageName);
		runtime.setTopicMapSystem(topicMapSystem);
		return runtime;
	}

}
