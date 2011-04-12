package de.topicmapslab.tmql4j.tolog.core.internal.snippets;

import java.util.List;

import org.antlr.runtime.tree.Tree;

import de.topicmapslab.tmql4j.exception.TMQLConverterException;
import de.topicmapslab.tmql4j.tolog.core.internal.TologSnippet;

public class OrderBySnippet extends TologSnippet {

	/**
	 * constructor
	 * 
	 * @param tree
	 *            the parsed tokens
	 */
	public OrderBySnippet(List<Tree> tree) {
		super(tree, TologSnippet.ORDERBY_SNIPPET);
	}

	@Override
	public String toTMQL() throws TMQLConverterException {
		StringBuilder builder = new StringBuilder();
		for (String token : getTokensAsString()) {
			builder.append(token + " ");
		}
		return builder.toString();
	}

	@Override
	public String toTMQL(boolean isFirst) throws TMQLConverterException {
		return toTMQL();
	}

}
