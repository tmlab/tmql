package de.topicmapslab.tmql4j.extension.tmml.grammar.tokens;

import de.topicmapslab.tmql4j.lexer.model.Token;

public class Merge extends Token {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLiteral() {
		return "MERGE";
	}

}
