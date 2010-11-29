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
package de.topicmapslab.tmql4j.path.components.lexer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.topicmapslab.tmql4j.components.lexer.ILexer;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLLexerException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * Base implementation of a lexical scanner for the TMQL query language.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TMQLLexer implements ILexer {

	/**
	 * the list of interpreted tokens
	 */
	private List<Class<? extends IToken>> tmqlTokens = new LinkedList<Class<? extends IToken>>();
	/**
	 * the list of string-represented tokens
	 */
	private List<String> tokens = new LinkedList<String>();
	/**
	 * the query to scan
	 */
	private IQuery query = null;

	/**
	 * the TMQL4J runtime
	 */
	private final ITMQLRuntime runtime;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param query
	 *            the query to scan
	 */
	public TMQLLexer(final ITMQLRuntime runtime, final IQuery query) {
		this.query = query;
		this.runtime = runtime;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Class<? extends IToken>> getTmqlTokens() {
		return tmqlTokens;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getTokens() {
		return tokens;
	}

	/**
	 * {@inheritDoc}
	 */
	public Iterator<String> getTokenIterator() {
		return tokens.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	public Iterator<Class<? extends IToken>> getTmqlTokenIterator() {
		return tmqlTokens.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	public void execute() throws TMQLLexerException {
		try {
			/*
			 * used special tokenizer to split query into string-represented
			 * tokens
			 */
			TMQLTokenizer tokenizer = new TMQLTokenizer(query.toString());
			/*
			 * iterate over all tokens and try to identify the language-specific
			 * token
			 */
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				Class<? extends IToken> tmql = runtime.getLanguageContext()
						.getTokenRegistry().getTokenClassByLiteral(token);
				tmqlTokens.add(tmql);
				tokens.add(token);
			}
		} catch (Exception ex) {
			throw new TMQLLexerException("Lexer failed because of: "
					+ ex.getLocalizedMessage());
		}
	}

	/**
	 * Method to reset the results of lexical scanning.
	 */
	public void clear() {
		this.tokens.clear();
		this.tmqlTokens.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	public IQuery getQuery() {
		return query;
	}

}
