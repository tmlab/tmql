package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

/**
 * The token of optional filter ::
 * @author Sven Krosse
 *
 */
public class DoubleColon extends Token {

	/**
	 * the token
	 */
	public static final String TOKEN = "::";

	/**
	 * 
	 * {@inheritDoc}
	 */
	public String getLiteral() {
		return TOKEN;
	}

}
