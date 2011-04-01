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
package de.topicmapslab.tmql4j.components.parser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.components.lexer.ILexer;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.extension.IExtensionPointAdapter;
import de.topicmapslab.tmql4j.extension.ILanguageExtension;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.query.IQuery;
import de.topicmapslab.tmql4j.util.HashUtil;

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
public abstract class ParserTreeImpl implements IParserTree {

	/**
	 * the logger
	 */
	private static Logger logger = LoggerFactory.getLogger(ParserImpl.class);

	/**
	 * the token at the end of a tree children
	 */
	private static final String TREE_CHILDREN_END = ")\r\n";
	/**
	 * the open bracket for beginning of tokens
	 */
	private static final String TREE_TOKENS = "(";
	/**
	 * the children part token
	 */
	private static final String TREE_CHILDREN = "|--";
	/**
	 * a white space
	 */
	private static final String WHITESPACE = "  ";
	/**
	 * the tree brother token
	 */
	private static final String TREE_BROTHER = "| ";
	/**
	 * the expression message
	 */
	private static final String INVALID_STATE_GRAMMAR_UNKNOWN_FOR_GIVEN_QUERY = "Invalid state, grammar unknown for given query.";
	/**
	 * the root node
	 */
	private IExpression root;
	/**
	 * the origin query
	 */
	private final IQuery query;

