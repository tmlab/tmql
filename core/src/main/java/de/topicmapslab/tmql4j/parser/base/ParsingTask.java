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
package de.topicmapslab.tmql4j.parser.base;

import de.topicmapslab.tmql4j.common.context.TMQLRuntimeProperties;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.process.ProcessingTaskImpl;
import de.topicmapslab.tmql4j.common.model.process.ILexerTask;
import de.topicmapslab.tmql4j.common.model.process.IParsingTask;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.lexer.model.ILexer;
import de.topicmapslab.tmql4j.parser.core.TMQLParser;
import de.topicmapslab.tmql4j.parser.model.IParser;

/**
 * Base implementation of {@link IParsingTask}. The parsing process tasks place
 * after the lexical scanning ( {@link ILexerTask} ) and transforms the lexical
 * tokens into a tree representation. Each node of the tree represent a special
 * TMQL language production rule.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ParsingTask extends ProcessingTaskImpl implements IParsingTask {

	/**
	 * internal instance of the parser
	 */
	private final IParser parser;
	/**
	 * the TMQL runtime
	 */
	private final ITMQLRuntime runtime;

	/**
	 * base constructor to create new instance
	 * 
	 * @param runtime
	 *            the runtime
	 * @param lexer
	 *            the lexer containing the lexical tokens
	 * 
	 */
	public ParsingTask(final ITMQLRuntime runtime, final ILexer lexer) {
		this.runtime = runtime;
		/*
		 * try to load properties
		 */
		TMQLRuntimeProperties properties = runtime.getProperties();
		/*
		 * create instance of default parser
		 */
		if (properties == null) {
			parser = new TMQLParser(lexer);
		}
		/*
		 * create instance of parser by property
		 */
		else {
			parser = properties.getParserImplementation(lexer);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void doRun() throws TMQLRuntimeException {
		parser.parse(runtime);
	}

	/**
	 * {@inheritDoc}
	 */
	public IParser getResult() {
		return this.parser;
	}

}
