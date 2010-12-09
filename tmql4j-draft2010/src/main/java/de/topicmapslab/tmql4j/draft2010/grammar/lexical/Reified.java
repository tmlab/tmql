package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;


/**
 * The reified axis token
 * @author Sven Krosse
 *
 */
public class Reified extends Token {

	/**
	 * the token
	 */
	private static final String TOKEN = "reified";

	/**
	 * {@inheritDoc}
	 */	
	public String getLiteral() {
		return TOKEN;
	}

}
