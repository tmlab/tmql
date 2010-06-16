package de.topicmapslab.tmql4j.draft2010.query;

import de.topicmapslab.tmql4j.common.core.exception.TMQLInitializationException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.preprocessing.core.moduls.TMQLCanonizer;
import de.topicmapslab.tmql4j.preprocessing.model.ICanonizer;

/**
 * Query implementation representing a query of the draft of 2008.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TMQLQueryDraft2008 extends TMQLQuery {

	private final IQuery canonized;

	/**
	 * constructor
	 * 
	 * @param query
	 *            the string representation of the query
	 * @throws TMQLInitializationException
	 *             thrown if the query cannot be canonized
	 */
	public TMQLQueryDraft2008(String query) throws TMQLInitializationException {
		super(query);
		ICanonizer canonizer = new TMQLCanonizer(new TMQLQuery(query));
		try {
			canonizer.canonize();
			canonized = canonizer.getCanonizedQuery();
		} catch (TMQLRuntimeException e) {
			throw new TMQLInitializationException(
					"Cannot convert given tmql query", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public IQuery toTMQL() {
		return canonized;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getQueryString() {
		return canonized.getQueryString();
	}

}
