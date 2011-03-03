package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

/**
 * The token of direct-supertype axis
 * @author Sven Krosse
 *
 */
public class DirectSupertype extends Token {

	/**
	 * the token
	 */
	private static final String TOKEN = "direct-supertype";

	/**
	 * {@inheritDoc}
	 */	
	public String getLiteral() {
		return TOKEN;
	}

}
