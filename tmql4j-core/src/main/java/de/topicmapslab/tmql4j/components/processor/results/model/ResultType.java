/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.results.model;

/**
 * Enumeration representing the currently supported result types.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public enum ResultType {

	/**
	 * a result containing XML
	 */
	XML,

	/**
	 * a result containing CTM
	 */
	CTM,
	
	/**
	 * a result containing JTMQR
	 */
	JTMQR,

	/**
	 * simple construct results
	 */
	TMAPI;

}
