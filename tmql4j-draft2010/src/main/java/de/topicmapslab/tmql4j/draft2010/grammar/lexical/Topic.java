package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

/**
 * The token of the topic axis
 * @author Sven Krosse
 *
 */
public class Topic extends Token {

	/**
	 * the token
	 */
	private static final String TOKEN = "topic";

	/**
	 * {@inheritDoc}
	 */
	
	public String getLiteral() {
		return TOKEN;
	}

}
