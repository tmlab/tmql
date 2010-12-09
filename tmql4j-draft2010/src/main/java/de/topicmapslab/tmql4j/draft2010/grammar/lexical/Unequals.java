package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

/**
 * The token of the unequals operator !=
 * @author Sven Krosse
 *
 */
public class Unequals extends Token {
	
	/**
	 * the token
	 */
	private static final String TOKEN = "!=";

	/**
	 * 
	 * {@inheritDoc}
	 */
	public String getLiteral() {
		return TOKEN;
	}

}
