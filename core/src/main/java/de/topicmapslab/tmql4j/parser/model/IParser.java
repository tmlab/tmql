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
package de.topicmapslab.tmql4j.parser.model;

import de.topicmapslab.tmql4j.common.core.exception.TMQLParserException;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;

/**
 * Interface definition of a parser of the TMQL engine. The parser transform the
 * string-represented and language-specific token lists to a tree-representation
 * by interpreting the current TMQL draft. The generated parsing tree
 * representing the structure of the given query split by productions rules.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IParser {

	/**
	 * Method starts the internal parsing process to transform the
	 * string-represented and language-specific token lists to a
	 * tree-representation by interpreting the current TMQL draft. The generated
	 * parsing tree representing the structure of the given query split by
	 * productions rules.
	 * 
	 * @param runtime
	 *            the TMQL runtime
	 * @throws TMQLParserException
	 *             thrown if the generation of the parsing tree fails
	 */
	void parse(ITMQLRuntime runtime) throws TMQLParserException;

	/**
	 * Method return the result of parsing process. The result will be a
	 * tree-structured representation of the origin query. Each node of the tree
	 * represents a independent production rule.
	 * 
	 * @return a parser tree instance
	 */
	IParserTree getParserTree();

}
