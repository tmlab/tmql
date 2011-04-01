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
package de.topicmapslab.tmql4j.components.processor.results.tmdm;

import de.topicmapslab.tmql4j.components.processor.results.model.Result;

/**
 * Class representing a simple tuple result similar to a row of a table result
 * of a data base. The result only contains a number of atomic or complex.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SimpleResult extends Result {

	/**
	 * base constructor create a new empty result
	 * 
	 * @param parent
	 *            the parent result set
	 */
	public SimpleResult(SimpleResultSet parent) {
		super(parent);
	}
}
