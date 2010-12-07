/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.results;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.util.CollectionsUtility;

/**
 * @author Sven Krosse
 * 
 */
public class ProjectionUtils {
	/**
	 * the Logger
	 */
	private static Logger logger = LoggerFactory.getLogger(ProjectionUtils.class.getSimpleName());

	/**
	 * Utility method to create a set of result tuples from the given query
	 * matches. The query tuples wont be reduced to a two-dimensional result.
	 * 
	 * @param matches
	 *            the query matches
	 * @return the result tuples
	 */
	public static List<List<Object>> asNDimensional(QueryMatches matches) {
		List<List<Object>> results = new LinkedList<List<Object>>();
		List<String> keys = matches.getOrderedKeys();
		for (Map<String, Object> tuple : matches) {
			List<Object> current = new LinkedList<Object>();
			for ( String key : keys){
				current.add(tuple.get(key));
			}
			results.add(current);
		}
		return results;
	}
	
	/**
	 * Utility method to create a set of result tuples from the given query
	 * matches. The query tuples will be reduced to a two-dimensional result.
	 * 
	 * @param matches
	 *            the query matches
	 * @return the result tuples
	 */
	public static List<List<Object>> asTwoDimensional(QueryMatches matches) {
		List<List<Object>> results = new LinkedList<List<Object>>();
		List<String> keys = matches.getOrderedKeys();
		for (Map<String, Object> tuple : matches) {
			List<Object> current = new LinkedList<Object>();
			asTwoDimensional(tuple, keys, current, results);
		}
		return results;
	}

	/**
	 * Utility method to create a set of result tuples from the given query
	 * tuple. The query tuple will be reduced to a two-dimensional result.
	 * 
	 * @param tuple
	 *            the tuple
	 * @param keys
	 *            the keys
	 * @param current
	 *            the current result tuple
	 * @param results
	 *            the overall results
	 */
	@SuppressWarnings("unchecked")
	public static void asTwoDimensional(Map<String, Object> tuple, List<String> keys, List<Object> current, List<List<Object>> results) {
		/*
		 * get current key
		 */
		final String key = keys.get(0);
		/*
		 * get next iteration lists
		 */
		List<String> keys_ = new LinkedList<String>(keys);
		keys_.remove(key);

		Object value = tuple.get(key);
		if (value == null) {
			logger.warn("Missing value for key " + key + ". Tuple will be ignored");
			return;
		}

		/*
		 * value is a collection
		 */
		if (value instanceof Collection<?>) {
			for (Object val : CollectionsUtility.asTwoDimension((Collection<Object>) value)) {
				List<Object> current_ = new LinkedList<Object>(current);
				current_.add(val);
				if (keys_.isEmpty()) {
					results.add(current_);
				} else {
					asTwoDimensional(tuple, keys_, current_, results);
				}
			}
		} else {
			List<Object> current_ = new LinkedList<Object>(current);
			current_.add(value);
			if (keys_.isEmpty()) {
				results.add(current_);
			} else {
				asTwoDimensional(tuple, keys_, current_, results);
			}
		}
	}

}
