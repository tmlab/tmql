package de.topicmapslab.tmql4j.tolog.core.internal.snippets;

import java.util.LinkedList;
import java.util.List;

import org.antlr.runtime.tree.Tree;

import de.topicmapslab.tmql4j.exception.TMQLConverterException;
import de.topicmapslab.tmql4j.tolog.core.internal.TologSnippet;
import de.topicmapslab.tmql4j.tolog.core.internal.TologUtility;

public class OrSnippet extends TologSnippet {

	/**
	 * constructor
	 * 
	 * @param tree
	 *            the parsed tokens
	 * @param utility
	 *            the utility class
	 */
	public OrSnippet(List<Tree> tree, TologUtility utility) {
		super(tree, TologSnippet.USING_SNIPPET);
		List<Tree> subtree = new LinkedList<Tree>();
		subtree
				.addAll(getAntlrTokens()
						.subList(1, getAntlrTokens().size() - 1));

		List<Tree> list = new LinkedList<Tree>();
		for (Tree node : subtree) {
			if (node.getText().equalsIgnoreCase("|")) {
				addChildren(new ClauselistSnippet(list, utility));
				list = new LinkedList<Tree>();
			} else {
				list.add(node);
			}
		}

		if (!list.isEmpty()) {
			addChildren(new ClauselistSnippet(list, utility));
		}
	}

	@Override
	public String toTMQL() throws TMQLConverterException {
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		boolean first = true;
		for (TologSnippet snippet : getChildren()) {
			final String query = snippet.toTMQL(first);
			if (query.isEmpty()) {
				continue;
			}
			builder.append((first ? "(" : " OR ( ") + query + " )");
			first = false;
		}
		builder.append(")");
		/**
		 * no clause added
		 */
		if (first) {
			return "";
		}
		return builder.toString();
	}

	@Override
	public String toTMQL(boolean isFirst) throws TMQLConverterException {
		return toTMQL();
	}

}
