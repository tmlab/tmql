package de.topicmapslab.tmql4j.tolog.core.internal.snippets;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.antlr.runtime.tree.Tree;

import de.topicmapslab.tmql4j.exception.TMQLConverterException;
import de.topicmapslab.tmql4j.tolog.core.internal.TologSnippet;
import de.topicmapslab.tmql4j.tolog.core.internal.TologUtility;

/**
 * Tolog snippet represenation of a tolog query rule.
 * 
 * @author Sven Krosse
 * 
 */
public class QuerySnippet extends TologSnippet {

	private final TologUtility utility;

	/**
	 * constructor
	 * 
	 * @param tree
	 *            the parsed tokens
	 * @param utility
	 *            the utility class
	 */
	public QuerySnippet(List<Tree> tree, TologUtility utility) {
		super(tree, TologSnippet.USING_SNIPPET);
		this.utility = utility;

		Iterator<Tree> iterator = getAntlrTokens().iterator();
		List<Tree> list = new LinkedList<Tree>();
		boolean isFrom = false;
		boolean isOrderBy = false;
		while (iterator.hasNext()) {
			Tree node = iterator.next();
			if (node.getText().equalsIgnoreCase("USING")) {
				List<Tree> subtree = new LinkedList<Tree>();
				subtree.add(node); // USING
				subtree.add(iterator.next()); // IDENT
				subtree.add(iterator.next()); // FOR
				node = iterator.next();
				String text = node.getText();
				if ("a".equalsIgnoreCase(text) || "i".equalsIgnoreCase(text)
						|| "s".equalsIgnoreCase(text)) {
					subtree.add(node); // i | a | s
					subtree.add(iterator.next()); // URI
				} else {
					subtree.add(node); // URI
				}
				addChildren(new UsingSnippet(subtree));
				continue;
			} else if (node.getText().equalsIgnoreCase("IMPORT")) {
				List<Tree> subtree = new LinkedList<Tree>();
				subtree.add(node); // IMPORT
				subtree.add(iterator.next()); // URI
				subtree.add(iterator.next()); // AS
				subtree.add(iterator.next()); // IDENT
				addChildren(new ImportSnippet(subtree, utility));
				continue;
			} else if (node.getText().equalsIgnoreCase(".")) {
				list.add(node);
				addChildren(new RuleSnippet(list, utility));
				list = new LinkedList<Tree>();
				continue;
			} else if (node.getText().equalsIgnoreCase("FROM")) {
				addChildren(new SelectSnippet(list));
				list = new LinkedList<Tree>();
				isFrom = true;
			} else if (node.getText().equalsIgnoreCase("ORDER BY")) {
				if (isFrom) {
					addChildren(new FromSnippet(list, utility));
				} else {
					addChildren(new ClauselistSnippet(list, utility));
				}
				list = new LinkedList<Tree>();
				isFrom = false;
				isOrderBy = true;
			} else if (node.getText().equalsIgnoreCase("LIMIT")) {
				if (isOrderBy) {
					addChildren(new OrderBySnippet(list));
				} else if (isFrom) {
					addChildren(new FromSnippet(list, utility));
				} else {
					addChildren(new ClauselistSnippet(list, utility));
				}
				list = new LinkedList<Tree>();
				isFrom = false;
				isOrderBy = false;

				list.add(node);
				list.add(iterator.next());
				addChildren(new LimitSnippet(list));
				list = new LinkedList<Tree>();
			} else if (node.getText().equalsIgnoreCase("OFFSET")) {
				if (isOrderBy) {
					addChildren(new OrderBySnippet(list));
				} else if (isFrom) {
					addChildren(new FromSnippet(list, utility));
				} else {
					addChildren(new ClauselistSnippet(list, utility));
				}
				list = new LinkedList<Tree>();
				isFrom = false;
				isOrderBy = false;

				list.add(node);
				list.add(iterator.next());
				addChildren(new OffsetSnippet(list));
				list = new LinkedList<Tree>();
			} else if (node.getText().equalsIgnoreCase("?")) {
				if (isOrderBy) {
					addChildren(new OrderBySnippet(list));
				} else if (isFrom) {
					addChildren(new FromSnippet(list, utility));
				} else {
					addChildren(new ClauselistSnippet(list, utility));
				}
				break;
			} else if (node.getText().equalsIgnoreCase(" ")) {
				continue;
			}
			list.add(node);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String toTMQL() throws TMQLConverterException {
		StringBuilder builder = new StringBuilder();

		/*
		 * using part of tolog query
		 */
		if (contains(USING_SNIPPET)) {
			for (TologSnippet snippet : getChildrenByType(USING_SNIPPET)) {
				builder.append(snippet.toTMQL());
			}
		}

		/*
		 * import part of tolog query
		 */
		if (contains(IMPORT_SNIPPET)) {
			for (TologSnippet snippet : getChildrenByType(IMPORT_SNIPPET)) {
				builder.append(snippet.toTMQL());
			}
		}

		/*
		 * rule definition part of tolog query
		 */
		if (contains(RULE_SNIPPET)) {
			for (TologSnippet snippet : getChildrenByType(RULE_SNIPPET)) {
				builder.append(snippet.toTMQL());
			}
		}

		/*
		 * select part if exists
		 */
		if (contains(SELECT_SNIPPET)) {
			builder.append(getChildrenByType(SELECT_SNIPPET).get(0).toTMQL());
			/*
			 * from part of tolog query
			 */
			builder.append(getChildrenByType(FROM_SNIPPET).get(0).toTMQL());
		}
		/*
		 * clauslist part of tolog query if not select part exists
		 */
		else {
			builder.append("SELECT ");
			boolean first = true;
			ClauselistSnippet clauselist = (ClauselistSnippet) getChildrenByType(
					CLAUSELIST_SNIPPET).get(0);
			for (String variable : clauselist.getVariableList()) {
				builder.append((first ? "" : " , ") + variable);
				first = false;
			}
			builder.append(" WHERE ");
			builder.append(clauselist.toTMQL());

		}

		/*
		 * order by part of tolog query
		 */
		if (contains(ORDERBY_SNIPPET)) {
			builder.append(" "
					+ getChildrenByType(ORDERBY_SNIPPET).get(0).toTMQL() + " ");
		}
		/*
		 * offset part of tolog query
		 */
		if (contains(OFFSET_SNIPPET)) {
			builder.append(" "
					+ getChildrenByType(OFFSET_SNIPPET).get(0).toTMQL() + " ");
		}

		/*
		 * limit part of tolog
		 */
		if (contains(LIMIT_SNIPPET)) {
			builder.append(" "
					+ getChildrenByType(LIMIT_SNIPPET).get(0).toTMQL() + " ");
		}

		/*
		 * clean TMQL query
		 */
		String tmql = builder.toString();

		/*
		 * replace imports
		 */
		tmql = utility.replaceImports(tmql);
		/*
		 * clean predefined predicates
		 */
		tmql = utility.replaceVariables(tmql);

		return tmql;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toTMQL(boolean isFirst) throws TMQLConverterException {
		return toTMQL();
	}

}
