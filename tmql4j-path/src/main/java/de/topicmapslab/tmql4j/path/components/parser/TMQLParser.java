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
import de.topicmapslab.tmql4j.components.parser.ParserImpl;
import de.topicmapslab.tmql4j.components.parser.ParserTreeImpl;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
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
public final class TMQLParser extends ParserImpl {

	/**
	 * @param lexer
	 */
	public TMQLParser(ILexer lexer) {
		super(lexer);
	}

	/**
	 * {@inheritDoc}
	 */
	protected ParserTreeImpl getParserTreeInstance(ITMQLRuntime runtime, IQuery query, ILexer lexer) {
		return new TmqlParserTree(runtime, query, lexer);
	}
}
