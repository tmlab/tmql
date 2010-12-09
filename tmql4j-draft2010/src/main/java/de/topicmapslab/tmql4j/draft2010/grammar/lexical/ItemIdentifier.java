package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

/**
 * token of the item-identifier axis
 * @author Sven Krosse
 *
 */
public class ItemIdentifier extends Token {

	/**
	 * the token
	 */
	private static final String TOKEN = "item-identifier";

	/**
	 * {@inheritDoc}
	 */	
	public String getLiteral() {
		return TOKEN;
	}

}
