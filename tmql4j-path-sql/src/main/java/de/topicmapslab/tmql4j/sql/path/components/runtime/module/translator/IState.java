/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator;

/**
 * @author Sven Krosse
 * 
 */
public interface IState {

	/**
	 * SQL tables represents the states
	 * 
	 * @author Sven Krosse
	 * 
	 */
	enum State {

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

	/**
	 * Returns the current state represents the SQL table the current state
	 * navigates to
	 * 
	 * @return the state
	 */
	public State getState();

	/**
	 * Returns the SQL part which represents the transformation until this state
	 * 
	 * @return the SQL part
	 */
	public String getInnerContext();

}
