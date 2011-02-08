package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

/**
 * The token of direct instance axis
 * @author Sven Krosse
 *
 */
public class DirectInstance extends Token {

	/**
	 * the token
	 */
	private static final String TOKEN = "direct-instance";

	/**
	 * {@inheritDoc}
	 */	
	public String getLiteral() {
		return TOKEN;
	}

}
