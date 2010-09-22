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

import java.util.Set;

import de.topicmapslab.tmql4j.common.utility.HashUtil;

/**
 * Class representing an event manager which handles the event fired during the
 * interpretation process of the TMQL engine.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class EventManager {

	/**
	 * set of registered event listeners
	 */
	private final Set<IEventListener<?>> eventListeners = HashUtil.getHashSet();

	/**
	 * base constructor creating a new event manager
	 */
	public EventManager() {
	}

	/**
	 * Register a new event listener. Only registered event listener will be
	 * inform if an event happened.
	 * 
	 * @param eventListener
	 *            the event listener to add.
	 */
	public void addEventListener(IEventListener<?> eventListener) {
		eventListeners.add(eventListener);
	}

	/**
	 * Remove event listener from internal set. This method should be used if a
	 * listener instance wont use in future.
	 * 
	 * @param eventListener
	 *            the event listener to remove
	 */
	public void removeEventListener(IEventListener<?> eventListener) {
		eventListeners.remove(eventListener);
	}

	/**
	 * Method used to inform the event manager on a new event. The event fill be
	 * redirected to all registered listeners which are interested in.
	 * 
	 * @param <E>
	 *            the event type
	 * @param event
	 *            the event
	 */
	public <E extends Event> void event(E event) {
		for (IEventListener<?> listener : eventListeners) {
			if (listener.isInterested(event)) {
				listener.event(event);
			}
		}
	}

}
