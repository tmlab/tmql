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
 * A token symbolize the re-definition of a new template, which can be used to query
 * 
 * @author Sven Krosse
 *
 */
public class Redefine extends Token {

	public static final String TOKEN = "REDEFINE";
	
	/**
	 * {@inheritDoc}
	 */
	public String getLiteral() {
		return TOKEN;
	}

}
