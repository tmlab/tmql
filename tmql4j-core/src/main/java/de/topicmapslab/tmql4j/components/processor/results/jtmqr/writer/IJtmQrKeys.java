/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer;

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
	 * the JSON hash key for items
	 */
	public static final String ITEM = "i";

	/**
	 * the JSON hash key for items with quotes and colon
	 */
	public static final String QUOTED_ITEM = "\"i\":";

	public static final String ITEM_TYPE = "item_type";

	public static final String TOPIC = "topic";

	public static final String ASSOCIATION = "association";

	public static final String NAME = "name";

	public static final String OCCURRENCE = "occurrence";

	public static final String ROLE = "role";

	public static final String VARIANT = "variant";

	public static final String SUBJECT_IDENTIFIERS = "subject_identifiers";

	public static final String SUBJECT_LOCATORS = "subject_locators";

	public static final String ITEM_IDENTIFIERS = "item_identifiers";

	public static final String TYPE = "type";

	public static final String TYPES = "types";

	public static final String NAMES = "names";

	public static final String OCCURRENCES = "occurrences";

	public static final String VARIANTS = "variants";

	public static final String ROLES = "roles";

	public static final String VALUE = "value";

	public static final String DATATYPE = "datatype";

	public static final String REIFIER = "reifier";

	public static final String SCOPE = "scope";

	public static final String ID = "id";

	public static final String PLAYER = "player";

	public static final String PREFIX_SI = "si:";

	public static final String PREFIX_SL = "sl:";

	public static final String PREFIX_II = "ii:";

}
