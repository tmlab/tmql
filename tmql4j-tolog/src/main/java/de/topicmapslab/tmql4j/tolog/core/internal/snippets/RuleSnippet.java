package de.topicmapslab.tmql4j.tolog.core.internal.snippets;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.antlr.runtime.tree.Tree;

import de.topicmapslab.tmql4j.exception.TMQLConverterException;
import de.topicmapslab.tmql4j.tolog.core.internal.TologRuleDefinition;
import de.topicmapslab.tmql4j.tolog.core.internal.TologSnippet;
import de.topicmapslab.tmql4j.tolog.core.internal.TologUtility;

public class RuleSnippet extends TologSnippet {

	private final TologUtility utility;

	/**
	 * constructor
	 * 
	 * @param tree
	 *            the parsed tokens
	 * @param utility
	 *            the utility class
	 */
	public RuleSnippet(List<Tree> tree, TologUtility utility) {
		super(tree, TologSnippet.RULE_SNIPPET);
		this.utility = utility;
		int index = getTokensAsString().indexOf(":-");
		List<Tree> subtree = new LinkedList<Tree>();
		subtree
				.addAll(getAntlrTokens()
						.subList(index, getAntlrTokens().size()));
		addChildren(new ClauselistSnippet(subtree, utility));
	}

	@Override
	public String toTMQL() throws TMQLConverterException {

		String tmql = getChildrenByType(CLAUSELIST_SNIPPET).get(0).toTMQL();
		StringTokenizer tokenizer = new StringTokenizer(tmql, " ");
		List<String> tokens = new LinkedList<String>();
		while (tokenizer.hasMoreTokens()) {
			tokens.add(tokenizer.nextToken());
		}
		TologRuleDefinition definition = new TologRuleDefinition(getTokensAsString(),tokens);
		utility.addNewRuleExpression(definition);
		return null;
	}

	@Override
	public String toTMQL(boolean isFirst) throws TMQLConverterException {
		return toTMQL();
	}

}
