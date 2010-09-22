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
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.preprocessing.core.transformer.TMQLQueryTransformer;
import de.topicmapslab.tmql4j.preprocessing.model.IWhiteSpacer;

/**
 * Base implementation of a {@link IWhiteSpacer}. The white-spacer removes at
 * first all unnecessary white-spaces. At the second step it tries to detect
 * missing white-spaces to split each language-specific token by a single space.
 * The cleaned query can be handle in a easier way than the origin one.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TMQLWhiteSpacer implements IWhiteSpacer {

	/**
	 * the query instance
	 */
	private final IQuery query;
	/**
	 * the transformed query as the result of execution
	 */
	private IQuery transformedQuery = null;

	/**
	 * the TMQL4J runtime
	 */
	private final ITMQLRuntime runtime;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param query
	 *            the query to transform
	 */
	public TMQLWhiteSpacer(final ITMQLRuntime runtime, IQuery query) {
		this.query = query;
		this.runtime = runtime;
	}

	/**
	 * {@inheritDoc}
	 */
	public void execute() throws TMQLRuntimeException {

		/*
		 * call query transformer
		 */
		transformedQuery = new TMQLQuery(TMQLQueryTransformer.transform(
				runtime, query.toString()));

	}

	/**
	 * {@inheritDoc}
	 */
	public IQuery getTransformedQuery() {
		return transformedQuery;
	}

}
