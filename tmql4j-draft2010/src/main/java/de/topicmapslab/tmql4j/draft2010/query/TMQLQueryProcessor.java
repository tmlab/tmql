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
package de.topicmapslab.tmql4j.draft2010.query;

import java.util.Map;

import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.components.processor.runtime.TMQLRuntimeFactory;
import de.topicmapslab.tmql4j.draft2010.components.processor.runtime.TmqlRuntime;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.query.IQuery;
import de.topicmapslab.tmql4j.query.IQueryProcessor;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * The query processor of TMQL draft 2010
 * 
 * @author Sven Krosse
 * 
 */
public class TMQLQueryProcessor implements IQueryProcessor {

	/**
	 * the queries cache
	 */
	private final Map<String, TMQLQuery> queries;

	/**
	 * constructor
	 */
	public TMQLQueryProcessor() {
		this.queries = HashUtil.getHashMap();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public TMQLQuery asTmqlQuery(TopicMap topicMap, String query) {
		if (queries.containsKey(query)) {
			return queries.get(query);
		}
		if (isValid(query)) {
			return new TMQLQuery(topicMap, query);
		}
		throw new IllegalArgumentException("String represenation cannot convert to a TMQL query!");
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String getLanguageName() {
		return "TMQL-2010";
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public IQuery getQuery(TopicMap topicMap, String query) {
		return asTmqlQuery(topicMap, query);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid(String query) {
		try {
			TMQLRuntimeFactory.newFactory().newRuntime(TmqlRuntime.TMQL_2010).parse(query);
			return true;
		} catch (TMQLInvalidSyntaxException e) {
			e.printStackTrace();
			return false;
		} catch (TMQLGeneratorException e) {
			e.printStackTrace();
			return false;
		}
	}

}
