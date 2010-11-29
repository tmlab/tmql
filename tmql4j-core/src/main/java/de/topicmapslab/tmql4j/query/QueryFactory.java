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

import java.util.Map;
import java.util.ServiceLoader;

import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.components.processor.util.HashUtil;

/**
 * Factory to create a new query instance. The factory can instantiate queries
 * of different types or can detect the type of a given query.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class QueryFactory {

	private Map<String, IQueryProcessor> processors;
	private static QueryFactory factory;

	/**
	 * hidden constructor
	 */
	private QueryFactory() {
		processors = HashUtil.getHashMap();

		ServiceLoader<IQueryProcessor> loader = ServiceLoader.load(IQueryProcessor.class, getClass().getClassLoader());
		loader.reload();
		for (IQueryProcessor processor : loader) {
			processors.put(processor.getLanguageName(), processor);
		}
	}

	/**
	 * Returns the language specific query for the given string argument
	 * 
	 * @param topicMap
	 *            the topic map
	 * @param query
	 *            the string representation of the language-specific query
	 * @return the language-specific query or <code>null</code> if there is no
	 *         processor supporting the query type
	 */
	public IQuery getQuery(final TopicMap topicMap, final String query) {
		for (IQueryProcessor processor : processors.values()) {
			if (processor.isValid(query)) {
				return processor.getQuery(topicMap, query);
			}
		}
		return null;
	}

	/**
	 * Returns the TMQL query for the given string argument. The query will be
	 * transformed to a TMQL query if it does not represent a TMQL query.
	 * 
	 * @param topicMap
	 *            the topic map
	 * @param query
	 *            the string representation of the language-specific query
	 * @return the TMQL query or <code>null</code> if there is no processor
	 *         supporting the query type
	 */
	public IQuery getTmqlQuery(final TopicMap topicMap, final String query) {
		for (IQueryProcessor processor : processors.values()) {
			if (processor.isValid(query)) {
				return processor.asTmqlQuery(topicMap, query);
			}
		}
		return null;
	}

	/**
	 * This method adds a processor to the processor map. It should be used if
	 * the automatic registration fails, e.g. in OSGi contexts.
	 * 
	 * The registration is a map which key is the language name. If a processor
	 * for the language of the new processor is already used the new processor
	 * overrides the old one.
	 * 
	 * @param newProcessor
	 *            the new processor
	 */
	public void addQueryProcessor(IQueryProcessor newProcessor) {
		processors.put(newProcessor.getLanguageName(), newProcessor);
	}

	/**
	 * Returns the query factory instance. If the factory is called, a new
	 * instance will be created.
	 * 
	 * @return the factory
	 */
	public static final QueryFactory getFactory() {
		if (factory == null) {
			factory = new QueryFactory();
		}
		return factory;
	}

}
