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
package de.topicmapslab.tmql4j.optimizer.util;

import de.topicmapslab.tmql4j.common.core.exception.TMQLOptimizationException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntimeFactory;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;

/**
 * Utility class to execute short TMQL queries as possible bindings of a
 * variable. Class is used during the optimization task.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public final class TMQLOptimizerUtility {

	/**
	 * the internal TMQL runtime to execute the given queries
	 */
	private final TMQLRuntime runtime;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the runtime used to execute
	 * @throws TMQLRuntimeException
	 *             thrown if the data-bridge instance cannot be instantiate
	 */
	public TMQLOptimizerUtility(final TMQLRuntime runtime)
			throws TMQLRuntimeException {
		this.runtime = (TMQLRuntime) TMQLRuntimeFactory.newFactory()
				.newRuntime(runtime.getTopicMapSystem(), runtime.getTopicMap());
	}

	/**
	 * Method executes a short TMQL query used during the optimization process.
	 * 
	 * @param query
	 *            the query to execute
	 * @return the results of querying
	 * @throws TMQLOptimizationException
	 *             thrown if execution of given query fails
	 */
	public QueryMatches execute(final String query)
			throws TMQLOptimizationException {
		try {
			runtime.run(new TMQLQuery(query));
			return runtime.getValueStore().getInterpretationResults();
		} catch (TMQLRuntimeException e) {
			throw new TMQLOptimizationException(
					"execution of tmql-query failed!");
		}
	}
}
