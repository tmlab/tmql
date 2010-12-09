package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

/**
 * The token of the variant axis
 * @author Sven Krosse
 *
 */
public class Variant extends Token {

	/**
	 * the token
	 */
	private static final String TOKEN = "variant";

	/**
	 * {@inheritDoc}
	 */	
	public String getLiteral() {
		return TOKEN;
	}

}
