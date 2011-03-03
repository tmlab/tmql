/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.path.filter;

import de.topicmapslab.tmql4j.hibernate.IHibernateConstants;
import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;
import de.topicmapslab.tmql4j.path.grammar.lexical.Scope;

/**
 * @author Sven Krosse
 * 
 */
public class ScopeFilter extends Filter {

	private final String subjectIdentifier;

	/**
	 * constructor
	 * 
	 * @param subjectIdentifier
	 *            the subject identifier of theme
	 */
	public ScopeFilter(String subjectIdentifier) {
		this.subjectIdentifier = subjectIdentifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toTmql() throws InvalidModelException {
		StringBuilder builder = new StringBuilder();
		builder.append(IHibernateConstants.WHITESPACE);
		builder.append(Scope.TOKEN);
		builder.append(IHibernateConstants.WHITESPACE);
		builder.append(subjectIdentifier);
		builder.append(IHibernateConstants.WHITESPACE);
		return builder.toString();
	}

}
