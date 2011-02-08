/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.template.grammar.lexical;

import de.topicmapslab.tmql4j.grammar.lexical.Token;

/**
 * A represents the return type JTMQR
 * 
 * @author Sven Krosse
 *
 */
public class JTMQROS extends Token {

	public static final String TOKEN = "JTMQROS";
	
	/**
	 * {@inheritDoc}
	 */
	public String getLiteral() {
		return TOKEN;
	}

}
