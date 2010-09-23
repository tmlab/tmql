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

import java.lang.reflect.ParameterizedType;

/**
 * Abstract base implementation of a listener for specific events fired by the
 * TMQL4J event model.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 * @param <E>
 *            the type of events, the listener is interested in
 */
public abstract class EventListener<E extends Event> implements
		IEventListener<E> {

	/**
	 * internal representation of the type argument E
	 */
	private final Class<? extends E> clazz;

	/**
	 * Base constructor create a new instance of an event listener and
	 * extracting the type information.
	 */
	@SuppressWarnings("unchecked")
	public EventListener() {
		clazz = (Class<? extends E>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isInterested(Event event) {
		return clazz.isInstance(event);
	}

}
