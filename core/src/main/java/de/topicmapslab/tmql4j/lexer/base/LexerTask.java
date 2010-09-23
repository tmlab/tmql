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
package de.topicmapslab.tmql4j.lexer.base;

import de.topicmapslab.tmql4j.common.context.TMQLRuntimeProperties;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.process.ProcessingTaskImpl;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.model.process.ILexerTask;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.lexer.core.TMQLLexer;
import de.topicmapslab.tmql4j.lexer.model.ILexer;

/**
 * Base implementation of a lexical scanning task of the TMQL engine.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class LexerTask extends ProcessingTaskImpl implements ILexerTask {

	/**
	 * the internal lexical scanner
	 */
	private final ILexer lexer;

	/**
	 * base constructor to create a new lexical scanning task and initialize the
	 * internal lexer.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param query
	 *            the query
	 */
	public LexerTask(final TMQLRuntime runtime, final IQuery query) {
		/*
		 * try to read initialize runtime properties
		 */
		TMQLRuntimeProperties properties = runtime.getProperties();
		/*
		 * failed to load properties
		 */
		if (properties == null) {
			lexer = new TMQLLexer(runtime, query);
		}
		/*
		 * read lexer implementation class from runtime properties
		 */
		else {
			lexer = properties.getLexerImplementation(query);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void doRun() throws TMQLRuntimeException {
		lexer.execute();
	}

	/**
	 * {@inheritDoc}
	 */
	public ILexer getResult() {
		return lexer;
	}

}
