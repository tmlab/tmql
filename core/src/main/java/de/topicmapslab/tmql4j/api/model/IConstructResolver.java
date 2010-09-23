/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.api.model;

import org.tmapi.core.Construct;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;

/**
 * Interface definition of a construct resolver provided by a specific data
 * bridge implementation ( {@link IDataBridge#getConstructResolver()} ) to look
 * up the topic map construct given by the string literal of its identifier.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IConstructResolver {

	/**
	 * Returns the topic item of the given topic map instance which can be
	 * identified by the given string literal as a subject-identifier.
	 * 
	 * @param identifier
	 *            the string literal of a subject-identifier
	 * @param topicMap
	 *            the topic map
	 * @return the topic map construct if it exists or <code>null</code>.
	 * @throws Exception
	 *             thrown by the internal topic map engine
	 */
	public Topic getTopicBySubjectIdentifier(final String identifier,
			final TopicMap topicMap) throws Exception;

	/**
	 * Returns the topic item of the given topic map instance which can be
	 * identified by the given string literal as a subject-locator.
	 * 
	 * @param identifier
	 *            the string literal of a subject-locator
	 * @param topicMap
	 *            the topic map
	 * @return the topic map construct if it exists or <code>null</code>.
	 * @throws Exception
	 *             thrown by the internal topic map engine
	 */
	public Topic getTopicBySubjectLocator(final String identifier,
			final TopicMap topicMap) throws Exception;

	/**
	 * Returns the information item of the given topic map instance which can be
	 * identified by the given string literal as a item-identifier.
	 * 
	 * @param identifier
	 *            the string literal of a subject-identifier
	 * @param topicMap
	 *            the topic map
	 * @return the topic map construct if it exists or <code>null</code>.
	 * @throws Exception
	 *             thrown by the internal topic map engine
	 */
	public Construct getConstructByItemIdentifier(final String identifier,
			final TopicMap topicMap) throws Exception;

	/**
	 * Returns the topic item of the given topic map instance which can be
	 * identified by the given string literal as a subject-identifier,
	 * subject-locator or item-identifier.
	 * 
	 * @param identifier
	 *            the string literal
	 * @param topicMap
	 *            the topic map
	 * @return the topic map construct if it exists or <code>null</code>.
	 * @throws Exception
	 *             thrown by the internal topic map engine
	 */
	public Construct getConstructByIdentifier(final TMQLRuntime runtime,
			final String identifier, final TopicMap topicMap) throws Exception;
}
