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
package de.topicmapslab.tmql4j.interpreter.core.base;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.utility.HashUtil;

/**
 * Extension of {@link QueryMatches} representing a projection of an origin
 * {@link QueryMatches}. An instance of this class containing a sub-sequence of
 * tuples and the reference to the origin tuple sequence.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ProjectionQueryMatches extends QueryMatches {

	/**
	 * constructor to create a new instance
	 * 
	 * @param origin
	 *            the origin tuple sequence
	 * @param variables
	 *            a set of variables as specification of the projection
	 * @throws TMQLRuntimeException
	 *             thrown if projection fails
	 * @see ProjectionQueryMatches#projection(QueryMatches, Set)
	 */
	public ProjectionQueryMatches(QueryMatches origin, Set<String> variables)
			throws TMQLRuntimeException {
		super(origin.getRuntime());
		projection(origin, variables);
	}

	/**
	 * Internal method to create the projection of the origin tuple sequence. At
	 * first the method extract all asymmetric keys of the given
	 * {@link QueryMatches}. Asymmetric keys are all keys which are not
	 * contained in the given variable set. After all each tuple will be reduced
	 * to the set of symmetric keys by removing each asymmetric key. Because of
	 * multiple instances of the same projected-tuple, each projection stores
	 * the origin tuples to enable the re-projection to the original one.
	 * 
	 * @param origin
	 *            the origin tuple sequence
	 * @param variables
	 *            the set of variables to project
	 * @throws TMQLRuntimeException
	 *             thrown if at least one variable is not contained by the given
	 *             {@link QueryMatches}.
	 */
	private void projection(final QueryMatches origin,
			final Set<String> variables) throws TMQLRuntimeException {
		if (!origin.getOrderedKeys().containsAll(variables)) {
			throw new TMQLRuntimeException(
					"projection values has to be contained by origin maps.");
		}
		/*
		 * check if all keys are symmetric
		 */
		if (origin.getOrderedKeys().size() == variables.size()) {
			/*
			 * add each tuple at new instance
			 */
			for (Map<String, Object> tuple : origin) {
				add(new ProjectionQueryMatch(tuple, tuple));
			}
		}
		/*
		 * at least one key is asymmetric
		 */
		else {
			/*
			 * iterate over tuples
			 */
			for (Map<String, Object> tuple : origin) {
				/*
				 * create new tuple
				 */
				Map<String, Object> copy = HashUtil.getHashMap();
				/*
				 * add values
				 */
				for (String variable : variables) {
					copy.put(variable, tuple.get(variable));
				}
				/*
				 * check if projected tuple is contained
				 */
				if (contains(copy)) {
					/*
					 * add new reference to the internal set of original tuples
					 */
					ProjectionQueryMatch projectionQueryMatch = (ProjectionQueryMatch) get(indexOf(copy));
					projectionQueryMatch.addOrigin(tuple);
				} else {
					/*
					 * create new projection tuple
					 */
					add(new ProjectionQueryMatch(copy, tuple));
				}
			}
		}
	}

	/**
	 * Internal class representing the content of {@link ProjectionQueryMatches}
	 * . A projection tuple is the reduction of a tuple of a
	 * {@link QueryMatches} and contains the projected values and a set of
	 * references of the original tuples.
	 * 
	 * @author Sven Krosse
	 * @email krosse@informatik.uni-leipzig.de
	 * 
	 */
	class ProjectionQueryMatch extends HashMap<String, Object> {

		private static final long serialVersionUID = 1L;
		/**
		 * A list of original tuples
		 */
		List<Map<String, Object>> origins = new LinkedList<Map<String, Object>>();

		/**
		 * constructor to create a new instance
		 * 
		 * @param tuple
		 *            the projected tuple
		 * @param origin
		 *            the original tuple
		 */
		public ProjectionQueryMatch(Map<String, Object> tuple,
				Map<String, Object> origin) {
			putAll(tuple);
			origins.add(origin);
		}

		/**
		 * Method adds a new reference to the internal set of original tuples.
		 * 
		 * @param origin
		 *            the new original tuple
		 */
		public void addOrigin(Map<String, Object> origin) {
			origins.add(origin);
		}

		/**
		 * Method returns the list of original tuples
		 * 
		 * @return the internal list of original tuples represented by this
		 *         projected-tuple
		 */
		public List<Map<String, Object>> getOrigins() {
			return origins;
		}

	}
}
