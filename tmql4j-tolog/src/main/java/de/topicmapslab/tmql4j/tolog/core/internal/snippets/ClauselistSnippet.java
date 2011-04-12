package de.topicmapslab.tmql4j.tolog.core.internal.snippets;

import java.util.LinkedList;
import java.util.List;

import org.antlr.runtime.tree.Tree;

import de.topicmapslab.tmql4j.exception.TMQLConverterException;
import de.topicmapslab.tmql4j.tolog.core.internal.TologSnippet;
import de.topicmapslab.tmql4j.tolog.core.internal.TologUtility;

public class ClauselistSnippet extends TologSnippet {

	/**
	 * constructor
	 * 
	 * @param tree
	 *            the parsed tokens
	 * @param utility
	 *            the utility class
	 */
	public ClauselistSnippet(List<Tree> tree, TologUtility utility) {
		super(tree, TologSnippet.CLAUSELIST_SNIPPET);

		List<Tree> subtree = new LinkedList<Tree>();

		boolean isOr = false;
		boolean isNot = false;
		boolean isOp = false;
		int bracketCount = 0;
		for (Tree node : tree) {
			if (node.getText().equalsIgnoreCase(",") && bracketCount == 0) {
				if (isOr) {
					addChildren(new OrSnippet(subtree, utility));
					isOr = false;
				} else if (isNot) {
					addChildren(new NotSnippet(subtree, utility));
					isNot = false;
				} else if (isOp) {
					addChildren(new OpSnippet(subtree, utility));
					isOp = false;
				} else {
					addChildren(new PredicateSnippet(subtree, utility));
				}
				subtree = new LinkedList<Tree>();
				continue;
			} else if (node.getText().equalsIgnoreCase("{")) {
				if (bracketCount == 0) {
					isOr = true;
				}
				bracketCount++;

			} else if (node.getText().equalsIgnoreCase("}")) {
				bracketCount--;
			} else if (node.getText().equalsIgnoreCase("not")
					&& bracketCount == 0) {
				isNot = true;
			} else if (node.getText().equalsIgnoreCase("(")) {
				bracketCount++;
			} else if (node.getText().equalsIgnoreCase(")")) {
				bracketCount--;
			} else if (node.getText().equalsIgnoreCase("/=")
					&& bracketCount == 0) {
				isOp = true;
			}
			subtree.add(node);
		}

		if (!subtree.isEmpty()) {
			if (isOr) {
				addChildren(new OrSnippet(subtree, utility));
				isOr = false;
			} else if (isNot) {
				addChildren(new NotSnippet(subtree, utility));
				isNot = false;
			} else if (isOp) {
				addChildren(new OpSnippet(subtree, utility));
				isOp = false;
			} else {
				addChildren(new PredicateSnippet(subtree, utility));
			}
			subtree = new LinkedList<Tree>();
		}

	}

	@Override
	public String toTMQL() throws TMQLConverterException {
		return toTMQL(true);
	}

	@Override
	public String toTMQL(boolean isFirst) throws TMQLConverterException {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (TologSnippet snippet : getChildren()) {
			final String query = snippet.toTMQL(first);
			if (query.isEmpty()) {
				continue;
			}
			builder.append((first ? "" : " AND ") + query);
			first = false;
		}
		return builder.toString();
	}

}
