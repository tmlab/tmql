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
public class Offset implements IQueryPart {

	private final long offset;

	/**
	 * Constructor
	 * 
	 * @param offset
	 *            the offset
	 */
	public Offset(long offset) {
		this.offset = offset;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toTmql() throws InvalidModelException {
		return de.topicmapslab.tmql4j.path.grammar.lexical.Offset.TOKEN + WhiteSpace.TOKEN + Long.toString(offset);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Offset clone() throws CloneNotSupportedException {
		return new Offset(offset);
	}

}
