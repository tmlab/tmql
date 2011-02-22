/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.path.filter;

import de.topicmapslab.tmql4j.hibernate.IHibernateConstants;
import de.topicmapslab.tmql4j.hibernate.IQueryPart;
import de.topicmapslab.tmql4j.hibernate.criterion.ICriterion;
import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketSquareClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketSquareOpen;

/**
 * @author Sven Krosse
 * 
 */
public class Filter implements IQueryPart {

	private ICriterion criterion;

	/**
	 * constructor
	 * @param criterion the criterion
	 */
	public Filter(ICriterion criterion) {
		this.criterion = criterion;
	}
	
	/**
	 * hidden constructor
	 */
	Filter(){
		// VOID
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toTmql() throws InvalidModelException {
		StringBuilder builder = new StringBuilder();
		builder.append(IHibernateConstants.WHITESPACE);
		builder.append(BracketSquareOpen.TOKEN);
		builder.append(IHibernateConstants.WHITESPACE);
		builder.append(getFilterPart());
		builder.append(IHibernateConstants.WHITESPACE);
		builder.append(BracketSquareClose.TOKEN);
		builder.append(IHibernateConstants.WHITESPACE);
		return builder.toString();
	}
	
	/**
	 * Utility method to fetch the content of filter
	 * @return the filter content
	 */
	String getFilterPart(){
		return criterion.toTmql();
	}
	
	/**
	  * {@inheritDoc}
	  */
	@Override
	public Filter clone() throws CloneNotSupportedException {
		return new Filter(criterion.clone());
	}

}
