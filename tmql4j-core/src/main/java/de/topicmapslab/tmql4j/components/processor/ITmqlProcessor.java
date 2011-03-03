/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor;

import java.io.OutputStream;

import de.topicmapslab.tmql4j.components.lexer.ILexer;
import de.topicmapslab.tmql4j.components.parser.IParser;
import de.topicmapslab.tmql4j.components.parser.IParserTree;
import de.topicmapslab.tmql4j.components.processor.prepared.IPreparedStatement;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultProcessor;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * @author Sven Krosse
 * 
 */
public interface ITmqlProcessor {

	/**
	 * Executes the querying process for the given query object
	 * 
	 * @param query
	 *            the query
	 * @return the results of querying process
	 */
	public IResultSet<?> query(IQuery query);

	/**
	 * Executes the querying process for the given query object
	 * 
	 * @param query
	 *            the query
	 * @param stream
	 *            the stream
	 * @return the results of querying process
	 */
	public IResultSet<?> query(IQuery query, OutputStream stream);

	/**
	 * Executes the querying process for the given statement object
	 * 
	 * @param stmt
	 *            the statement
	 * @return the results of querying process
	 */
	public IResultSet<?> query(IPreparedStatement statement);

	/**
	 * Parse the given query and returns a prepared statement instance
	 * 
	 * @param query
	 *            the query
	 * @return the prepared statement instance
	 */
	public IPreparedStatement prepared(IQuery query);

	/**
	 * Parse the given query and returns the query as parser tree
	 * 
	 * @param query
	 *            the query
	 * @return the parser tree instance
	 */
	public IParserTree parse(IQuery query);

	/**
	 * Returns the lexical scanner instance for the current TMQL processor
	 * 
	 * @param query
	 *            the query
	 * @return the lexical scanner
	 */
	public ILexer getTmqlLexer(IQuery query);

	/**
	 * Returns the parser instance for the current TMQL processor
	 * 
	 * @param query
	 *            the query
	 * @return the parser
	 */
	public IParser getTmqlParser(ILexer lexer);

	/**
	 * Returns the result processor instance for the current TMQL processor
	 * 
	 * @param query
	 *            the query
	 * @return the result processor
	 */
	public IResultProcessor getResultProcessor();

}
