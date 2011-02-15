package de.topicmapslab.tmql4j.merge.grammar.tokens;

import de.topicmapslab.tmql4j.grammar.lexical.Token;


public class Merge extends Token {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLiteral() {
		return "MERGE";
	}

}
