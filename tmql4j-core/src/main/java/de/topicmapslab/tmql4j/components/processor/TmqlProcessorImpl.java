/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor;

import de.topicmapslab.tmql4j.components.lexer.ILexer;
import de.topicmapslab.tmql4j.components.parser.IParser;
import de.topicmapslab.tmql4j.components.parser.IParserTree;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.prepared.IPreparedStatement;
import de.topicmapslab.tmql4j.components.processor.prepared.PreparedStatement;
import de.topicmapslab.tmql4j.components.processor.results.IResultProcessor;
import de.topicmapslab.tmql4j.components.processor.results.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.ResultSet;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * @author Sven Krosse
 * 
 */
public abstract class TmqlProcessorImpl implements ITmqlProcessor {

	private final ITMQLRuntime runtime;

	/**
	 * constructor
	 * 
	 * @param runtime
	 *            the runtime
	 */
	public TmqlProcessorImpl(ITMQLRuntime runtime) {
		this.runtime = runtime;
	}

	/**
	 * {@inheritDoc}
	 */
	public IResultSet<?> query(IQuery query) {
		IParserTree tree = parse(query);
		if (tree != null) {
			IContext context = new Context(this, query);

			QueryMatches results = tree.root().interpret(runtime, context);

			IResultProcessor resultProcessor = getResultProcessor();
			resultProcessor.proceed(results);

			return resultProcessor.getResultSet();
		}
		return ResultSet.emptyResultSet();
	}

	/**
	 * {@inheritDoc}
	 */
	public IResultSet<?> query(IPreparedStatement statement) {
		/*
		 * create context
		 */
		IContext context = new Context(this, statement);
		/*
		 * execute query
		 */
		QueryMatches results = statement.getParserTree().root().interpret(runtime, context);
		/*
		 * proceed results
		 */
		IResultProcessor resultProcessor = getResultProcessor();
		resultProcessor.proceed(results);
		/*
		 * set results to statement and return it
		 */
		IResultSet<?> resultSet = resultProcessor.getResultSet();
		statement.setResults(resultSet);
		return resultSet;
	}

	/**
	 * {@inheritDoc}
	 */
	public IParserTree parse(IQuery query) {
		ILexer lexer = getTmqlLexer(query);
		lexer.execute();
		if (!lexer.getTokens().isEmpty()) {
			IParser parser = getTmqlParser(lexer);
			parser.parse(runtime);
			return parser.getParserTree();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public IPreparedStatement prepared(IQuery query) {
		IParserTree tree = parse(query);
		if (tree != null) {
			return new PreparedStatement(runtime, query, tree);
		}
		return null;
	}

	/**
	 * @return the runtime
	 */
	protected ITMQLRuntime getRuntime() {
		return runtime;
	}
}
