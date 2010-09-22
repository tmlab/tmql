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

import de.topicmapslab.tmql4j.event.model.EventType;

/**
 * Special modification event fired if new content was inserted into the current
 * topic map using the insert-expression of TMQL.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class InsertEvent extends ModificationEvent {

	/**
	 * base constructor to create a new event
	 * 
	 * @param constructOrLocator
	 *            the new content which was inserted
	 * @param origin
	 *            the origin of the event
	 */
	public InsertEvent(Object constructOrLocator, Object origin) {
		super(constructOrLocator, constructOrLocator, EventType.INSERT, origin);
	}

}
