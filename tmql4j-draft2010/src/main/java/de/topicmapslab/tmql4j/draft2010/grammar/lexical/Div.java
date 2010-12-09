package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;


/**
 * The token of the div operator
 * @author Sven Krosse
 *
 */
public class Div extends Token {
	
	/**
	 * the token
	 */
	private static final String TOKEN = "div";

	/**
	 * 
	 * {@inheritDoc}
	 */
	public String getLiteral() {
		return TOKEN;
	}

}
