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
package de.topicmapslab.tmql4j.query;

import org.tmapi.core.TopicMap;

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
	 * the topic map to query
	 */
	private TopicMap topicMap;

	/**
	 * the string representation of the query
	 */
	private String queryString;

	/**
	 * base constructor as shortcut to create a TMQL query. <br />
	 * <br />
	 * 
	 * @param topicMap
	 *            the topic map to query
	 * @param queryString
	 *            the string representation of the query
	 */
	public QueryImpl(final TopicMap topicMap, final String queryString) {
		this.topicMap = topicMap;
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

	/**
	 * 
	 * {@inheritDoc}
	 */
	public TopicMap getTopicMap() {
		return topicMap;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public void setTopicMap(TopicMap topicMap) {
		this.topicMap = topicMap;
	}
	
	/**
	 * Internal method to update the query string
	 * @param queryString the queryString to set
	 */
	protected void setQueryString(String queryString) {
		this.queryString = queryString;
	}
}
