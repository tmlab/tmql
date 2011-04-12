package de.topicmapslab.tmql4j.tolog.core.internal.snippets;

import java.util.List;

import org.antlr.runtime.tree.Tree;

import de.topicmapslab.tmql4j.exception.TMQLConverterException;
import de.topicmapslab.tmql4j.tolog.core.internal.TologSnippet;
import de.topicmapslab.tmql4j.tolog.core.internal.TologUtility;

public class ImportSnippet extends TologSnippet {

	private final TologUtility utility;

	/**
	 * constructor
	 * 
	 * @param tree
	 *            the parsed tokens
	 * @param utility
	 *            the utility class
	 */
	public ImportSnippet(List<Tree> tree, TologUtility utility) {
		super(tree, TologSnippet.IMPORT_SNIPPET);
		this.utility = utility;
	}

	@Override
	public String toTMQL() throws TMQLConverterException {
		String uri = getTokensAsString().get(1);
		String ident = getTokensAsString().get(3);
		this.utility.addNewImportExpression(uri, ident);
		return "";
	}

	@Override
	public String toTMQL(boolean isFirst) throws TMQLConverterException {
		return toTMQL();
	}

}
