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
package de.topicmapslab.tmql4j.path.components.parser;

import de.topicmapslab.tmql4j.components.lexer.ILexer;
import de.topicmapslab.tmql4j.components.parser.IParser;
import de.topicmapslab.tmql4j.components.parser.IParserTree;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLException;
import de.topicmapslab.tmql4j.exception.TMQLParserException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;

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
public final class TMQLParser implements IParser {

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
	public TMQLParser(ILexer lexer) {
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
			tree = ParserTreeImpl.create(lexer, runtime);
			if (!isValid(runtime)) {
				throw new TMQLParserException(
						"Parser tree is invalid, at least one expression are not allowed.");
			}
		} catch (TMQLParserException e) {
			throw e;
		} catch (TMQLException e) {
			throw new TMQLParserException("Parser failed because of "
					+ e.getLocalizedMessage());
		}
	}

	/**
	 * Method checks if the parser tree is valid. The parser tree is valid, if
	 * the parser tree contains on top-level only expressions which are allowed.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @return <code>true</code> if the parser tree is valid, <code>false</code>
	 *         otherwise.
	 * @throws TMQLParserException
	 */
	private boolean isValid(ITMQLRuntime runtime) throws TMQLParserException {

		/*
		 * if expressions are empty allow all
		 */
		if (runtime.getLanguageContext().getAllowedExpressionTypes().isEmpty()) {
			return true;
		}

		/*
		 * iterate over top-level expression
		 */
		for (IExpression expression : tree.root().getExpressions()) {
			/*
			 * check if expressions are allowed
			 */
			if (!runtime.getLanguageContext().getAllowedExpressionTypes()
					.contains(expression.getClass())) {
				return false;
			}
		}
		return true;
	}
}
