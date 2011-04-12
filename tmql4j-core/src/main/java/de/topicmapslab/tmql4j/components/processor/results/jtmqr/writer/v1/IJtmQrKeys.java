/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.v1;

/**
 * @author Sven Krosse
 * 
 */
public interface IJtmQrKeys {

	/**
	 * the JSON hash key for version data
	 */
	public static final String VERSION = "version";

	/**
	 * the JSON hash key for sequences
	 */
	public static final String SEQUENCE = "seq";

	/**
	 * the JSON hash key for tuple values
	 */
	public static final String TUPLE = "t";
	/**
	 * the JSON hash key for tuple values
	 */
	public static final String ARRAY = "a";

	/**
	 * the JSON hash key for numerical values
	 */
	public static final String NUMBER = "n";

	/**
	 * the JSON hash key for number of rows
	 */
	public static final String ROWS = "rows";

	/**
	 * the JSON hash key for number of columns
	 */
	public static final String COLUMNS = "columns";

	/**
	 * the JSON hash key for the aliases
	 */
	public static final String ALIASES = "aliases";

	/**
	 * the JSON hash key for an alias
	 */
	public static final String ALIAS = "a";
	/**
	 * the JSON hash key for meta data
	 */
	public static final String METADATA = "metadata";

	/**
	 * the JSON hash key for string values
	 */
	public static final String STRING = "s";
	/**
	 * the JSON hash key for boolean values
	 */
	public static final String BOOLEAN = "b";
	/**
	 * the JSON hash key for locator values
	 */
	public static final String LOCATOR = "l";

	/**
	 * the JSON hash key for items
	 */
	public static final String ITEM = "i";

	/**
	 * the JSON hash key for items with quotes and colon
	 */
	public static final String QUOTED_ITEM = "\"i\":";

}
