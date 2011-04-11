package de.topicmapslab.tmql4j.path.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

public class All extends Token {

	public static final String TOKEN = "ALL";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLiteral() {
		return TOKEN;
	}

}
