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

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.query.QueryImpl;

/**
 * @author Sven Krosse
 * 
 */
public class HatanaQuery extends QueryImpl {

	/**
	 * constructor
	 * 
	 * @param topicMap
	 *            the topic map
	 * @param queryString
	 *            the query string
	 */
	public HatanaQuery(TopicMap topicMap, String queryString) {
		super(topicMap, queryString);
	}

	/**
	 * {@inheritDoc}
	 */
	public void beforeQuery(ITMQLRuntime runtime) {
		// NOTHING TO DO
	}

	/**
	 * {@inheritDoc}
	 */
	public void afterQuery(ITMQLRuntime runtime) {
		// NOTHING TO DO
	}

	/**
	 * {@inheritDoc}
	 */
	protected String[] getModificationExpressionTypeNames() {
		return new String[0];
	}

}
