/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.grammar.lexical;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;


/**
 * A special token represent the wild card of a prepared statement
 * @author Sven Krosse
 *
 */
public class Wildcard extends Token {

	/**
	 * the token
	 */
	public static final String TOKEN = "?";

	/**
	 * {@inheritDoc}
	 */
	public boolean isToken(ITMQLRuntime runtime, String literal) {
		return literal.startsWith(TOKEN);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getLiteral() {
		return TOKEN;
	}

}
