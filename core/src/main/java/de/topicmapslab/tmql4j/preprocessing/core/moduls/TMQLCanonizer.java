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
package de.topicmapslab.tmql4j.preprocessing.core.moduls;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.preprocessing.core.transformer.NonCanonicalTransformer;
import de.topicmapslab.tmql4j.preprocessing.model.ICanonizer;

/**
 * Base implementation of {@link ICanonizer}. The canonizer transform the given
 * query from the non-canonical level of the TMQL grammar to the canonical level
 * using term substitutions. If the reduction resolves ambiguousness the pattern
 * wont be transformed.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TMQLCanonizer implements ICanonizer {

	/**
	 * the query instance
	 */
	private final IQuery query;
	/**
	 * the canonized query
	 */
	private IQuery canonizedQuery = null;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param query
	 *            the query to canonize
	 */
	public TMQLCanonizer(IQuery query) {
		this.query = query;
	}

	/**
	 * {@inheritDoc}
	 */
	public void canonize() throws TMQLRuntimeException {
		/*
		 * get string representation
		 */
		String canonical = query.toString();

		/*
		 * call module to transform
		 */
		canonical = NonCanonicalTransformer.transform(canonical);

		canonizedQuery = new TMQLQuery(canonical);
	}

	/**
	 * {@inheritDoc}
	 */
	public IQuery getCanonizedQuery() {
		return this.canonizedQuery;
	}

}
