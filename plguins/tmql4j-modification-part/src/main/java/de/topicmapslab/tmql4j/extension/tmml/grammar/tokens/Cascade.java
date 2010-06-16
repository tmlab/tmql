package de.topicmapslab.tmql4j.extension.tmml.grammar.tokens;

import de.topicmapslab.tmql4j.lexer.model.Token;

public class Cascade extends Token {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLiteral() {
		return "CASCADE";
	}

}
