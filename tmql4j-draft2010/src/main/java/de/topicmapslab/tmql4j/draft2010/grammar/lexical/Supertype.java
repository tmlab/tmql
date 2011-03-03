package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

/**
 * The supertype axis token
 * @author Sven Krosse
 *
 */
public class Supertype extends Token {

	/**
	 * the token
	 */
	private static final String TOKEN = "supertype";

	/**
	 * {@inheritDoc}
	 */	
	public String getLiteral() {
		return TOKEN;
	}

}
