/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.runtime.module.model;

import de.topicmapslab.tmql4j.exception.TMQLExtensionRegistryException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;

/**
 * Registry class to handle all tokens of different languages extensions and the
 * core implementation.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface ITokenRegistry {

	/**
	 * Registration method to add a new token to the internal store
	 * 
	 * @param tokenClass
	 *            the class representing the token
	 * @throws TMQLExtensionRegistryException
	 *             throw if instantiation failed
	 */
	public void register(final Class<? extends IToken> tokenClass) throws TMQLExtensionRegistryException;

	/**
	 * Registration method to add a new token to the internal store
	 * 
	 * @param token
	 *            the token to add
	 * 
	 */
	public void register(final IToken token);

	/**
	 * Returns the token representing the given string literal. If no token
	 * matches to the given literal the default token 'ELEMENT' will returned.
	 * 
	 * @param literal
	 *            the string literal
	 * @return the token representing the literal or 'ELEMENT' if no token
	 *         matches.
	 */
	public IToken getTokenByLiteral(final String literal);

	/**
	 * Returns the token class representing the given string literal. If no
	 * token matches to the given literal the default token 'ELEMENT' will
	 * returned.
	 * 
	 * @param literal
	 *            literal the string literal
	 * @return the class of the token representing the literal or 'ELEMENT' if
	 *         no token matches.
	 */
	public Class<? extends IToken> getTokenClassByLiteral(final String literal);

	/**
	 * Returns the default token represent a topic maps item
	 * 
	 * @return the default token
	 */
	public IToken getDefaultToken();
}
