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
package de.topicmapslab.tmql4j.event.model;

/**
 * Interface definition of an event listener. Event listeners will be informed
 * by the event manager if they are registered there and if an event happened.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 * @param <E>
 *            the type of events the listener are interested in
 */
public interface IEventListener<E extends Event> {

	/**
	 * Callback method to inform the event listener about a new event if it is
	 * interested in.
	 * 
	 * @param event
	 *            the new event
	 * 
	 * @see IEventListener#isInterested(Event)
	 */
	public void event(Event event);

	/**
	 * Check if the event listener is interested in the event
	 * 
	 * @param event
	 *            the event to handle
	 * @return <code>true</code> if the listener is interested in the event,
	 *         <code>false</code> otherwise
	 */
	public boolean isInterested(Event event);

}
