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

import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;

/**
 * Interface definition of a query, which can be handled by the TMQL4 engine or
 * a plug-in. Each implementation of this interface represent a query of a
 * specific language.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IQuery {

	/**
	 * Method returns the internal string representation of the query.
	 * 
	 * @return the query content as string
	 */
	public String getQueryString();

	/**
	 * Returns the querying result stored by the TMQL4J runtime after finishing
	 * the querying process.
	 * 
	 * If the querying process is not finished yet, an exception will be thrown
	 * 
	 * @return the querying results and never <code>null</code>
	 * @throws TMQLRuntimeException
	 *             thrown if querying process not finished yet
	 */
	public IResultSet<?> getResults() throws TMQLRuntimeException;

	/**
	 * Method called by the TMQL runtime after the execution querying process
	 * 
	 * @param runtime
	 *            the runtime calling the method
	 */
	public void beforeQuery(ITMQLRuntime runtime);
	
	/**
	 * Method called by the TMQL runtime after the execution querying process
	 * 
	 * @param runtime
	 *            the runtime calling the method
	 */
	public void afterQuery(ITMQLRuntime runtime);
	
	/**
	 * returns the topic map to query
	 */
	public TopicMap getTopicMap();
	
	/**
	 * Set the topic map to query
	 * @param topicMap the topic map
	 */
	public void setTopicMap(TopicMap topicMap);
	
	/**
	 * Internal setter of the results
	 * 
	 * @param results
	 *            the results
	 */
	public void setResults(IResultSet<?> results);
	
	/**
	 * Forbid the expression as part of the parsed tree
	 * @param forbiddenExpressionType the expression to forbid
	 */
	public void forbidExpression(Class<? extends IExpression> forbiddenExpressionType);
	
	/**
	 * Allow the expression as part of the parsed tree. Please note, that this has only an effect if the expression was forbid before.
	 * @param allowedExpressionType the expression to allow
	 */
	public void allowExpression(Class<? extends IExpression> allowedExpressionType);
	
	/**
	 * Forbid any expression which modify the topic map
	 */
	public void forbidModificationQueries();
	
	/**
	 * Allow any expression which modify the topic map. Please note, that this has only an effect if the expressions were forbid before.
	 */
	public void allowModificationQueries();
	
	/**
	 * Checks if the current query forbids the given expression type
	 * @param expressionType the expression type
	 * @return <code>true</code> if the expression type is forbidden, <code>false</code> otherwise
	 */
	public boolean isForbidden(Class<? extends IExpression> expressionType);
}
