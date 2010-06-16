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
 * Abstract base class of events fired by the TMQL4J event model.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public abstract class Event {

	/**
	 * the internal event type
	 * 
	 * @see EventType
	 */
	private final IEventType eventType;
	/**
	 * the topic map element which was affected
	 */
	private final Object constructOrLocator;
	/**
	 * the origin which fire the event
	 */
	private final Object origin;

	/**
	 * Base constructor to create a new event.
	 * 
	 * @param constructOrLocator
	 *            the affected element
	 * @param eventType
	 *            the event type
	 * @param origin
	 *            the origin which fire the event
	 */
	public Event(Object constructOrLocator, IEventType eventType,
			final Object origin) {
		this.constructOrLocator = constructOrLocator;
		this.eventType = eventType;
		this.origin = origin;
	}

	/**
	 * Method returns the construct which was affected.
	 * 
	 * @return the affected element
	 */
	public Object getConstructOrLocator() {
		return constructOrLocator;
	}

	/**
	 * Method return the internal event type.
	 * 
	 * @return the event type
	 */
	public IEventType getEventType() {
		return eventType;
	}

	/**
	 * Method returns the origin of the event.
	 * 
	 * @return the origin
	 */
	public Object getOrigin() {
		return origin;
	}

	/**
	 * Method returns a string representation of the event instance containing
	 * information about the affected construct, the type and the origin. <br />
	 * <br />
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Event\r\n");
		buffer.append("reported by: " + getOrigin().getClass().getSimpleName()
				+ "\r\n");
		buffer.append("type: " + getEventType().name() + "\r\n");
		buffer.append("item: " + getConstructOrLocator() + "\r\n");
		return buffer.toString();
	}

}
