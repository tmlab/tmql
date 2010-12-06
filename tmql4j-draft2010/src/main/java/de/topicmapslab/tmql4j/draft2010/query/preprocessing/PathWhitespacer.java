package de.topicmapslab.tmql4j.draft2010.query.preprocessing;

import de.topicmapslab.tmql4j.draft2010.grammar.lexical.DoubleColon;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.WhiteSpace;

public class PathWhitespacer {

	public static String whitespace(final String query) {
		StringBuilder builder = new StringBuilder();

		final String doubleColon = new DoubleColon().getLiteral();
		int index = query.indexOf(doubleColon);
		int last = 0;
		while (index != -1) {
			String leftHand = query.substring(last, index);
			builder.append(leftHand + new WhiteSpace().getLiteral() + doubleColon + new WhiteSpace().getLiteral());
			last = index + doubleColon.length();
			index = query.indexOf(new DoubleColon().getLiteral(), last);
		}

		String leftHand = query.substring(last);
		builder.append(leftHand);
		return builder.toString();
	}
}
