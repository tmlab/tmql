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
package de.topicmapslab.tmql4j.tolog.query;

import java.util.Map;

import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.exception.TMQLConverterException;
import de.topicmapslab.tmql4j.path.query.TMQLQuery;
import de.topicmapslab.tmql4j.query.IQuery;
import de.topicmapslab.tmql4j.query.IQueryProcessor;
import de.topicmapslab.tmql4j.tolog.core.TologConverter;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * {@link IQueryProcessor} to handle tolog queries.
 * 
 * @author Sven Krosse
 * 
 */
public class TologQueryProcessor implements IQueryProcessor {

	private Map<String, TMQLQuery> queries;

	/**
	 * {@inheritDoc}
	 */
	public IQuery asTmqlQuery(TopicMap topicMap, String query) {
		if (queries != null && queries.containsKey(query)) {
			return queries.get(query);
		}
		try {
			String tmqlQueryString = TologConverter.convert(query);
			if (queries == null) {
				queries = HashUtil.getHashMap();
			}
			TMQLQuery tmqlQuery = new TMQLQuery(topicMap, tmqlQueryString);
			queries.put(query, tmqlQuery);
			return tmqlQuery;
		} catch (TMQLConverterException e) {
			throw new IllegalArgumentException("Given query is not a valid tolog query or cannot convert to a TMQL query!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLanguageName() {
		return "tolog";
	}

	/**
	 * {@inheritDoc}
	 */
	public IQuery getQuery(TopicMap topicMap, String query, Object...  objects) {
		if (isValid(query)) {
			return new TologQuery(topicMap, query);
		}
		throw new IllegalArgumentException("Given query is not a valid tolog query!");
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isValid(String query) {
		if (!query.endsWith("?")) {
			return false;
		}
		try {
			TologConverter.convert(query);
			return true;
		} catch (TMQLConverterException e) {
			return false;
		}
	}

}
