/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.flwr;

import de.topicmapslab.tmql4j.hibernate.IQueryPart;
import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;
import de.topicmapslab.tmql4j.path.grammar.lexical.In;
import de.topicmapslab.tmql4j.path.grammar.lexical.WhiteSpace;

/**
 * @author Sven Krosse
 *
 */
public class For implements IQueryPart {

	private final String variable;
	private final IQueryPart content;
	
	/**
	 * constructor
	 * @param variable the variable 
	 * @param content the content
	 */
	public For(final String variable, final IQueryPart content) {
		this.variable = variable;
		this.content = content;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toTmql() throws InvalidModelException {
		StringBuilder builder = new StringBuilder();
		builder.append(WhiteSpace.TOKEN);
		builder.append(de.topicmapslab.tmql4j.flwr.grammar.lexical.For.TOKEN);
		builder.append(WhiteSpace.TOKEN);
		builder.append(variable);
		builder.append(WhiteSpace.TOKEN);
		builder.append(In.TOKEN);
		builder.append(WhiteSpace.TOKEN);
		builder.append(content.toTmql());
		builder.append(WhiteSpace.TOKEN);
		return builder.toString();
	}

}
