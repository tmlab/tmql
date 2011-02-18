/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.path.filter;

import de.topicmapslab.tmql4j.hibernate.IHibernateConstants;
import de.topicmapslab.tmql4j.path.grammar.lexical.Ako;
import de.topicmapslab.tmql4j.path.grammar.lexical.Dot;

/**
 * @author Sven Krosse
 * 
 */
public class SupertypeFilter extends Filter {

	private final String subjectIdentifier;

	/**
	 * constructor
	 * 
	 * @param subjectIdentifier
	 *            the subject identifier of super type
	 */
	public SupertypeFilter(String subjectIdentifier) {
		this.subjectIdentifier = subjectIdentifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	String getFilterPart() {
		return Dot.TOKEN + IHibernateConstants.WHITESPACE + Ako.TOKEN + IHibernateConstants.WHITESPACE
				+ subjectIdentifier;
	}

}
