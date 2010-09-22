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
package de.topicmapslab.tmql4j.common.core.query;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;

/**
 * Specific query implementation represent a TMQL query.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TMQLQuery extends QueryImpl {

	/**
	 * the querying results
	 */
	private IResultSet<?> results;

	/**
	 * Constructor to create a new instance of a TMQL query.
	 * 
	 * @param query
	 *            the string representation of the query
	 */
	public TMQLQuery(final String query) {
		super(query);
	}

	/**
	 * In context of TMQL query this method does not do anything.
	 * 
	 * @return the method returns itself ( <code>this</this> )
	 */
	public IQuery toTMQL() {
		return this;
	}

	/**
	 * Internal setter of the results
	 * 
	 * @param results
	 *            the results
	 */
	public void setResults(IResultSet<?> results) {
		this.results = results;
	}

	/**
	 * {@inheritDoc}
	 */
	public IResultSet<?> getResults() throws TMQLRuntimeException {
		if (results == null) {
			throw new TMQLRuntimeException("Querying process not finished yet.");
		}
		return results;
	}

}
