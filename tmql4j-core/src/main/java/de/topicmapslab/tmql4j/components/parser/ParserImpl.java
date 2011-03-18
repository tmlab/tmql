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

import java.text.MessageFormat;

import de.topicmapslab.tmql4j.components.lexer.ILexer;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLException;
import de.topicmapslab.tmql4j.exception.TMQLParserException;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * Base implementation of {@link IParser}. The parser transform the
 * string-represented and language-specific token lists to a tree-representation
 * by interpreting the current TMQL draft. The generated parsing tree
 * representing the structure of the given query split by productions rules.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public abstract class ParserImpl implements IParser {
	/**
	 * exception message
	 */
	private static final String THE_QUERY_CONTAINS_FORBIDDEN_EXPRESSION = "The query contains a {0} which is a forbidden expression!";
	/**
	 * the parsing result
	 */
	private IParserTree tree = null;
	/**
	 * the lexical scanner containing the lexical tokens
	 */
	private final ILexer lexer;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param lexer
	 *            the lexical scanner containing the lexical tokens to parse
	 */
	public ParserImpl(ILexer lexer) {
		this.lexer = lexer;
	}

	/**
	 * {@inheritDoc}
	 */
	public IParserTree getParserTree() {
		return this.tree;
	}

	/**
	 * {@inheritDoc}
	 */
	public void parse(ITMQLRuntime runtime) throws TMQLParserException {
		try {
			tree = getParserTreeInstance(runtime, lexer.getQuery(), lexer);
			if (!tree.isValid(runtime, lexer.getQuery())) {
				throw new TMQLParserException(MessageFormat.format(THE_QUERY_CONTAINS_FORBIDDEN_EXPRESSION, tree.root().getClass().getSimpleName()));
			}
		} catch (TMQLParserException e) {
			throw e;
		} catch (TMQLException e) {
			throw new TMQLParserException("Parser failed because of " + e.getLocalizedMessage());
		}
	}

	/**
	 * Method returns the parser tree instance used to generate the parser tree
	 * 
	 * @param runtime
	 *            the runtime
	 * @param query
	 *            the query
	 * @param lexer
	 *            the lexical scanner
	 * @return the parser tree instance and never <code>null</code>
	 */
	protected abstract ParserTreeImpl getParserTreeInstance(ITMQLRuntime runtime, IQuery query, ILexer lexer);

}
