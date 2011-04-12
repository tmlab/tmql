package de.topicmapslab.tmql4j.tolog.core.internal.snippets;

import java.util.List;

import org.antlr.runtime.tree.Tree;

import de.topicmapslab.tmql4j.exception.TMQLConverterException;
import de.topicmapslab.tmql4j.tolog.core.internal.TologSnippet;
import de.topicmapslab.tmql4j.tolog.core.internal.TologUtility;

public class UsingSnippet extends TologSnippet {

	/**
	 * constructor
	 * 
	 * @param tree
	 *            the parsed tokens
	 */
	public UsingSnippet(List<Tree> tree) {
		super(tree, TologSnippet.USING_SNIPPET);
	}

	@Override
	public String toTMQL() throws TMQLConverterException {
		String ident = getTokensAsString().get(1);
		String uri;
		if (getTokensAsString().size() == 4) {
			uri = getTokensAsString().get(3);
		} else {
			uri = getTokensAsString().get(2);
		}

		uri = TologUtility.cleanUriref(uri);

		StringBuilder builder = new StringBuilder();
		builder.append("%prefix ");
		builder.append(ident + " ");
		builder.append(uri + " ");
		return builder.toString();
	}

	@Override
	public String toTMQL(boolean isFirst) throws TMQLConverterException {
		return toTMQL();
	}

}
