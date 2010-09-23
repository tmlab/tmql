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

import de.topicmapslab.tmql4j.lexer.core.TMQLLexer;
import de.topicmapslab.tmql4j.lexer.model.ILexer;
import de.topicmapslab.tmql4j.lexer.model.IToken;

/**
 * Special task definition define a lexical scanning process. The lexical
 * scanning tasks place before parsing process and split the given query into
 * known language tokens.
 * 
 * @see IToken
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface ILexerTask extends IProcessingTask {

	/**
	 * The result of the lexical scanning will be a set of defined language
	 * tokens and string patterns encapsulate by the lexer instance.
	 * 
	 * @see TMQLLexer
	 * 
	 * @return the internal instance of TMQL lexer
	 */
	public ILexer getResult();

}
