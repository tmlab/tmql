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

	private final IQueryPart part;

	/**
	 * Constructor
	 * 
	 * @param part
	 *            the query part
	 */
	public Exists(IQueryPart part) {
		super(part);
		this.part = part;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Exists clone() throws CloneNotSupportedException {
		return new Exists(this.part.clone());
	}

}
