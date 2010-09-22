/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2009 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.common.model.query;

/**
 * Enumeration containing all possible and known query types which can be return
 * by the method {@link IQuery#getType()}.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public enum QueryType {

	/**
	 * TMQL query
	 */
	TMQL,

	/**
	 * Toma query
	 */
	TOMA,

	/**
	 * tolog query
	 */
	TOLOG,

	/**
	 * TMPL query
	 */
	TMPL,

	/**
	 * any other query types like SQL or something else
	 */
	OTHER;

}
