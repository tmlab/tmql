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
 * Enumeration of all supported event types of the TMQL4J event model.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public enum EventType implements IEventType {

	/**
	 * insert event fired if new content will be insert into the topic map
	 */
	INSERT,

	/**
	 * update event fired if content of the topic map will be changed
	 */
	UPDATE,

	/**
	 * deletion event fired if content will be removed from the topic map
	 */
	DELETE,

	/**
	 * merge event fired if content will be merged
	 */
	MERGE;

}
