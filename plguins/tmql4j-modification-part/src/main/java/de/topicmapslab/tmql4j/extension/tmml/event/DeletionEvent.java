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
 * Special modification event fired if a item was deleted from the internal
 * topic map by using the delete-expression of TMQL.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class DeletionEvent extends ModificationEvent {

	/**
	 * base constructor to create a new event
	 * 
	 * @param constructOrLocator
	 *            the deleted construct or locator
	 * @param origin
	 *            the origin of the event
	 */
	public DeletionEvent(Object constructOrLocator, Object origin) {
		super(constructOrLocator, constructOrLocator, EventType.DELETE, origin);
	}

}
