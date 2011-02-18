/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.core;

import de.topicmapslab.tmql4j.hibernate.IQueryPart;
import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;
import de.topicmapslab.tmql4j.path.grammar.lexical.WhiteSpace;

/**
 * @author Sven Krosse
 * 
 */
public class Limit implements IQueryPart {

	private final long limit;

	/**
	 * Constructor
	 * 
	 * @param limit
	 *            the limit
	 */
	public Limit(long limit) {
		this.limit = limit;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toTmql() throws InvalidModelException {
		return de.topicmapslab.tmql4j.path.grammar.lexical.Limit.TOKEN + WhiteSpace.TOKEN + Long.toString(limit);
	}

}
