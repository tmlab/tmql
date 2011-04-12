package de.topicmapslab.tmql4j.tolog.core.internal.snippets;

import java.util.LinkedList;
import java.util.List;

import org.antlr.runtime.tree.Tree;

import de.topicmapslab.tmql4j.exception.TMQLConverterException;
import de.topicmapslab.tmql4j.tolog.core.internal.TologSnippet;
import de.topicmapslab.tmql4j.tolog.core.internal.TologUtility;

public class NotSnippet extends TologSnippet {

	/**
	 * constructor
	 * 
	 * @param tree
	 *            the parsed tokens
	 * @param utility
	 *            the utility class
	 */
	public NotSnippet(List<Tree> tree, TologUtility utility) {
		super(tree, TologSnippet.USING_SNIPPET);

		List<Tree> subtree = new LinkedList<Tree>();
		subtree.addAll(getAntlrTokens().subList(2, getAntlrTokens().size() - 1));

		addChildren(new ClauselistSnippet(subtree, utility));
	}

	@Override
	public String toTMQL() throws TMQLConverterException {
		StringBuilder builder = new StringBuilder();
		String clauses = getChildren().get(0).toTMQL();
		if (!clauses.isEmpty()) {
			builder.append(" NOT ( " + clauses + " ) ");
		}
		return builder.toString();
	}

	@Override
	public String toTMQL(boolean isFirst) throws TMQLConverterException {
		return toTMQL();
	}

}
