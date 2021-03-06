package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

/**
 * The token of direct-type axis
 * @author Sven Krosse
 *
 */
public class DirectType extends Token {

	/**
	 * the token
	 */
	private static final String TOKEN = "direct-type";

	/**
	 * {@inheritDoc}
	 */	
	public String getLiteral() {
		return TOKEN;
	}

}
