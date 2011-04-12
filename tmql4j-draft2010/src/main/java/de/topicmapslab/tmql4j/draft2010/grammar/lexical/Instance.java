package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

/**
 * The token of the instance axis
 * @author Sven Krosse
 *
 */
public class Instance extends Token {

	/**
	 * the token
	 */
	private static final String TOKEN = "instance";

	/**
	 * {@inheritDoc}
	 */
	
	public String getLiteral() {
		return TOKEN;
	}

}
