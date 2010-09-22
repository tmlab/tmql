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
package de.topicmapslab.tmql4j.extension.tmml.event;

import de.topicmapslab.tmql4j.event.model.Event;
import de.topicmapslab.tmql4j.event.model.EventType;

/**
 * Base event class representing a modification event fired during the topic map
 * will be changed because of deletion, merging, inserts or updating .
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ModificationEvent extends Event {

	/**
	 * topic map content which was affected by this event
	 */
	private final Object modifiedConstructOrLocator;

	/**
	 * Base constructor to create a new event
	 * 
	 * @param modifiedConstructOrLocator
	 *            the modified construct or locator
	 * @param constructOrLocator
	 *            the origin construct or locator
	 * @param eventType
	 *            the event type
	 * @param origin
	 *            the origin of the event
	 */
	public ModificationEvent(final Object modifiedConstructOrLocator,
			final Object constructOrLocator, final EventType eventType,
			final Object origin) {
		super(constructOrLocator, eventType, origin);
		this.modifiedConstructOrLocator = modifiedConstructOrLocator;
	}

	/**
	 * Method returns the topic map construct which was affected.
	 * 
	 * @return the modifiedConstructOrLocator the affected element
	 */
	public Object getModifiedConstructOrLocator() {
		return modifiedConstructOrLocator;
	}

}
