/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.hatana.query;

import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.components.parser.IParserTree;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.TMQLRuntimeFactory;
import de.topicmapslab.tmql4j.exception.TMQLConverterException;
import de.topicmapslab.tmql4j.hatana.query.transformer.HatanaQueryTransformer;
import de.topicmapslab.tmql4j.hatana.query.transformer.IPSIRegistry;
import de.topicmapslab.tmql4j.query.IQuery;
import de.topicmapslab.tmql4j.query.IQueryProcessor;

/**
 * @author Sven Krosse
 * 
 */
public class HatanaQueryProcessor implements IQueryProcessor {

	public static final String HATANA_TMQL = "HatanaTMQL";

	/**
	 * {@inheritDoc}
	 */
	public IQuery getQuery(TopicMap topicMap, String query, Object... optionals) {
		if (optionals.length != 1 || !(optionals[0] instanceof IPSIRegistry)) {
			throw new TMQLConverterException("Missing argument PSI registry!");
		}
		ITMQLRuntime runtime = TMQLRuntimeFactory.newFactory().newRuntime();
		IParserTree tree = runtime.parse(query);
		String newQueryString = HatanaQueryTransformer.transform(tree, (IPSIRegistry) optionals[0]);
		return new HatanaQuery(topicMap, newQueryString);
	}

	/**
	 * {@inheritDoc}
	 */
	public IQuery asTmqlQuery(TopicMap topicMap, String query) {
		return getQuery(topicMap, query);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLanguageName() {
		return HATANA_TMQL;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isValid(String query) {
		return false;
	}

}
