package de.topicmapslab.tmql4j.draft2011.path.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

public class Delete extends Token {

	public static final String TOKEN = "DELETE";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLiteral() {
		return TOKEN;
	}

}
