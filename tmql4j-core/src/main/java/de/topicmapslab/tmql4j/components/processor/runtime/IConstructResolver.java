/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.runtime;

import org.tmapi.core.Construct;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

/**
 * @author Sven Krosse
 * 
 */
public interface IConstructResolver {

	/**
	 * Returns the topic item of the given topic map instance which can be
	 * identified by the given string literal as a subject-identifier.
	 * 
	 * @param topicMap
	 *            the topic map
	 * @param identifier
	 *            the string literal of a subject-identifier
	 * @return the topic map construct if it exists or <code>null</code>.
	 * @throws Exception
	 *             thrown by the internal topic map engine
	 */
	public Topic getTopicBySubjectIdentifier(final TopicMap topicMap, final String identifier) throws Exception;

	/**
	 * Returns the topic item of the given topic map instance which can be
	 * identified by the given string literal as a subject-locator.
	 * 
	 * @param topicMap
	 *            the topic map
	 * @param identifier
	 *            the string literal of a subject-locator
	 * @return the topic map construct if it exists or <code>null</code>.
	 * @throws Exception
	 *             thrown by the internal topic map engine
	 */
	public Topic getTopicBySubjectLocator(final TopicMap topicMap, final String identifier) throws Exception;

	/**
	 * Returns the information item of the given topic map instance which can be
	 * identified by the given string literal as a item-identifier.
	 * 
	 * @param topicMap
	 *            the topic map
	 * @param identifier
	 *            the string literal of a subject-identifier
	 * @return the topic map construct if it exists or <code>null</code>.
	 * @throws Exception
	 *             thrown by the internal topic map engine
	 */
	public Construct getConstructByItemIdentifier(final TopicMap topicMap, final String identifier) throws Exception;

	/**
	 * Returns the topic item of the given topic map instance which can be
	 * identified by the given string literal as a subject-identifier,
	 * subject-locator or item-identifier.
	 * 
	 * @param topicMap
	 *            the topic map
	 * @param identifier
	 *            the string literal
	 * @return the topic map construct if it exists or <code>null</code>.
	 * @throws Exception
	 *             thrown by the internal topic map engine
	 */
	public Construct getConstructByIdentifier(final TopicMap topicMap, final String identifier) throws Exception;
}
