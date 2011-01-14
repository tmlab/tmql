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
public interface ISelection {

	/**
	 * Returns the alias 
	 * 
	 * @return the alias
	 */
	public String getAlias();

	/**
	 * Returns the column name.
	 * 
	 * @return the column name
	 */
	public String getColumn();
	
	/**
	 * Returns the selection as string matches the pattern
	 * 
	 *  <code>alias.column</code>
	 * 
	 * @return the selection
	 */
	public String getSelection();

}
