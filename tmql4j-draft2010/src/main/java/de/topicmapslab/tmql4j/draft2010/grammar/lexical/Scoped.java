package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

/**
 * The token of the scoped axis
 * @author Sven Krosse
 *
 */
public class Scoped extends Token {

	/**
	 * the token
	 */
	private static final String TOKEN = "scoped";

	/**
	 * {@inheritDoc}
	 */	
	public String getLiteral() {
		return TOKEN;
	}

}
