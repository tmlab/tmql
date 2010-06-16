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

import de.topicmapslab.tmql4j.common.model.query.IQuery;

/**
 * Abstract base implementation of {@link IQuery} to implements the core
 * functionality of each query.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public abstract class QueryImpl implements IQuery {

	/**
	 * the string representation of the query
	 */
	private final String queryString;

	/**
	 * base constructor as shortcut to create a TMQL query. <br />
	 * <br />
	 * 
	 * @param queryString
	 *            the string representation of the query
	 */
	public QueryImpl(final String queryString) {
		this.queryString = queryString;
	}

	/**
	 * Method returns the string representation of the query.
	 * 
	 * @see QueryImpl#getQueryString()
	 */
	@Override
	public String toString() {
		return getQueryString();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getQueryString() {
		return queryString;
	}

}
