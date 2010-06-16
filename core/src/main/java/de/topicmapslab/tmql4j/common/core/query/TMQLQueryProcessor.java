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

import java.util.Map;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntimeFactory;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.converter.IQueryProcessor;

public class TMQLQueryProcessor implements IQueryProcessor {

	private final Map<String, TMQLQuery> queries;

	public TMQLQueryProcessor() {
		this.queries = HashUtil.getHashMap();
	}

	@Override
	public TMQLQuery asTmqlQuery(String query) {
		if (queries.containsKey(query)) {
			return queries.get(query);
		}
		if (isValid(query)) {
			return new TMQLQuery(query);
		}
		throw new IllegalArgumentException(
				"String represenation cannot convert to a TMQL query!");
	}

	@Override
	public String getLanguageName() {
		return "TMQL";
	}

	@Override
	public IQuery getQuery(String query) {
		return asTmqlQuery(query);
	}

	@Override
	public boolean isValid(String query) {
		try {
			TMQLRuntimeFactory.newFactory().newRuntime(null).parse(query);
			return true;
		} catch (TMQLRuntimeException e) {
			return false;
		}
	}

}
