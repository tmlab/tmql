/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator;

import java.util.List;

/**
 * @author Sven Krosse
 * 
 */
public interface ITranslatorContext {

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
	 * Returns the SQL part which represents the values for current node
	 * 
	 * @return the SQL part
	 */
	public String getContextOfCurrentNode();

	/**
	 * Sets the SQL part which represents the values for current node
	 * 
	 * @param context
	 *            the SQL part
	 */
	public void setContextOfCurrentNode(final String context);

	/**
	 * Returns the SQL part which represents the values for the given variable
	 * 
	 * @param variable
	 *            the variable
	 * @return the SQL part
	 */
	public String getContextOfVariable(final String variable);

	/**
	 * Sets the SQL part which represents the values for the given variable
	 * 
	 * @param variable
	 *            the variable
	 * @param context
	 *            the SQL part
	 */
	public void setContextOfVariable(final String variable, final String context);
	
	/**
	 * Returns all top level selections.
	 * <p>
	 * <b>Example:</b> If the last selection is 'SELECT id, value FROM ....' the
	 * top level selection is id and value.
	 * </p>
	 * 
	 * @return the top level selections
	 */
	public List<String> getTopLevelSelections();
}
