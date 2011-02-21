/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.criterion;

import de.topicmapslab.tmql4j.hibernate.IQueryPart;

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

}
