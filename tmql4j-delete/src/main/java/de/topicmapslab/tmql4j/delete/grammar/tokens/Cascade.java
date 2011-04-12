package de.topicmapslab.tmql4j.delete.grammar.tokens;

import de.topicmapslab.tmql4j.grammar.lexical.Token;


public class Cascade extends Token {

	public static final String TOKEN = "CASCADE";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLiteral() {
		return TOKEN;
	}

}
