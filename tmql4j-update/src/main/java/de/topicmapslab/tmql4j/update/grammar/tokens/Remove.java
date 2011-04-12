package de.topicmapslab.tmql4j.update.grammar.tokens;

import de.topicmapslab.tmql4j.grammar.lexical.Token;


public class Remove extends Token {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLiteral() {
		return "REMOVE";
	}

}
