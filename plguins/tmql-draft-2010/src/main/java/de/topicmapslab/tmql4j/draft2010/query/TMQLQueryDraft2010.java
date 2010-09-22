package de.topicmapslab.tmql4j.draft2010.query;

import de.topicmapslab.tmql4j.common.core.exception.TMQLInitializationException;
import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;

/**
 * Query implementation representing a query of the draft of 2008.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TMQLQueryDraft2010 extends TMQLQuery {

	/**
	 * constructor
	 * 
	 * @param query
	 *            the string representation of the query
	 * @throws TMQLInitializationException
	 *             thrown if the query cannot be canonized
	 */
	public TMQLQueryDraft2010(String query) throws TMQLInitializationException {
		super(query);
	}

}
