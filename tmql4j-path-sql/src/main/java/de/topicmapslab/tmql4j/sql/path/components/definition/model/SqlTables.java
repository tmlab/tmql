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
	 * additional state that current node is tm:subject
	 */
	TMSUBJECT,
	/**
	 * current nodes are topics
	 */
	TOPIC,
	/**
	 * current nodes are associations
	 */
	ASSOCIATION,
	/**
	 * current nodes are roles
	 */
	ROLE,
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
	 * current nodes are variants
	 */
	VARIANT,
	/**
	 * current node is anything else
	 */
	ANY,
	/**
	 * current nodes are strings
	 */
	STRING,
	/**
	 * current nodes are locators
	 */
	LOCATOR,
	/**
	 * is integer value
	 */
	INTEGER,
	/**
	 * is decimal value
	 */
	DECIMAL,
	/**
	 * is date-time
	 */
	DATETIME,
	/**
	 * a boolean
	 */
	BOOLEAN,
	/**
	 * current nodes are records of topics
	 */
	RECORD_TOPIC,
	/**
	 * current nodes are records of associations
	 */
	RECORD_ASSOCIATION,
	/**
	 * current nodes are records of roles
	 */
	RECORD_ROLE,
	/**
	 * current nodes are records of names
	 */
	RECORD_NAME,
	/**
	 * current nodes are records of occurrences
	 */
	RECORD_OCCURRENCE,
	/**
	 * current nodes are records of names or occurrences
	 */
	RECORD_CHARACTERISTICS,
	/**
	 * current nodes are records of variants
	 */
	RECORD_VARIANT,
	/**
	 * current node is record of anything else
	 */
	RECORD_ANY,
	/**
	 * current nodes are records of strings
	 */
	RECORD_STRING,
	/**
	 * current nodes are records of locators
	 */
	RECORD_LOCATOR,
	/**
	 * is record of integer values
	 */
	RECORD_INTEGER,
	/**
	 * is record of decimal values
	 */
	RECORD_DECIMAL,
	/**
	 * is record of date-times
	 */
	RECORD_DATETIME,
	/**
	 * a record of booleans
	 */
	RECORD_BOOLEAN;

	/**
	 * 
	 */
	private static final char CHAR = '_';
	/**
	 * 
	 */
	private static final String RECORD = "RECORD_";

	/**
	 * Returns the record type of the selected table
	 * 
	 * @param table
	 *            the table
	 * @return the record type
	 */
	public SqlTables recordOf() {
		if (name().startsWith(RECORD)) {
			return this;
		}
		String name = RECORD + name();
		return valueOf(name);
	}

	/**
	 * Returns the raw type of the selected record type
	 * 
	 * @param table
	 *            the record type
	 * @return the raw type
	 */
	public SqlTables rawOf() {
		String name = name();
		int index = name.indexOf(CHAR);
		if (index == -1) {
			return this;
		}
		return valueOf(name.substring(index + 1));
	}

	/**
	 * Checks if the record contains IDs of constructs
	 * 
	 * @param type
	 *            the SQL type
	 * @return <code>true</code> if the record contains constructs
	 */
	public boolean isRecord() {
		return name().startsWith(RECORD);
	}

	/**
	 * Checks if the record contains IDs of constructs
	 * 
	 * @param type
	 *            the SQL type
	 * @return <code>true</code> if the record contains constructs
	 */
	public boolean isConstructRecord() {
		return this == RECORD_NAME || this == RECORD_OCCURRENCE || this == RECORD_CHARACTERISTICS || this == RECORD_TOPIC || this == RECORD_ASSOCIATION || this == RECORD_ROLE
				|| this == RECORD_LOCATOR;
	}
}
