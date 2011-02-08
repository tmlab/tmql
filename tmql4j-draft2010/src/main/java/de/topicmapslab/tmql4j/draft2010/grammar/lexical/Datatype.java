package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

/**
 * The token of datatype axis
 * @author Sven Krosse
 *
 */
public class Datatype extends Token {

	/**
	 * the token
	 */
	private static final String TOKEN = "datatype";

	/**
	 * {@inheritDoc}
	 */	
	public String getLiteral() {
		return TOKEN;
	}

}
