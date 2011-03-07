/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.runtime.module;

import java.util.ArrayList;
import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.module.model.ITokenRegistry;
import de.topicmapslab.tmql4j.exception.TMQLExtensionRegistryException;
import de.topicmapslab.tmql4j.exception.TMQLInitializationException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.lexical.Pragma;
import de.topicmapslab.tmql4j.grammar.lexical.Wildcard;

/**
 * Registry class to handle all tokens of different languages extensions and the
 * core implementation.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public abstract class TokenRegistryImpl implements ITokenRegistry {

	/**
	 * the encapsulated runtime
	 */
	private final ITMQLRuntime runtime;
	/**
	 * a token type store
	 */
	private final List<Class<? extends IToken>> tokenClasses = new ArrayList<Class<? extends IToken>>();
	/**
	 * the token store
	 */
	private final List<IToken> tokens = new ArrayList<IToken>();

	/**
	 * constructor
	 * 
	 * @param runtime
	 *            the encapsulating runtime
	 * @throws TMQLInitializationException
	 *             thrown if initialization failed
	 */
	public TokenRegistryImpl(final ITMQLRuntime runtime) throws TMQLInitializationException {
		this.runtime = runtime;
		register(Wildcard.class);
		register(Pragma.class);
		initialize();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public final void register(final Class<? extends IToken> tokenClass) throws TMQLExtensionRegistryException {
		try {
			register(tokenClass.newInstance());
		} catch (InstantiationException e) {
			throw new TMQLExtensionRegistryException(e);
		} catch (IllegalAccessException e) {
			throw new TMQLExtensionRegistryException(e);
		}
	}

	/**
	 * Registration method to add a new token to the internal store
	 * 
	 * @param token
	 *            the token to add
	 * 
	 */
	@Override
	public final void register(final IToken token) {
		if (!tokenClasses.contains(token.getClass())) {
			tokenClasses.add(token.getClass());
			tokens.add(token);
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public final IToken getTokenByLiteral(final String literal) {
		/*
		 * iterate over tokens
		 */
		for (IToken token : tokens) {
			/*
			 * check if token is known for literal
			 */
			if (token.isToken(runtime, literal)) {
				/*
				 * return token
				 */
				return token;
			}
		}
		/*
		 * return default
		 */
		return getDefaultToken();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public final Class<? extends IToken> getTokenClassByLiteral(final String literal) {
		return getTokenByLiteral(literal).getClass();
	}

	/**
	 * Initialization method
	 */
	protected abstract void initialize();
}
