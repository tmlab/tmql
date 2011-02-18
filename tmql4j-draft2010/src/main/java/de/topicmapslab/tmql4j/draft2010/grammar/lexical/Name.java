package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

/**
 * The token of the name axis
 * @author Sven Krosse
 *
 */
public class Name extends Token {

	/**
	 * the token
	 */
	private static final String TOKEN = "name";

	/**
	 * {@inheritDoc}
	 */	
	public String getLiteral() {
		return TOKEN;
	}

}
