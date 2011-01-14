/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.definition.model;

/**
 * @author Sven Krosse
 *
 */
public enum SqlTables {

	/**
	 * current nodes are topics
	 */
	TOPIC,
	/**
	 * current nodes are associations
	 */
	ASSOCIATION,
	/**
	 * current node is the topic map
	 */
	TOPICMAP,
	/**
	 * current nodes are names
	 */
	NAME,
	/**
	 * current nodes are occurrences
	 */
	OCCURRENCE,
	/**
	 * current nodes are names or occurrences
	 */
	CHARACTERISTICS,
	/**
	 * current node is anything else
	 */
	ANY,
	/**
	 * current nodes are strings
	 */
	STRING,
	
}
