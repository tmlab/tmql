package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

/**
 * The token of the subject-identifier axis
 * 
 * @author Sven Krosse
 * 
 */
public class SubjectIdentifier extends Token {

	/**
	 * the token
	 */
	private static final String TOKEN = "subject-identifier";

	/**
	 * {@inheritDoc}
	 */
	public String getLiteral() {
		return TOKEN;
	}

}
