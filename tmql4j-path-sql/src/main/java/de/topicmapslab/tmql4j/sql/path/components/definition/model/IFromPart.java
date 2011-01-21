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
public interface IFromPart {

	/**
	 * Returns the alias of this from part
	 * 
	 * @return the alias
	 */
	public String getAlias();

	/**
	 * Returns the table or content part of the from entry.
	 * 
	 * @return the table or content part
	 */
	public String getTableOrContent();

	/**
	 * The from part represent a table and not an inner selection
	 * @return <code>true</code> if the from part represents a table, <code>false</code> otherwise
	 */
	public boolean isTable();
}
