package de.topicmapslab.tmql4j.tolog.core.internal.snippets;

import java.util.List;

import org.antlr.runtime.tree.Tree;

import de.topicmapslab.tmql4j.exception.TMQLConverterException;
import de.topicmapslab.tmql4j.tolog.core.internal.TologSnippet;
import de.topicmapslab.tmql4j.tolog.core.internal.TologUtility;

public class OpSnippet extends TologSnippet {

	/**
	 * constructor
	 * 
	 * @param tree
	 *            the parsed tokens
	 * @param utility
	 *            the utility class
	 */
	public OpSnippet(List<Tree> tree, TologUtility utility) {
		super(tree, TologSnippet.OP_SNIPPET);
	}

	@Override
	public String toTMQL() throws TMQLConverterException {
		StringBuilder builder = new StringBuilder();
		builder.append(" NOT ( ");

		for (String token : getTokensAsString()) {
			if (token.equalsIgnoreCase("/=")) {
				builder.append(" == ");
			} else {
				builder.append(token + " ");
			}
		}

		builder.append(" )");
		return builder.toString();
	}

	@Override
	public String toTMQL(boolean isFirst) throws TMQLConverterException {
		return toTMQL();
	}

}
