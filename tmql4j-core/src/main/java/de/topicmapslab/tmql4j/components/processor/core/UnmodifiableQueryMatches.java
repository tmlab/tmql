/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.core;

import java.util.Collection;
import java.util.Map;

import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * @author Sven Krosse
 * @since 2.7.0
 */
public class UnmodifiableQueryMatches extends QueryMatches {

	private static UnmodifiableQueryMatches instance;

	/**
	 * @param runtime
	 * @throws TMQLRuntimeException
	 */
	private UnmodifiableQueryMatches() throws TMQLRuntimeException {
		super(null);
	}

	/**
	 * {@inheritDoc}
	 */
	public void add(Collection<Map<String, Object>> tuples) {
		throw new UnsupportedOperationException("Unmodifable query matches!");
	}

	/**
	 * {@inheritDoc}
	 */
	public void add(Map<String, Object> tuple) {
		throw new UnsupportedOperationException("Unmodifable query matches!");
	}

	/**
	 * {@inheritDoc}
	 */
	public void add(QueryMatches queryMatches) throws TMQLRuntimeException {
		throw new UnsupportedOperationException("Unmodifable query matches!");
	}

	/**
	 * {@inheritDoc}
	 */
	public void addAll(Collection<QueryMatches> queryMatches) {
		throw new UnsupportedOperationException("Unmodifable query matches!");
	}

	/**
	 * {@inheritDoc}
	 */
	public void addNegation(Collection<Map<String, Object>> negations) throws TMQLRuntimeException {
		throw new UnsupportedOperationException("Unmodifable query matches!");
	}

	/**
	 * {@inheritDoc}
	 */
	public void addNegation(QueryMatches negation) throws TMQLRuntimeException {
		throw new UnsupportedOperationException("Unmodifable query matches!");
	}

	/**
	 * {@inheritDoc}
	 */
	public void addOrigin(String origin, String mapping) {
		throw new UnsupportedOperationException("Unmodifable query matches!");
	}

	/**
	 * {@inheritDoc}
	 */
	public void setOrigins(Map<String, String> origins) {
		throw new UnsupportedOperationException("Unmodifable query matches!");
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeAll(QueryMatches matches) throws TMQLRuntimeException {
		throw new UnsupportedOperationException("Unmodifable query matches!");
	}

	/**
	 * Returns an empty query match
	 * @return an unmodifiable query matches instance
	 * @since 2.7.0
	 */
	public static QueryMatches emptyMatches() {
		if (instance == null) {
			instance = new UnmodifiableQueryMatches();
		}
		return instance;
	}

}
