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

import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.path.query.preprocessing.TmqlScreener;
import de.topicmapslab.tmql4j.path.query.preprocessing.TmqlWhiteSpacer;
import de.topicmapslab.tmql4j.query.IQuery;
import de.topicmapslab.tmql4j.query.QueryImpl;

/**
 * Specific query implementation represent a TMQL query.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TMQLQuery extends QueryImpl {

	/**
	 * Constructor to create a new instance of a TMQL query.
	 * 
	 * @param topicMap
	 *            the topic map
	 * @param query
	 *            the string representation of the query
	 */
	public TMQLQuery(final TopicMap topicMap, final String query) {
		super(topicMap, query);
	}

	/**
	 * In context of TMQL query this method does not do anything.
	 * 
	 * @return the method returns itself ( <code>this</this> )
	 */
	public IQuery toTMQL() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public void beforeQuery(ITMQLRuntime runtime) {
		/*
		 * screen the given query string to remove comments
		 */
		String screened = TmqlScreener.screen(getQueryString());
		/*
		 * transform query and add necessary white spaces or remove multiple
		 * white spaces
		 */
		String whitespaced = TmqlWhiteSpacer.execute(runtime, screened);
		/*
		 * update internal query string
		 */
		setQueryString(whitespaced);
	}

	/**
	 * {@inheritDoc}
	 */
	public void afterQuery(ITMQLRuntime runtime) {
		// NOTHING TO DO
	}
}
