package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

/**
 * The token of the subtype axis
 * @author Sven Krosse
 *
 */
public class Subtype extends Token {

	/**
	 * the token
	 */
	private static final String TOKEN = "subtype";

	/**
	 * {@inheritDoc}
	 */	
	public String getLiteral() {
		return TOKEN;
	}

}
