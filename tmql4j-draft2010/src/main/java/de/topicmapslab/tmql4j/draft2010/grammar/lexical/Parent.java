package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

/**
 * The token of the parent axis.
 * @author Sven Krosse
 *
 */
public class Parent extends Token {

	/**
	 * the token
	 */
	private static final String TOKEN = "parent";

	/**
	 * {@inheritDoc}
	 */	
	public String getLiteral() {
		return TOKEN;
	}

}
