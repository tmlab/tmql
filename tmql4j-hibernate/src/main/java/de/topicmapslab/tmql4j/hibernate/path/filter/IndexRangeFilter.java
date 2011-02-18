/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.path.filter;

import de.topicmapslab.tmql4j.hibernate.IHibernateConstants;
import de.topicmapslab.tmql4j.path.grammar.lexical.DoubleDot;

/**
 * @author Sven Krosse
 * 
 */
public class IndexRangeFilter extends Filter {

	private final long from;
	private final long to;

	/**
	 * constructor
	 * 
	 * @param from
	 *            the start index
	 * @param to
	 *            the first element not contained
	 */
	public IndexRangeFilter(long from, long to) {
		this.from = from;
		this.to = to;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	String getFilterPart() {
		return Long.toString(from) + IHibernateConstants.WHITESPACE + DoubleDot.TOKEN + IHibernateConstants.WHITESPACE
				+ Long.toString(to);
	}

}
