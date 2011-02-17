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

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.util.HashUtil;

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
	 * the logger
	 */
	private static Logger logger = LoggerFactory.getLogger(QueryImpl.class);

	/**
	 * a set holding all forbidden expression types
	 */
	private Set<Class<? extends IExpression>> forbiddenExpressionTypes;

	/**
	 * the querying results
	 */
	private IResultSet<?> results;

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
	 * 
	 * @param queryString
	 *            the queryString to set
	 */
	protected void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/**
	 * 
	 * {@inheritDoc}
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

	/**
	 * {@inheritDoc}
	 */
	public void forbidExpression(Class<? extends IExpression> forbiddenExpressionType) {
		if (forbiddenExpressionTypes == null) {
			forbiddenExpressionTypes = HashUtil.getHashSet();
		}
		forbiddenExpressionTypes.add(forbiddenExpressionType);
	}

	/**
	 * {@inheritDoc}
	 */
	public void forbidModificationQueries() {
		for (Class<? extends IExpression> forbiddenExpressionType : getModificationExpressionTypes()) {
			forbidExpression(forbiddenExpressionType);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void allowExpression(Class<? extends IExpression> allowedExpressionType) {
		if (forbiddenExpressionTypes != null) {
			forbiddenExpressionTypes.remove(allowedExpressionType);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void allowModificationQueries() {
		for (Class<? extends IExpression> allowedExpressionType : getModificationExpressionTypes()) {
			allowExpression(allowedExpressionType);
		}
	}

	/**
	 * Utility method to load all expression of a specific language which occurs
	 * a modification of the topic map
	 * 
	 * @return a set of classes
	 */
	@SuppressWarnings("unchecked")
	private Set<Class<? extends IExpression>> getModificationExpressionTypes() {
		Set<Class<? extends IExpression>> classes = HashUtil.getHashSet();
		for (String name : getModificationExpressionTypeNames()) {
			try {
				Class<? extends IExpression> clazz = (Class<? extends IExpression>) Class.forName(name);
				classes.add(clazz);
			} catch (Exception e) {
				logger.warn("Expression type '" + name + "'not present in classpath");
			}
		}
		return classes;
	}	

	/**
	 * Returns an array of the full qualified expression types of the query
	 * language which occurs modification expression.
	 * 
	 * @return a string array
	 */
	protected abstract String[] getModificationExpressionTypeNames();
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isForbidden(Class<? extends IExpression> expressionType) {
		if (forbiddenExpressionTypes == null) {
			return false;
		}
		return forbiddenExpressionTypes.contains(expressionType);
	}
}
