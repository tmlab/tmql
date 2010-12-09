package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

/**
 * The association axis token
 * @author Sven Krosse
 *
 */
public class Association extends Token {

	/**
	 * the token
	 */
	private static final String TOKEN = "association";

	/**
	 * {@inheritDoc}
	 */	
	public String getLiteral() {
		return TOKEN;
	}

}
