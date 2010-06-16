package de.topicmapslab.tmql4j.draft2010.tokens;

import de.topicmapslab.tmql4j.lexer.model.Token;

public class MatchesRegExp extends Token {

	@Override
	public String getLiteral() {
		return "=~";
	}

}
