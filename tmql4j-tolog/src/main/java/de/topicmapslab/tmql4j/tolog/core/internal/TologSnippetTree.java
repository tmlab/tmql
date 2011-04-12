package de.topicmapslab.tmql4j.tolog.core.internal;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import org.antlr.runtime.tree.Tree;

import de.topicmapslab.tmql4j.tolog.core.internal.snippets.QuerySnippet;

/**
 * Tree structure representing the parsed tolog query.
 * 
 * @author Sven Krosse
 * 
 */
public class TologSnippetTree {

	private final QuerySnippet query;

	/**
	 * Constructor
	 * 
	 * @param root
	 *            the tree root
	 * @param utility
	 *            the utility class
	 */
	public TologSnippetTree(Tree root, TologUtility utility) {
		List<Tree> tokens = new LinkedList<Tree>();
		for (int index = 0; index < root.getChildCount(); index++) {
			tokens.add(root.getChild(index));
		}
		this.query = new QuerySnippet(tokens, utility);
	}

	/**
	 * Return the root node of the parser tree.
	 * 
	 * @return the root
	 */
	public TologSnippet getRoot() {
		return query;
	}

	/**
	 * Print the parser tree to the given stream
	 * 
	 * @param stream
	 *            the stream
	 */
	public void print(PrintStream stream) {
		print(stream, query, 0);
	}

	/**
	 * Print the given snippet located at the given level to the given stream.
	 * 
	 * @param stream
	 *            the stream
	 * @param snippet
	 *            the snippet
	 * @param lvl
	 *            the level
	 */
	private void print(PrintStream stream, TologSnippet snippet, int lvl) {
		for (int i = 0; i < lvl; i++) {
			stream.print("\t");
		}
		stream
				.println(snippet.getAntlrTokens() + "(" + snippet.getType()
						+ ")");
		lvl++;
		for (TologSnippet child : snippet.getChildren()) {
			print(stream, child, lvl);
		}
	}

}
