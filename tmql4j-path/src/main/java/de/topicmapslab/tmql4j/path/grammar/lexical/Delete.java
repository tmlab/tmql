package de.topicmapslab.tmql4j.path.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

public class Delete extends Token {

	public static final String TOKEN = "DELETE";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLiteral() {
		return TOKEN;
	}

}
