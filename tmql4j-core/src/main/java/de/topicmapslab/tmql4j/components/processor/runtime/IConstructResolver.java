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

import de.topicmapslab.tmql4j.components.processor.core.IContext;

/**
 * @author Sven Krosse
 * 
 */
public interface IConstructResolver {

	/**
	 * Returns the topic item of the given topic map instance which can be
	 * identified by the given string literal as a subject-identifier.
	 * 
	 * @param context
	 *            the context
	 * @param identifier
	 *            the string literal of a subject-identifier
	 * @return the topic map construct if it exists or <code>null</code>.
	 */
	public Topic getTopicBySubjectIdentifier(final IContext context, final String identifier);

	/**
	 * Returns the topic item of the given topic map instance which can be
	 * identified by the given string literal as a subject-locator.
	 * 
	 * @param context
	 *            the context
	 * @param identifier
	 *            the string literal of a subject-locator
	 * @return the topic map construct if it exists or <code>null</code>.
	 */
	public Topic getTopicBySubjectLocator(final IContext context, final String identifier);

	/**
	 * Returns the information item of the given topic map instance which can be
	 * identified by the given string literal as a item-identifier.
	 * 
	 * @param context
	 *            the context
	 * @param identifier
	 *            the string literal of a subject-identifier
	 * @return the topic map construct if it exists or <code>null</code>.
	 */
	public Construct getConstructByItemIdentifier(final IContext context, final String identifier);

	/**
	 * Returns the topic item of the given topic map instance which can be
	 * identified by the given string literal as a subject-identifier,
	 * subject-locator or item-identifier.
	 * 
	 * @param context
	 *            the context
	 * @param identifier
	 *            the string literal
	 * @return the topic map construct if it exists or <code>null</code>.
	 */
	public Construct getConstructByIdentifier(final IContext context, final String identifier);
}
