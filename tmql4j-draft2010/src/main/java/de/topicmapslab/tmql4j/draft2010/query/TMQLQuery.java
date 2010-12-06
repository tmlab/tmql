package de.topicmapslab.tmql4j.draft2010.query;

import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.query.preprocessing.TmqlWhiteSpacer;
import de.topicmapslab.tmql4j.query.QueryImpl;

/**
 * Query implementation representing a query of the draft of 2008.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TMQLQuery extends QueryImpl {

	/**
	 * a static array of full qualified names of all expression which occurs a
	 * modification of the topic map
	 */
	private static String[] modificationExpressions = new String[0];

	/**
	 * Constructor
	 * 
	 * @param topicMap
	 *            the topic map
	 * @param queryString
	 *            the query string
	 */
	public TMQLQuery(TopicMap topicMap, String queryString) {
		super(topicMap, queryString);
	}

	/**
	 * {@inheritDoc}
	 */
	public void beforeQuery(ITMQLRuntime runtime) {
		/*
		 * transform query and add necessary white spaces or remove multiple
		 * white spaces
		 */
		String whitespaced = TmqlWhiteSpacer.execute(runtime, getQueryString());
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

	/**
	 * {@inheritDoc}
	 */
	protected String[] getModificationExpressionTypeNames() {
		return modificationExpressions;
	}

}
