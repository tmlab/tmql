/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package net.ontopia.topicmaps.query.tmql.impl.basic;

import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.draft2010.query.TMQLQuery;

/**
 * @author Sven Krosse
 *
 */
public class Tmql4OntopiaQuery extends TMQLQuery {

	private final String topicMapId;
	
	/**
	 * constructor
	 * @param topicMap the topic map
	 * @param queryString the query string
	 * @param topicMapId the topic map id
	 */
	public Tmql4OntopiaQuery(TopicMap topicMap, String queryString, final String topicMapId) {
		super(topicMap, queryString);
		this.topicMapId = topicMapId;
	}
	
	/**
	 * @return the topicMapId
	 */
	public String getTopicMapId() {
		return topicMapId;
	}

}
