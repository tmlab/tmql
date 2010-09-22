/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2009 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.common.model.process;

import de.topicmapslab.tmql4j.parser.core.TMQLParser;
import de.topicmapslab.tmql4j.parser.model.IParser;

/**
 * Special task definition modeling the parsing process. The parsing process
 * tasks place after the lexical scanning ( {@link ILexerTask} ) and transforms
 * the lexical tokens into a tree representation. Each node of the tree
 * represent a special TMQL language production rule.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IParsingTask extends IProcessingTask {

	/**
	 * The result of the parsing process will be an instance of a parser tree
	 * and some additional information encapsulate by the parser instance.
	 * 
	 * @return the instance of the internal parser
	 * @see TMQLParser
	 */
	IParser getResult();

}
