package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

/**
 * the token of the role axis
 * @author Sven Krosse
 *
 */
public class Role extends Token {

	/**
	 * the token
	 */
	private static final String TOKEN = "role";

	/**
	 * {@inheritDoc}
	 */	
	public String getLiteral() {
		return TOKEN;
	}

}
