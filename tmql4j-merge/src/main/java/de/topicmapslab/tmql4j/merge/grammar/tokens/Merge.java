package de.topicmapslab.tmql4j.merge.grammar.tokens;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

public class Merge extends Token {

	public static final String TOKEN = "MERGE";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLiteral() {
		return TOKEN;
	}

}
