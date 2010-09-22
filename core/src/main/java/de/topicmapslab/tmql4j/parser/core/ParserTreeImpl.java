/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.parser.core;

import java.util.List;
import java.util.Set;

import de.topicmapslab.tmql4j.common.core.exception.TMQLException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.extensions.core.ExtensionPointAdapter;
import de.topicmapslab.tmql4j.extensions.model.ILanguageExtension;
import de.topicmapslab.tmql4j.extensions.model.ILanguageExtensionEntry;
import de.topicmapslab.tmql4j.lexer.model.ILexer;
import de.topicmapslab.tmql4j.optimizer.tree.QueryExpressionVariableCountQueryTreeOptimizer;
import de.topicmapslab.tmql4j.parser.core.expressions.QueryExpression;
import de.topicmapslab.tmql4j.parser.model.IExpression;
import de.topicmapslab.tmql4j.parser.model.IParserTree;

/**
 * Base implementation of a {@link IParserTree}. The parser tree is a
 * tree-structured representation of the origin query. Each node of the tree
 * represent exactly one production rule of the current TMQL draft. A node can
 * have a non-quantified number of children representing the current node in the
 * aggregate. Nodes of the same level never overlapping and cannot contain the
 * same tokens at the same time.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ParserTreeImpl implements IParserTree {

	/**
	 * the root node
	 */
	private final IExpression root;
	/**
	 * the origin query
	 */
	private final IQuery query;

	/**
	 * private and hidden constructor to create a new instance
	 * 
	 * @param root
	 *            the root node
	 * @param query
	 *            the query to transform
	 */
	private ParserTreeImpl(IExpression root, IQuery query) {
		this.root = root;
		this.query = query;
	}

	/**
	 * {@inheritDoc}
	 */
	public IExpression root() {
		return root;
	}

	/**
	 * {@inheritDoc}
	 */
	public IQuery getQuery() {
		return query;
	}

	/**
	 * Method creates a new parser tree. At first the structure of the
	 * expressions will be extracted and transform to a number of
	 * {@link IExpression} instances. Each instance of {@link IExpression}
	 * create their own children nodes.
	 * 
	 * @param lexer
	 *            the lexical scanner containing the tokens to transform
	 * @param runtime
	 *            the encapsulation runtime
	 * @return a new instance of {@link IParserTree}
	 * @throws TMQLException
	 *             thrown if generation of parsing tree fails
	 */
	protected static ParserTreeImpl create(ILexer lexer, ITMQLRuntime runtime)
			throws TMQLException {

		/*
		 * get extension adapter
		 */
		ExtensionPointAdapter adapter = runtime.getExtensionPointAdapter();
		/*
		 * get extensions
		 */
		Set<ILanguageExtension> extensions = adapter
				.getLanguageExtensions(QueryExpression.class);

		/*
		 * try to create root expression
		 */
		IExpression root = null;
		boolean useDefault = true;

		if (extensions != null) {
			/*
			 * iterate over all extensions
			 */
			for (ILanguageExtension extension : extensions) {
				/*
				 * get extension entry point
				 */
				ILanguageExtensionEntry entry = extension
						.getLanguageExtensionEntry();
				/*
				 * check if production is valid for current extension
				 */
				if (entry.isValidProduction(runtime, lexer.getTmqlTokens(),
						lexer.getTokens(), null)) {
					/*
					 * create root node
					 */
					root = entry.parse(runtime, lexer.getTmqlTokens(), lexer
							.getTokens(), null, false);
					useDefault = false;
					break;
				}
			}
		}

		/*
		 * check if default should be used
		 */
		if (useDefault) {
			/*
			 * create root node ( query-expression )
			 */
			root = new QueryExpression(lexer, (TMQLRuntime) runtime);
			/*
			 * check if optimization should be used
			 */
			if (runtime.getProperties().useOptimization()) {
				/*
				 * reorder tree structure
				 */
				root = new QueryExpressionVariableCountQueryTreeOptimizer()
						.optimize((QueryExpression) root);
			}
		}

		/*
		 * root should not be null
		 */
		if (root == null) {
			throw new TMQLGeneratorException(
					"Invalid state, grammar unknown for given query.");
		}

		/*
		 * create instance of parsing tree
		 */
		return new ParserTreeImpl(root, lexer.getQuery());
	}

	/**
	 * {@inheritDoc}
	 */
	public void toStringTree(StringBuilder builder) {
		builder.append(root().getClass().getSimpleName());
		builder.append("(");
		builder.append(root().getTokens().toString());
		builder.append(")\r\n");
		List<IExpression> nodes = root().getExpressions();

		for (int index = 0; index < nodes.size(); index++) {
			IExpression node = nodes.get(index);
			treeNodeToString(node, new boolean[] { false,
					index < (nodes.size() - 1) }, builder);
		}
	}

	/**
	 * Internal method to transform a tree node ( {@link IExpression} ) to a
	 * representative string. The string representation contains the name of the
	 * production rule and a list of contained lexical tokens.
	 * 
	 * @param expression
	 *            the expression representing the tree node
	 * @param hasBrothers
	 *            a array representing the upper-hierarchy. If the expression at
	 *            the specific level has brothers the array contains
	 *            <code>true</code> at this position, <code>false</code>
	 *            otherwise.
	 * @param builder
	 *            the string builder where the generated string will be written
	 *            in
	 */
	private void treeNodeToString(final IExpression expression,
			boolean[] hasBrothers, final StringBuilder builder) {

		/*
		 * iterate over array
		 */
		for (int index = 0; index < hasBrothers.length - 1; index++) {
			boolean hasBrother = hasBrothers[index];
			if (hasBrother)
				/*
				 * add tree like symbolic lines for brothers
				 */
				builder.append("| ");
			else {
				/*
				 * add white-spaces
				 */
				builder.append("  ");
			}
		}
		/*
		 * create current node pattern
		 */
		builder.append("|--");
		/*
		 * add expression type
		 */
		builder.append(expression.getClass().getSimpleName());
		/*
		 * add tokens
		 */
		builder.append("(");
		builder.append(expression.getTokens().toString());
		builder.append(")\r\n");

		boolean hasBrothers_[] = arrayCopy(hasBrothers);

		List<IExpression> nodes = expression.getExpressions();

		/*
		 * iterate over contained sub-expressions
		 */
		for (int index = 0; index < nodes.size(); index++) {
			IExpression node = nodes.get(index);
			hasBrothers_[hasBrothers.length] = index < (nodes.size() - 1);
			treeNodeToString(node, hasBrothers_, builder);
		}

	}

	/**
	 * Internal utility method to copy all values of the given array to a new
	 * instance and add a new empty field at the end of the new array. The
	 * length of the new array will be the same like the origin one extended by
	 * one.
	 * 
	 * @param array
	 *            the array to copy
	 * @return the new array
	 */
	private boolean[] arrayCopy(boolean[] array) {
		boolean copy[] = new boolean[array.length + 1];
		for (int i = 0; i < array.length; i++) {
			copy[i] = array[i];
		}
		return copy;
	}

}
