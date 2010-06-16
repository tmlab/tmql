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

import de.topicmapslab.tmql4j.resultprocessing.base.ResultSet;
import de.topicmapslab.tmql4j.resultprocessing.model.ResultType;

/**
 * Class representing a simple result set similar to a table result of a data
 * base. The result set only contains a number of simple tuple results.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SimpleResultSet extends ResultSet<SimpleTupleResult> {

	/**
	 * base constructor create an empty result set
	 */
	public SimpleResultSet() {
		// VOID
	}

	/**
	 * base constructor create a new result set containing the given results
	 * 
	 * @param results
	 *            the results to add
	 */
	public SimpleResultSet(Collection<SimpleTupleResult> results) {
		addResults(results);
	}

	/**
	 * base constructor create a new result set containing the given results
	 * 
	 * @param results
	 *            the results to add
	 */
	public SimpleResultSet(SimpleTupleResult... results) {
		addResults(results);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getResultType() {
		return ResultType.TMAPI.name();
	}

}
