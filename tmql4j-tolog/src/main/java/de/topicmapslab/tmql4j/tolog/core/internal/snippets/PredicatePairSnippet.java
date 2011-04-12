package de.topicmapslab.tmql4j.tolog.core.internal.snippets;

import java.util.List;

import org.antlr.runtime.tree.Tree;

import de.topicmapslab.tmql4j.exception.TMQLConverterException;
import de.topicmapslab.tmql4j.tolog.core.internal.TologSnippet;
import de.topicmapslab.tmql4j.tolog.core.internal.TologUtility;

public class PredicatePairSnippet extends TologSnippet {

	/**
	 * constructor
	 * 
	 * @param tree
	 *            the parsed tokens
	 */
	public PredicatePairSnippet(List<Tree> tree) {
		super(tree, TologSnippet.USING_SNIPPET);
	}

	@Override
	public String toTMQL() throws TMQLConverterException {

		final List<String> tokens = getTokensAsString();
		final String expr = TologUtility.cleanUriref(tokens.get(0));
		final String ref;
		if (tokens.size() == 3) {
			ref = TologUtility.cleanUriref(tokens.get(2));
		} else {
			ref = "tm:subject";
		}
		return ref + " : " + expr;
	}

	@Override
	public String toTMQL(boolean isFirst) throws TMQLConverterException {
		return toTMQL();
	}

}
