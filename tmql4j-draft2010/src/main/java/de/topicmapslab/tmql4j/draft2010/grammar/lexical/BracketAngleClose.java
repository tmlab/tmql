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
package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

/**
 * The token of closing angle bracket
 * @author Sven Krosse
 *
 */
public class BracketAngleClose extends Token {

	/**
	 * token
	 */
	private static final String TOKEN = "}";

	/**
	 * {@inheritDoc}
	 */
	public String getLiteral() {
		return TOKEN;
	}

}
