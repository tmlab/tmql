package de.topicmapslab.tmql4j.tolog.core.internal.snippets;

import java.util.LinkedList;
import java.util.List;

import org.antlr.runtime.tree.Tree;

import de.topicmapslab.tmql4j.exception.TMQLConverterException;
import de.topicmapslab.tmql4j.tolog.core.internal.TologSnippet;
import de.topicmapslab.tmql4j.tolog.core.internal.TologUtility;

public class FromSnippet extends TologSnippet {

	/**
	 * constructor
	 * 
	 * @param tree
	 *            the parsed tokens
	 * @param utility
	 *            the utility class
	 */
	public FromSnippet(List<Tree> tree, TologUtility utility) {
		super(tree, TologSnippet.FROM_SNIPPET);

		List<Tree> subtree = new LinkedList<Tree>();
		subtree.addAll(tree.subList(1, tree.size()));
		addChildren(new ClauselistSnippet(subtree, utility));
	}

	@Override
	public String toTMQL() throws TMQLConverterException {
		return toTMQL(true);
	}

	@Override
	public String toTMQL(boolean isFirst) throws TMQLConverterException {
		final String clause = getChildren().get(0).toTMQL();
		if (!clause.isEmpty()) {
			return " WHERE " + clause;
		}
		return "";
	}

}
