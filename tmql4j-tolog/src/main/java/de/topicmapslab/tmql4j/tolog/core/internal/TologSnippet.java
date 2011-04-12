package de.topicmapslab.tmql4j.tolog.core.internal;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.antlr.runtime.tree.Tree;

import de.topicmapslab.tmql4j.exception.TMQLConverterException;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Sub-Node of the tolog parser tree representing a grammar rule of a tolog
 * query.
 * 
 * @author Sven Krosse
 * 
 */
public abstract class TologSnippet {

	public static final int QUERY_SNIPPET = 0;
	public static final int RULE_SNIPPET = 1;
	public static final int USING_SNIPPET = 2;
	public static final int IMPORT_SNIPPET = 3;
	public static final int SELECT_SNIPPET = 4;
	public static final int FROM_SNIPPET = 5;
	public static final int AND_SNIPPET = 6;
	public static final int NOT_SNIPPET = 7;
	public static final int OR_SNIPPET = 8;
	public static final int OP_SNIPPET = 9;
	public static final int CLAUSELIST_SNIPPET = 10;
	public static final int ORDERBY_SNIPPET = 11;
	public static final int OFFSET_SNIPPET = 12;
	public static final int LIMIT_SNIPPET = 13;
	public static final int PREDICATEPAIR_SNIPPET = 14;

	private final List<Tree> tokens;
	private final List<String> tokensAsString;
	private final List<TologSnippet> children;
	private final int type;

	/**
	 * constructor
	 * 
	 * @param tokens
	 *            the antlr token of the current snippet
	 * @param type
	 *            the type
	 */
	public TologSnippet(List<Tree> tokens, int type) {
		this.type = type;
		this.tokens = new LinkedList<Tree>();
		this.children = new LinkedList<TologSnippet>();
		this.tokensAsString = new LinkedList<String>();

		for (Tree tree : tokens) {
			if (!tree.getText().equalsIgnoreCase(" ")) {
				tokensAsString.add(tree.getText());
				this.tokens.add(tree);
			}
		}
	}

	/**
	 * Method checks if their is a child contained of the specific type.
	 * 
	 * @param type
	 *            the type
	 * @return <code>true</code> if their is a child of the given type
	 *         contained, <code>false</code> otherwise.
	 */
	public boolean contains(int type) {
		for (TologSnippet child : children) {
			if (child.getType() == type) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Method checks if the given token is contained by the current node.
	 * 
	 * @param token
	 *            the token to check
	 * @return <code>true</code> if the specific token is contained,
	 *         <code>false</code> otherwise.
	 */
	public boolean containsToken(String token) {
		return tokensAsString.contains(token);
	}

	/**
	 * Returns the antlr specific tokens
	 * 
	 * @return the antlr specific tokens
	 */
	public List<Tree> getAntlrTokens() {
		return tokens;
	}

	/**
	 * Returns the list of child nodes.
	 * 
	 * @return the child nodes
	 */
	public List<TologSnippet> getChildren() {
		return children;
	}

	/**
	 * Returns the string-represented antlr tokens
	 * 
	 * @return the string-represented antlr tokens
	 */
	public List<String> getTokensAsString() {
		return tokensAsString;
	}

	/**
	 * Returns the internal type of snippet
	 * 
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * Checks if the node contains child nodes
	 * 
	 * @return <code>true</code> if the node contains at least one child node,
	 *         <code>false</code> otherwise.
	 */
	public boolean hasChildren() {
		return !children.isEmpty();
	}

	/**
	 * Adding a new child to this node
	 * 
	 * @param snippet
	 *            the new child
	 */
	protected void addChildren(TologSnippet snippet) {
		children.add(snippet);
	}

	/**
	 * Returns the filtered list of child nodes. The child nodes are filtered by
	 * the given type.
	 * 
	 * @param type
	 *            the type
	 * @return the filtered list of child nodes
	 */
	protected List<TologSnippet> getChildrenByType(int type) {
		List<TologSnippet> list = new LinkedList<TologSnippet>();
		for (TologSnippet snippet : getChildren()) {
			if (snippet.getType() == type) {
				list.add(snippet);
			}
		}
		return list;
	}

	/**
	 * Returns the list of contained tolog variables
	 * 
	 * @return the varible list
	 */
	public Set<String> getVariableList() {
		Set<String> variables = HashUtil.getHashSet();

		for (String token : getTokensAsString()) {
			if (token.startsWith("$")) {
				variables.add(token);
			}
		}

		return variables;
	}

	/**
	 * Convert the current tolog snippet to its TMQL corresponding sub-query.
	 * 
	 * @return the string-represented TMQL sub-query
	 * @throws TMQLConverterException thrown if query cannot transformed to TMQL
	 */
	public abstract String toTMQL() throws TMQLConverterException;

	/**
	 * Convert the current tolog snippet to its TMQL corresponding sub-query.
	 * 
	 * @param first
	 *            indicates if the current snippet is the first of a list
	 * @return the string-represented TMQL sub-query
	 * @throws TMQLConverterException thrown if token cannot transform to TMQL
	 */
	public abstract String toTMQL(boolean first) throws TMQLConverterException;
}
