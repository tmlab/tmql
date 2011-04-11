package de.topicmapslab.tmql4j.update.grammar.tokens;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

public class Update extends Token {

	public static final String TOKEN = "UPDATE";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLiteral() {
		return TOKEN;
	}

}