	/**
	 * private and hidden constructor to create a new instance
	 * 
	 * @param runtime
	 *            the runtime
	 * @param query
	 *            the query to transform
	 * @param lexer
	 *            the lexical scanner
	 */
	public ParserTreeImpl(ITMQLRuntime runtime, IQuery query, ILexer lexer) {
		this.query = query;
		/*
		 * get extension adapter
		 */
		IExtensionPointAdapter adapter = runtime.getExtensionPointAdapter();
		/*
		 * get extensions
		 */
		Set<ILanguageExtension> extensions = adapter.getLanguageExtensions(getRootExpressionClass());

		/*
		 * try to create root expression
		 */
		boolean useDefault = true;

		if (extensions != null) {
			/*
			 * iterate over all extensions
			 */
			for (ILanguageExtension extension : extensions) {
				/*
				 * check if production is valid for current extension
				 */
				if (extension.isValidProduction(runtime, lexer.getTmqlTokens(), lexer.getTokens(), null)) {
					/*
					 * create root node
					 */
					root = extension.parse(runtime, lexer.getTmqlTokens(), lexer.getTokens(), null, false);
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
			root = createRootExpression(runtime, lexer);
		}

		/*
		 * root should not be null
		 */
		if (root == null) {
			throw new TMQLGeneratorException(INVALID_STATE_GRAMMAR_UNKNOWN_FOR_GIVEN_QUERY);
		}
	}

	/**
	 * Returns the class of root expression of this parser tree
	 * 
	 * @return the class of root expression
	 */
	protected abstract Class<? extends IExpression> getRootExpressionClass();

	/**
	 * Utility method to create a root expression by calling the constructor
	 * with an instance of {@link ITMQLRuntime} and {@link ILexer}
	 * 
	 * @param <T>
	 *            the type of root expression to return
	 * @param runtime
	 *            the runtime
	 * @param lexer
	 *            the lexical scanner
	 * @return the created expression and never <code>null</code>
	 */
	@SuppressWarnings("unchecked")
	protected <T extends IExpression> T createRootExpression(ITMQLRuntime runtime, ILexer lexer) {
		try {
			Class<? extends IExpression> clazz = getRootExpressionClass();
			Constructor<? extends IExpression> constructor = clazz.getConstructor(ITMQLRuntime.class, ILexer.class);
			return (T) constructor.newInstance(runtime, lexer);
		} catch (SecurityException e) {
			throw new TMQLGeneratorException(INVALID_STATE_GRAMMAR_UNKNOWN_FOR_GIVEN_QUERY, e);
		} catch (NoSuchMethodException e) {
			throw new TMQLGeneratorException(INVALID_STATE_GRAMMAR_UNKNOWN_FOR_GIVEN_QUERY, e);
		} catch (IllegalArgumentException e) {
			throw new TMQLGeneratorException(INVALID_STATE_GRAMMAR_UNKNOWN_FOR_GIVEN_QUERY, e);
		} catch (InstantiationException e) {
			throw new TMQLGeneratorException(INVALID_STATE_GRAMMAR_UNKNOWN_FOR_GIVEN_QUERY, e);
		} catch (IllegalAccessException e) {
			throw new TMQLGeneratorException(INVALID_STATE_GRAMMAR_UNKNOWN_FOR_GIVEN_QUERY, e);
		} catch (InvocationTargetException e) {
			throw new TMQLGeneratorException(INVALID_STATE_GRAMMAR_UNKNOWN_FOR_GIVEN_QUERY, e);
		}
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
	 * {@inheritDoc}
	 */
	public void toStringTree(StringBuilder builder) {
		builder.append(root().getClass().getSimpleName());
		builder.append(TREE_TOKENS);
		builder.append(root().getTokens().toString());
		builder.append(TREE_CHILDREN_END);
		List<IExpression> nodes = root().getExpressions();

		for (int index = 0; index < nodes.size(); index++) {
			IExpression node = nodes.get(index);
			treeNodeToString(node, new boolean[] { false, index < (nodes.size() - 1) }, builder);
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
	private void treeNodeToString(final IExpression expression, boolean[] hasBrothers, final StringBuilder builder) {
		/*
		 * iterate over array
		 */
		for (int index = 0; index < hasBrothers.length - 1; index++) {
			boolean hasBrother = hasBrothers[index];
			if (hasBrother)
				/*
				 * add tree like symbolic lines for brothers
				 */
				builder.append(TREE_BROTHER);
			else {
				/*
				 * add white-spaces
				 */
				builder.append(WHITESPACE);
			}
		}
		/*
		 * create current node pattern
		 */
		builder.append(TREE_CHILDREN);
		/*
		 * add expression type
		 */
		builder.append(expression.getClass().getSimpleName());
		/*
		 * add tokens
		 */
		builder.append(TREE_TOKENS);
		builder.append(expression.getTokens().toString());
		builder.append(TREE_CHILDREN_END);

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

	/**
	 * 
	 * {@inheritDoc}
	 */
	public boolean isValid(ITMQLRuntime runtime, IQuery query) {
		/*
		 * iterate over top-level expression
		 */
		for (IExpression expression : root().getExpressions()) {
			/*
			 * check if expressions are allowed
			 */
			if (query.isForbidden(expression.getClass())) {
				logger.error("Expression type '" + expression.getClass().getName() + "' is forbidden!");
				return false;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<List<IExpression>> paths(Class<? extends IExpression> expressionType) {
		List<List<IExpression>> paths = HashUtil.getList();
		List<IExpression> currentPath = HashUtil.getList();
		currentPath.add(root());		
		paths(expressionType, root(), currentPath, paths);		
		return paths;
	}

	/**
	 * Utility method to navigate throw the tree and extract the paths for given
	 * expression type
	 * 
	 * @param expressionType
	 *            the expression type
	 * @param parent
	 *            the parent
	 * @param currentPath
	 *            the current path
	 * @param paths
	 *            the paths
	 */
	public void paths(Class<? extends IExpression> expressionType, IExpression parent, List<IExpression> currentPath, List<List<IExpression>> paths) {
		for (IExpression expression : parent.getExpressions()) {
			List<IExpression> currentPath_ = HashUtil.getList(currentPath);
			currentPath_.add(expression);
			if (expressionType.isAssignableFrom(expression.getClass())) {
				paths.add(currentPath_);
			} else {
				paths(expressionType, expression, currentPath_, paths);
			}
		}
	}
}
