package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

/**
 * The token of the occurrence axis
 * @author Sven Krosse
 *
 */
public class Occurrence extends Token {

	/**
	 * the token
	 */
	private static final String TOKEN = "occurrence";

	/**
	 * {@inheritDoc}
	 */	
	public String getLiteral() {
		return TOKEN;
	}

}
