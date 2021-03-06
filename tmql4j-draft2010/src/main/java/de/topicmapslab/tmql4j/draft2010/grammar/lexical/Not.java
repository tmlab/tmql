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
 * The token of the boolean negation NOT
 * 
 * @author Sven Krosse
 * 
 */
public class Not extends Token {

	/**
	 * thet token
	 */
	public static final String TOKEN = "NOT";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLiteral() {
		return TOKEN;
	}

}
