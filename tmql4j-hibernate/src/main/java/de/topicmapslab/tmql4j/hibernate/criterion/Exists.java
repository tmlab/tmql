/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.criterion;

import de.topicmapslab.tmql4j.hibernate.IQueryPart;
import de.topicmapslab.tmql4j.hibernate.core.NonInterpreted;

/**
 * @author Sven Krosse
 * 
 */
public class Exists extends Criterion {

	/**
	 * Constructor
	 * 
	 * @param part
	 *            the query part
	 */
	public Exists(IQueryPart part) {
		super(part.toTmql());
	}
	
	/**
	  * {@inheritDoc}
	  */
	@Override
	public Exists clone() throws CloneNotSupportedException {
		return new Exists(new NonInterpreted(toTmql()));
	}

}
