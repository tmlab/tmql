/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.components.processor;

import de.topicmapslab.tmql4j.components.lexer.ILexer;
import de.topicmapslab.tmql4j.components.parser.IParser;
import de.topicmapslab.tmql4j.components.parser.IParserTree;
import de.topicmapslab.tmql4j.components.processor.ITmqlProcessor;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.results.IResultProcessor;
import de.topicmapslab.tmql4j.components.processor.results.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.ResultSet;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.path.components.lexer.TMQLLexer;
import de.topicmapslab.tmql4j.path.components.parser.TMQLParser;
import de.topicmapslab.tmql4j.path.components.processor.core.Context;
import de.topicmapslab.tmql4j.path.components.processor.results.TmqlResultProcessor;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * @author Sven Krosse
 * 
 */
public class TmqlProcessor2007 implements ITmqlProcessor {

	private final ITMQLRuntime runtime;
	private TmqlResultProcessor tmqlResultProcessor;

	/**
	 * constructor
	 * 
	 * @param runtime
	 *            the runtime
	 */
	public TmqlProcessor2007(ITMQLRuntime runtime) {
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
	public ILexer getTmqlLexer(IQuery query) {
		return new TMQLLexer(runtime, query);
	}

	/**
	 * {@inheritDoc}
	 */
	public IParser getTmqlParser(ILexer lexer) {
		return new TMQLParser(lexer);
	}

	/**
	 * {@inheritDoc}
	 */
	public IResultProcessor getResultProcessor() {
		if (tmqlResultProcessor == null) {
			tmqlResultProcessor = new TmqlResultProcessor(runtime);
		}
		return tmqlResultProcessor;
	}

}
