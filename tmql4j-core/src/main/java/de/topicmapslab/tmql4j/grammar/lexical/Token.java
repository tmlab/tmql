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
 * Abstract base implementation of {@link IToken} realizing the core functionality of each language specific token.
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
public abstract class Token implements IToken {

	/**
	 * {@inheritDoc}
	 */
	public boolean isToken(final ITMQLRuntime runtime, final String literal) {
		return literal.equalsIgnoreCase(getLiteral());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getLiteral();
	}

}
