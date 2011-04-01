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
package de.topicmapslab.tmql4j.grammar.lexical;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;


/**
 * Interface definition of a language specific token. Each class implementing
 * this interface represents exactly one type of language-specific token.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IToken {

	/**
	 * Method checks if the token can be represented by the given string literal
	 * 
	 * @param literal
	 *            the string literal
	 * @param runtime
	 *            the contained runtime
	 * @return <code>true</code> if the literal can be represented by this
	 *         expression type
	 */
	public boolean isToken(final ITMQLRuntime runtime, final String literal);

	/**
	 * Method returns the string representation of the current language token
	 * 
	 * @return the literal
	 */
	public String getLiteral();

}
