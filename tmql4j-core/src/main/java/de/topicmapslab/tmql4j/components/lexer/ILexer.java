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
package de.topicmapslab.tmql4j.components.lexer;

import java.util.Iterator;
import java.util.List;

import de.topicmapslab.tmql4j.exception.TMQLLexerException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * Interface definition of a lexical scanner. The lexical scanner split the
 * given TMQL query and extract a list of specific language tokens.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface ILexer {

	/**
	 * Method returns a list of specific language tokens represented by the
	 * class objects extending {@link IToken}.
	 * 
	 * @return a list of specific language tokens
	 */
	List<Class<? extends IToken>> getTmqlTokens();

	/**
	 * Method returns a list of string-represented tokens.
	 * 
	 * @return a list of string-represented tokens
	 */
	List<String> getTokens();

	/**
	 * Method returns a iterator to iterate over the list of language-specific
	 * tokens.
	 * 
	 * @return a new iterator instance
	 */
	Iterator<Class<? extends IToken>> getTmqlTokenIterator();

	/**
	 * Method returns a new iterator to iterate over the list of
	 * string-represented tokens.
	 * 
	 * @return a new iterator instance
	 */
	Iterator<String> getTokenIterator();

	/**
	 * Start the lexical scanner and split the given TMQL query to a list of
	 * specific and string-represented tokens. The lexical scanning will be
	 * split into to parts. At first the lexical scanner split the query to a
	 * list of string-represented tokens which will be transformed to
	 * language-specific tokens at the second step.
	 * 
	 * @throws TMQLLexerException
	 *             thrown if the lexical scanning fails
	 */
	void execute() throws TMQLLexerException;

	/**
	 * Method return the internal stored query.
	 * 
	 * @return the reference of the query
	 */
	IQuery getQuery();

}
