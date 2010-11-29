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
 * Interface definition of a query processor provide a language transformation
 * between a query language and TMQL. The query processor should provide methods
 * to check if a string-represented query can be proceeded by this instance. The
 * processor also should provide a transformation algorithm.
 * 
 * @author Sven Krosse
 * 
 */
public interface IQueryProcessor {

	/**
	 * Method checks if the given string represented query is a valid query of
	 * the processor-represented query language.
	 * 
	 * @param query
	 *            the query as string literal
	 * @return <code>true</code> if the query is valid in the context of the
	 *         processor-represented query language, <code>false</code>
	 *         otherwise.
	 */
	public boolean isValid(final String query);

	/**
	 * Method provides a query instance of the specific language.
	 * 
	 * @param topicMap
	 *            the topic map
	 * @param query
	 *            the query
	 * @return a data object representing the query of the specific language
	 * @throws IllegalArgumentException
	 *             thrown if the given string cannot represent within the
	 *             current language
	 */
	public IQuery getQuery(TopicMap topicMap, final String query);

	/**
	 * Method transforms the given query to a TMQL query.
	 * @param topicMap
	 *            the topic map
	 * @param query
	 *            the query
	 * @return the corresponding TMQL query
	 * @throws IllegalArgumentException
	 *             thrown if the given string cannot represent within the
	 *             current language
	 * @throws UnsupportedOperationException
	 *             thrown if the transformation is not supported by the current
	 *             processor
	 */
	public IQuery asTmqlQuery(TopicMap topicMap, final String query);

	/**
	 * Return the name of the current query language.
	 * 
	 * @return the language name
	 */
	public String getLanguageName();

}
