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
import de.topicmapslab.tmql4j.components.processor.results.IResultProcessor;
import de.topicmapslab.tmql4j.components.processor.results.IResultSet;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * @author Sven Krosse
 * 
 */
public interface ITmqlProcessor {

	public IResultSet<?> query(IQuery query);
	
	public IParserTree parse(IQuery query);

	public ILexer getTmqlLexer(IQuery query);

	public IParser getTmqlParser(ILexer lexer);

	public IResultProcessor getResultProcessor();

}
