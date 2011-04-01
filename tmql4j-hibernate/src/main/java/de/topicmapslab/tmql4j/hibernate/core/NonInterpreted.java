/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.core;

import de.topicmapslab.tmql4j.hibernate.IQueryPart;
import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;

/**
 * @author Sven Krosse
 *
 */
public class NonInterpreted implements IQueryPart {

	private final String queryPart;
	
	/**
	 * constructor
	 * @param queryPart the query part
	 */
	public NonInterpreted(final String queryPart) {
		this.queryPart = queryPart;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toTmql() throws InvalidModelException {
		return this.queryPart;
	}
	


	/**
	  * {@inheritDoc}
	  */
	@Override
	public NonInterpreted clone() throws CloneNotSupportedException {
		return new NonInterpreted(queryPart);
	}

}
