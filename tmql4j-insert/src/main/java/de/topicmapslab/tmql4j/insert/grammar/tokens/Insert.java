package de.topicmapslab.tmql4j.insert.grammar.tokens;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

public class Insert extends Token {

	public static final String TOKEN = "INSERT";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLiteral() {
		return TOKEN;
	}

}
