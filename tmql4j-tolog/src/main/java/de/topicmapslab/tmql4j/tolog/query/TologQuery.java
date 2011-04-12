package de.topicmapslab.tmql4j.tolog.query;

import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.query.QueryImpl;

/**
 * Special query class representing a tolog query.
 * 
 * @author Sven Krosse
 * 
 */
public class TologQuery extends QueryImpl {

	
	/**
	 * constructor 
	 * @param topicMap the topic map
	 * @param queryString the query string
	 */
	public TologQuery(TopicMap topicMap, String queryString) {
		super(topicMap, queryString);
	}

	/**
	 * {@inheritDoc}
	 */
	public void beforeQuery(ITMQLRuntime runtime) {
		// NOTHING TO DO HERE		
	}

	/**
	 * {@inheritDoc}
	 */
	public void afterQuery(ITMQLRuntime runtime) {
		// NOTHING TO DO HERE		
	}

	/**
	 * {@inheritDoc}
	 */
	protected String[] getModificationExpressionTypeNames() {
		return new String[0];
	}

}
