package de.topicmapslab.tmql4j.tolog.core.internal.snippets;

import java.util.LinkedList;
import java.util.List;

import org.antlr.runtime.tree.Tree;

import de.topicmapslab.tmql4j.exception.TMQLConverterException;
import de.topicmapslab.tmql4j.tolog.core.internal.TologRuleDefinition;
import de.topicmapslab.tmql4j.tolog.core.internal.TologSnippet;
import de.topicmapslab.tmql4j.tolog.core.internal.TologUtility;

public class PredicateSnippet extends TologSnippet {

	private final String predicate;
	private final TologUtility utility;

	/**
	 * constructor
	 * 
	 * @param tree
	 *            the parsed tokens
	 * @param utility
	 *            the utility class
	 */
	public PredicateSnippet(List<Tree> tree, TologUtility utility) {
		super(tree, TologSnippet.USING_SNIPPET);
		this.utility = utility;

		predicate = TologUtility.cleanUriref(tree.get(0).getText());

		List<Tree> subtree = new LinkedList<Tree>();
		for (int index = 2; index < getAntlrTokens().size() - 1; index++) {
			Tree node = getAntlrTokens().get(index);
			if (node.getText().equalsIgnoreCase(",")) {
				addChildren(new PredicatePairSnippet(subtree));
				subtree = new LinkedList<Tree>();
			} else {
				subtree.add(node);
			}
		}

		if (!subtree.isEmpty()) {
			addChildren(new PredicatePairSnippet(subtree));
		}

	}

	/**	 
	 * {@inheritDoc}
	 *  TODO: pre-defined predicates to navigation expression
	 */
	public String toTMQL() throws TMQLConverterException {
		StringBuilder builder = new StringBuilder();

		if (utility.containsRuleDefintion(predicate)) {
			TologRuleDefinition ruleDefinition = utility
					.getRuleDefintion(predicate);
			builder.append(ruleDefinition.clean(getTokensAsString()));
		} else if (!utility.addAsPredefinedPredicate(getTokensAsString())) {
			if (predicate.equalsIgnoreCase("direct-instance-of")
					|| predicate.equalsIgnoreCase("instance-of")) {

				List<String> variables = new LinkedList<String>();
				StringBuilder sb = new StringBuilder();
				for (String token : getTokensAsString()) {
					if (token.equalsIgnoreCase("(")) {
						sb = new StringBuilder();
					} else if (token.equalsIgnoreCase(",")
							|| token.equalsIgnoreCase(")")) {
						variables.add(sb.toString().trim());
						sb = new StringBuilder();
					} else {
						sb.append((sb.toString().isEmpty() ? "" : " ") + token);
					}
				}

				builder.append(variables.get(0) + " ISA " + variables.get(1));

			} else {
				builder.append(predicate);
				builder.append(" ( ");
				boolean first = true;
				for (TologSnippet snippet : getChildren()) {
					if (!first) {
						builder.append(" , ");
					}
					builder.append(snippet.toTMQL());
					first = false;
				}

				builder.append(" ) ");
			}

		}
		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public String toTMQL(boolean isFirst) throws TMQLConverterException {
		return toTMQL();
	}

}
