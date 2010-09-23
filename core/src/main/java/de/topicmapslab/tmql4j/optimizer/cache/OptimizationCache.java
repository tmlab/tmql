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

package de.topicmapslab.tmql4j.optimizer.cache;

import java.util.Map;
import java.util.Set;

import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;

/**
 * Class definition of a cache containing the optimized bindings of
 * interpreters. The bindings will be add after execute a variable binding
 * optimizer.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class OptimizationCache {

	/**
	 * singleton instance of the cache
	 */
	private static OptimizationCache optimizationCache;

	/**
	 * internal map to cache the optimized bindings
	 */
	private final Map<IExpressionInterpreter<?>, Map<String, Set<?>>> internalCache;

	/**
	 * private and hidden constructor
	 */
	private OptimizationCache() {
		internalCache = HashUtil.getHashMap();
	}

	/**
	 * Method to create or get the singleton instance of the optimization cache.
	 * 
	 * @return the singleton instance
	 */
	public static OptimizationCache getOptimizationCache() {
		if (optimizationCache == null) {
			optimizationCache = new OptimizationCache();
		}
		return optimizationCache;
	}

	/**
	 * Insert the new binding into the internal cache if it does not exists.
	 * 
	 * @param interpreter
	 *            the interpreter represent the expression using the optimized
	 *            variable bindings
	 * @param variable
	 *            the variable name
	 * @param optimizedBindings
	 *            the optimized bindings of the variable
	 */
	public void cache(IExpressionInterpreter<?> interpreter, String variable,
			Set<Object> optimizedBindings) {
		/*
		 * try to get cached map
		 */
		Map<String, Set<?>> optimized = internalCache.get(interpreter);
		/*
		 * no map found, create new
		 */
		if (optimized == null) {
			optimized = HashUtil.getHashMap();
		}
		/*
		 * add optimized bindings
		 */
		optimized.put(variable, optimizedBindings);
		/*
		 * insert into the cache
		 */
		internalCache.put(interpreter, optimized);
	}

	/**
	 * Method checks if there is already a cached binding for the given
	 * interpreter and variable.
	 * 
	 * @param interpreter
	 *            the interpreter to check
	 * @param variable
	 *            the variable to check
	 * @return <code>true</code> if cache contains the combination of given
	 *         interpreter and variable<code>false</code> otherwise.
	 */
	public boolean isCached(final IExpressionInterpreter<?> interpreter,
			final String variable) {
		if (internalCache.containsKey(interpreter)) {
			return internalCache.get(interpreter).containsKey(variable);
		}
		return false;
	}

	/**
	 * Method return the cached bindings of a specific variable and interpreter.
	 * 
	 * @param interpreter
	 *            the interpreter as first key of bindings
	 * @param variable
	 *            the variable as second key of bindings
	 * @return the cached bindings and never <code>null</code>. If no entries
	 *         cached for given value, an empty set will be returned.
	 */
	public Set<?> getCachedOptimization(
			final IExpressionInterpreter<?> interpreter, final String variable) {
		if (isCached(interpreter, variable)) {
			return internalCache.get(interpreter).get(variable);
		}
		return HashUtil.getHashSet();
	}

}
