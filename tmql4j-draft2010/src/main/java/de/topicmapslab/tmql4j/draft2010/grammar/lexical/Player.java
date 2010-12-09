package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

/**
 * the token of the player axis
 * @author Sven Krosse
 *
 */
public class Player extends Token {

	/**
	 * the token
	 */
	private static final String TOKEN = "player";

	/**
	 * {@inheritDoc}
	 */	
	public String getLiteral() {
		return TOKEN;
	}

}
