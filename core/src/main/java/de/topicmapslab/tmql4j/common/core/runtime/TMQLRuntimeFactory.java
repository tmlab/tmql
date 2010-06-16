/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.common.core.runtime;

import java.io.PrintStream;

import org.tmapi.core.FactoryConfigurationException;
import org.tmapi.core.TMAPIException;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;

import de.topicmapslab.java.tmapi.extension.model.index.SupertypeSubtypeIndex;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.parser.model.IExpression;

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
	 * @param topicMap
	 *            a topic maps to query
	 * @return the created {@link ITMQLRuntime}
	 * @throws TMQLRuntimeException
	 *             thrown if the data-bridge instance cannot be instantiate
	 */
	public ITMQLRuntime newRuntime(final TopicMap topicMap)
			throws TMQLRuntimeException {
		try {
			return newRuntime(TopicMapSystemFactory.newInstance()
					.newTopicMapSystem(), topicMap);
		} catch (FactoryConfigurationException e) {
			throw new TMQLRuntimeException(e);
		} catch (TMAPIException e) {
			throw new TMQLRuntimeException(e);
		}
	}

	/**
	 * Create a new TMQL4J runtime
	 * 
	 * @param topicMapSystem
	 *            the topic map system used to handle CTM or XTM content
	 * @param topicMap
	 *            a topic maps to query
	 * @return the created {@link ITMQLRuntime}
	 * @throws TMQLRuntimeException
	 *             thrown if the data-bridge instance cannot be instantiate
	 */
	public ITMQLRuntime newRuntime(final TopicMapSystem topicMapSystem,
			final TopicMap topicMap) throws TMQLRuntimeException {
		return new TMQLRuntime(topicMapSystem, topicMap);
	}

	/**
	 * Create a new TMQL4J runtime
	 * 
	 * @param topicMapSystem
	 *            the topic map system used to handle CTM or XTM content
	 * @param topicMap
	 *            a topic maps to query
	 * @return the created {@link ITMQLRuntime}
	 * @param verbose
	 *            flag used to define verbose mode
	 * @param autoReindex
	 *            enable or disable auto indexing of internal indexes -
	 *            {@link SupertypeSubtypeIndex}
	 * @param printStream
	 *            the print stream used to print out debug information if
	 *            verbose is enabled
	 * @param allowedExpressionTypes
	 *            a list of all allowed expression types *
	 * @throws TMQLRuntimeException
	 *             thrown if the data-bridge instance cannot be instantiate
	 */
	public ITMQLRuntime newRuntime(final TopicMapSystem topicMapSystem,
			final TopicMap topicMap, boolean verbose, PrintStream stream,
			final boolean autoIndex,
			final Class<? extends IExpression>... expressionTypes)
			throws TMQLRuntimeException {
		return new TMQLRuntime(topicMapSystem, topicMap, verbose, autoIndex,
				stream, expressionTypes);
	}

}
