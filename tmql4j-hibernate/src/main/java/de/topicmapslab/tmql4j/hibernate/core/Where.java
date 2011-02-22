/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.core;

import de.topicmapslab.tmql4j.hibernate.IQueryPart;
import de.topicmapslab.tmql4j.hibernate.criterion.ICriterion;
import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;
import de.topicmapslab.tmql4j.path.grammar.lexical.WhiteSpace;

/**
 * @author Sven Krosse
 * 
 */
public class Where implements IQueryPart {

	private final ICriterion criterion;

	/**
	 * Constructor
	 * 
	 * @param criterion
	 *            the criterion
	 */
	public Where(ICriterion criterion) {
		this.criterion = criterion;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toTmql() throws InvalidModelException {
		return de.topicmapslab.tmql4j.path.grammar.lexical.Where.TOKEN + WhiteSpace.TOKEN + this.criterion.toTmql();
	}
	
	/**
	  * {@inheritDoc}
	  */
	@Override
	public Where clone() throws CloneNotSupportedException {
		return new Where(criterion.clone());
	}

}
