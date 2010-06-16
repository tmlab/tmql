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
package de.topicmapslab.tmql4j.resultprocessing.core.simple;

import java.util.Collection;

import de.topicmapslab.tmql4j.resultprocessing.base.Result;

/**
 * Class representing a simple tuple result similar to a row of a table result
 * of a data base. The result only contains a number of atomic or complex.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SimpleTupleResult extends Result {

	/**
	 * base constructor create a new empty result
	 */
	public SimpleTupleResult() {
		// VOID
	}

	/**
	 * base constructor create a new result containing the given results
	 * 
	 * @param results
	 *            the results to add
	 */
	public SimpleTupleResult(Collection<Object> results) {
		add(results);
	}

	/**
	 * base constructor create a new result containing the given results
	 * 
	 * @param results
	 *            the results to add
	 */
	public SimpleTupleResult(Object... results) {
		add(results);
	}
}
