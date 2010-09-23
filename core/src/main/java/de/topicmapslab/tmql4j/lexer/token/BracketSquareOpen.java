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
package de.topicmapslab.tmql4j.lexer.token;

import de.topicmapslab.tmql4j.lexer.model.Token;

public class BracketSquareOpen extends Token {

	/**
	 * {@inheritDoc}
	 */
	public String getLiteral() {
		return "[";
	}

}
