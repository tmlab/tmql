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
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.path.components.lexer.TMQLLexer;
import de.topicmapslab.tmql4j.path.components.parser.TMQLParser;
import de.topicmapslab.tmql4j.path.components.processor.core.Context;
import de.topicmapslab.tmql4j.path.components.processor.results.SimpleResultSet;
import de.topicmapslab.tmql4j.path.components.processor.results.TmqlResultProcessor;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * @author Sven Krosse
 * 
 */
public class TmqlProcessor2007 implements ITmqlProcessor {

	private final ITMQLRuntime runtime;

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

		ILexer lexer = getTmqlLexer(query);
		lexer.execute();

		IParser parser = getTmqlParser(lexer);
		parser.parse(runtime);
		IParserTree tree = parser.getParserTree();

		IContext context = new Context(query);

		QueryMatches results = tree.root().interpret(runtime, context);

		IResultProcessor resultProcessor = getResultProcessor();
		/*
		 * TODO handle flwr xtm and ctm results
		 */
		resultProcessor.proceed(results, SimpleResultSet.class);

		return resultProcessor.getResultSet();
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
		return new TmqlResultProcessor(runtime);
	}

}
