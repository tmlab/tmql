package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

/**
 * The token of the regular-expression match operator
 * @author Sven Krosse
 *
 */
public class MatchesRegExp extends Token {

	
	/**
	 * the token
	 */
	private static final String TOKEN = "=~";

	/**
	 * 
	 * {@inheritDoc}
	 */
	public String getLiteral() {
		return TOKEN;
	}

}
