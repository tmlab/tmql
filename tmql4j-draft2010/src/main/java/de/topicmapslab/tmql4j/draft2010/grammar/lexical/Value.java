package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

/**
 * the token of the value axis
 * @author Sven Krosse
 *
 */
public class Value extends Token {

	/**
	 * the token
	 */
	private static final String TOKEN = "value";

	/**
	 * {@inheritDoc}
	 */	
	public String getLiteral() {
		return TOKEN;
	}

}
