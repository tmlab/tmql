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

import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Class definition of a cache saving unsuccessful bindings for expressions.
 * Expressions store the unsuccessful bindings after execution.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class UnsuccesfulBindingsCache {

	/**
	 * singleton instance of the cache
	 */
	private static UnsuccesfulBindingsCache unsuccesfulBindingsCache;

	/**
	 * the internal map to store unsuccessful bindings
	 */
	private final Map<IExpression, Map<String, Object>> internalCache;

	/**
	 * private and hidden constructor
	 */
	private UnsuccesfulBindingsCache() {
		internalCache = HashUtil.getHashMap();
	}

	/**
	 * Static method to get the singleton instance of the cache.
	 * 
	 * @return the singleton instance and never <code>null</code>
	 */
	public static UnsuccesfulBindingsCache getUnsuccesfulBindingsCache() {
		if (unsuccesfulBindingsCache == null) {
			unsuccesfulBindingsCache = new UnsuccesfulBindingsCache();
		}
		return unsuccesfulBindingsCache;
	}

	/**
	 * Method checks if there is already a cached binding for the given
	 * expression and if the given binding is equal.
	 * 
	 * @param expression
	 *            the expression to check
	 * @param bindings
	 *            the binding to check
	 * @return <code>true</code> if stored binding is the same like the given
	 *         one for the given expression, <code>false</code> otherwise.
	 */
	public boolean isCached(IExpression expression, Map<String, Object> bindings) {
		if (internalCache.containsKey(expression)) {
			Map<String, Object> storedBindings = internalCache.get(expression);
			return storedBindings.equals(bindings);
		}
		return false;
	}

	/**
	 * Insert the new binding into the internal cache if it does not exists.
	 * 
	 * @param expression
	 *            the expression report the unsuccessful bindings
	 * @param bindings
	 *            the bindings to add
	 */
	public void cache(IExpression expression, Map<String, Object> bindings) {
		internalCache.put(expression, bindings);
	}
}
