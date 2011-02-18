/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.path.filter;

import de.topicmapslab.tmql4j.hibernate.IHibernateConstants;
import de.topicmapslab.tmql4j.path.grammar.lexical.ShortcutAxisTypes;

/**
 * @author Sven Krosse
 * 
 */
public class TypeFilter extends Filter {

	private final String subjectIdentifier;

	/**
	 * constructor
	 * 
	 * @param subjectIdentifier
	 *            the subject identifier of type
	 */
	public TypeFilter(String subjectIdentifier) {
		this.subjectIdentifier = subjectIdentifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	String getFilterPart() {
		return ShortcutAxisTypes.TOKEN + IHibernateConstants.WHITESPACE + subjectIdentifier;
	}

}
