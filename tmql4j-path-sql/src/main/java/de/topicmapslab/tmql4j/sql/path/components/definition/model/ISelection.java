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
	 * Modify the internal alias
	 * 
	 * @param alias
	 *            the new alias
	 */
	public void setAlias(String alias);

	/**
	 * Returns the column name.
	 * 
	 * @return the column name
	 */
	public String getColumn();

	/**
	 * Returns the selection as string matches the pattern
	 * 
	 * <code>alias.column</code>
	 * 
	 * @return the selection
	 */
	public String getSelection();

	/**
	 * Adds a cast definition for this selection. Only one cast is allowed for
	 * each selection, past casts will be overwritten.
	 * 
	 * @param type
	 *            the type to cast to
	 */
	public void cast(String type);

	/**
	 * Sets the given table as current navigation point for this SQL definition.
	 * 
	 * @param table
	 *            the table
	 */
	public void setCurrentTable(SqlTables table);

	/**
	 * Returns the table which represents the current navigation point of this
	 * SQL definition
	 * 
	 * @return the table
	 */
	public SqlTables getCurrentTable();

}
