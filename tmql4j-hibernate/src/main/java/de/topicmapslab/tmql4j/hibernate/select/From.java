/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.select;

import de.topicmapslab.tmql4j.hibernate.IQueryPart;
import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;
import de.topicmapslab.tmql4j.path.grammar.lexical.WhiteSpace;

/**
 * @author Sven Krosse
 * 
 */
public class From implements IQueryPart {

	private final IQueryPart content;

	/**
	 * constructor
	 * 
	 * @param content
	 *            the content
	 */
	public From(final IQueryPart content) {
		this.content = content;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toTmql() throws InvalidModelException {
		StringBuilder builder = new StringBuilder();
		builder.append(WhiteSpace.TOKEN);
		builder.append(de.topicmapslab.tmql4j.select.grammar.lexical.From.TOKEN);
		builder.append(WhiteSpace.TOKEN);
		builder.append(content.toTmql());
		builder.append(WhiteSpace.TOKEN);
		return builder.toString();
	}

}
