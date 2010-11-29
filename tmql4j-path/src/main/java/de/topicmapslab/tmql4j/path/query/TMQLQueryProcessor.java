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
package de.topicmapslab.tmql4j.path.query;

import java.util.Map;

import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.components.processor.runtime.TMQLRuntimeFactory;
import de.topicmapslab.tmql4j.components.processor.util.HashUtil;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.query.IQuery;
import de.topicmapslab.tmql4j.query.IQueryProcessor;

public class TMQLQueryProcessor implements IQueryProcessor {

	private final Map<String, TMQLQuery> queries;

	public TMQLQueryProcessor() {
		this.queries = HashUtil.getHashMap();
	}

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

	@Override
	public String getLanguageName() {
		return "TMQL";
	}

	@Override
	public IQuery getQuery(TopicMap topicMap, String query) {
		return asTmqlQuery(topicMap, query);
	}

	@Override
	public boolean isValid(String query) {
		try {
			TMQLRuntimeFactory.newFactory().newRuntime(null).parse(query);
			return true;
		} catch (TMQLInvalidSyntaxException e) {
			return false;
		} catch (TMQLGeneratorException e) {
			return false;
		}
	}

}
