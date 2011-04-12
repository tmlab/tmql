/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.v2;


/**
 * 
 * @author Christian Haß
 *
 */
public interface IJtmQr2Keys {

	/**
	 * the JSON hash key for version data
	 */
	public static final String VERSION = "version";

	/**
	 * the JSON hash key for tuples
	 */
	public static final String TUPLES = "tuples";

	/**
	 * the JSON hash key for meta data
	 */
	public static final String METADATA = "metadata";
	
	/**
	 * the JSON hash key for number of rows
	 */
	public static final String ROWS = "rows";

	/**
	 * the JSON hash key for number of columns
	 */
	public static final String COLUMNS = "columns";

	/**
	 * the JSON hash key for the headers
	 */
	public static final String HEADERS = "headers";
	
	/**
	 * the JSON hash key for jtm values
	 */
	public static final String JTM = "jtm";
	
	/**
	 * the JSON hash key for locator values
	 */
	public static final String REF = "ref";



}
