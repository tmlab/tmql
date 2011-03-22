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
package de.topicmapslab.tmql4j.components.processor.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.topicmapslab.tmql4j.components.processor.core.ProjectionQueryMatches.ProjectionQueryMatch;
import de.topicmapslab.tmql4j.components.processor.results.model.ProjectionUtils;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.CollectionsUtility;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Class definition of a container of all querying results of the TMQL querying
 * process. Each expression interpreter create a least one instance of this
 * class as its interpretation results and put it on the top of the variable
 * stack.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class QueryMatches implements Iterable<Map<String, Object>> {

	/**
	 * 
	 */
	private static final String AT_LEAST_ONE_OF_THE_GIVEN_VARIABLES_IS_MISSED_CANNOT_GROUP_BY_NON_EXISTING_VARIABLES = "At least one of the given variables is missed! Cannot group by non existing variables!";
	/**
	 * internal sequence of tuples
	 */
	private final List<Map<String, Object>> matches;
	/**
	 * special variable using for content which is not bind to a specific
	 * variable, like the results of path-expressions
	 */
	private static final String nonScopedVariable = "$$$$$";
	/**
	 * internal container of known non-matching tuples
	 */
	private QueryMatches negation = null;
	/**
	 * internal flag representing the current state of the query matches
	 */
	private boolean multiple = true;

	/**
	 * the internal runtime reference creating this query match
	 */
	private final ITMQLRuntime runtime;

	/**
	 * map containing some origin variable names before mapping
	 */
	private Map<String, String> origins;

	/**
	 * map to store alias of columns
	 */
	private Map<Integer, String> columnAlias;

	/**
	 * constructor to create a new empty instance
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * 
	 * @throws TMQLRuntimeException
	 *             thrown if internal tuple sequence cannot be instantiate
	 */
	public QueryMatches(ITMQLRuntime runtime) throws TMQLRuntimeException {
		this.runtime = runtime;
		this.matches = HashUtil.getList();
		multiple = false;
	}

	/**
	 * base constructor to create a new tuple sequence containing the given
	 * matches
	 * <p>
	 * Constructor will create a copy of the given {@link QueryMatches}.
	 * </p>
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param queryMatches
	 *            the {@link QueryMatches} to add
	 * @throws TMQLRuntimeException
	 *             thrown if combination fails or if internal tuple sequence
	 *             cannot be instantiate
	 */
	public QueryMatches(ITMQLRuntime runtime, QueryMatches queryMatches) throws TMQLRuntimeException {
		this.matches = HashUtil.getList();
		this.runtime = runtime;
		multiple = false;
		add(queryMatches);
	}

	/**
	 * base constructor to create a new tuple sequence containing the given
	 * matches
	 * <p>
	 * Constructor will create a new instance by combine each tuple with the
	 * tuple of the other {@link QueryMatches} identify by their index.
	 * </p>
	 * <p>
	 * For example the query matches {{A1},{A2}} and {{B1},{B2}} will be
	 * combined to {{A1,B1},{A2,B2}}.
	 * </p>
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param queryMatches
	 *            the {@link QueryMatches} to add
	 * @throws TMQLRuntimeException
	 *             thrown if combination fails or if internal tuple sequence
	 *             cannot be instantiate
	 */
	public QueryMatches(ITMQLRuntime runtime, QueryMatches... queryMatches) throws TMQLRuntimeException {
		this(runtime, Arrays.asList(queryMatches));
	}

	/**
	 * base constructor to create a new tuple sequence containing the given
	 * matches
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param matches
	 *            a sequence of tuples to add
	 * @throws TMQLRuntimeException
	 *             thrown if combination fails or if internal tuple sequence
	 *             cannot be instantiate
	 */
	public QueryMatches(ITMQLRuntime runtime, List<Map<String, Object>> matches) throws TMQLRuntimeException {
		this.runtime = runtime;
		this.matches = HashUtil.getList();
		multiple = false;
		add(matches);
	}

	/**
	 * base constructor to create a new tuple sequence containing the given
	 * matches
	 * <p>
	 * Constructor will create a new instance by combine each tuple with the
	 * tuple of the other {@link QueryMatches} identify by their index.
	 * </p>
	 * <p>
	 * For example the query matches {{A1},{A2}} and {{B1},{B2}} will be
	 * combined to {{A1,B1},{A2,B2}}.
	 * </p>
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param queryMatches
	 *            the {@link QueryMatches} to add
	 * @throws TMQLRuntimeException
	 *             thrown if combination fails or if internal tuple sequence
	 *             cannot be instantiate
	 */
	public QueryMatches(ITMQLRuntime runtime, Collection<QueryMatches> queryMatches) throws TMQLRuntimeException {
		this.runtime = runtime;
		this.matches = HashUtil.getList();
		this.origins = HashUtil.getHashMap();
		multiple = false;
		addAll(queryMatches);
	}

	/**
	 * Method checks if the current instance contains at least one tuple.
	 * 
	 * @return <code>true</code> if the query matches are empty,
	 *         <code>false</code> otherwise.
	 */
	public boolean isEmpty() {
		return matches.isEmpty();
	}

	/**
	 * Method return the internal sequence of tuples containing the results of
	 * the represented interpretation step.
	 * 
	 * @return a sequence of matching tuples
	 */
	public List<Map<String, Object>> getMatches() {
		return matches;
	}

	/**
	 * Method add a new sequence of tuples to the end of the internal sequence.
	 * 
	 * 
	 * @param queryMatches
	 *            the {@link QueryMatches} containing the tuple-sequence
	 * @throws TMQLRuntimeException
	 *             thrown if given matches are <code>null</code>
	 * @see QueryMatches#add(Collection)
	 */
	public void add(QueryMatches queryMatches) throws TMQLRuntimeException {
		if (matches == null) {
			throw new TMQLRuntimeException("new QueryMatches can not be null");
		}
		if (this.origins == null) {
			this.origins = HashUtil.getHashMap();
		}
		add(queryMatches.getMatches());
		this.origins.putAll(queryMatches.getOrigins());
	}

	/**
	 * Adding all the given query matches to the internal store map
	 * 
	 * @param queryMatches
	 *            the query matches
	 */
	public void addAll(Collection<QueryMatches> queryMatches) {
		Collection<QueryMatches> content = HashUtil.getHashSet(queryMatches);
		/*
		 * add it self if necessary
		 */
		if (!isEmpty()) {
			content.add(this);
		}

		/*
		 * create empty list for combinations
		 */
		List<Map<String, Object>> tuples = HashUtil.getList();
		/*
		 * create empty maps for tuples and origins
		 */
		Map<String, Object> tuple = HashUtil.getHashMap();
		Map<String, String> origins = HashUtil.getHashMap();
		/*
		 * calculate union
		 */
		addAll(content, tuple, origins, tuples);
		/*
		 * set new matches
		 */
		this.matches.clear();
		this.matches.addAll(tuples);
		this.setOrigins(origins);

	}

	/**
	 * Internal method to handle next iteration of combination
	 * 
	 * @param queryMatches
	 *            the query matches for next iteration
	 * @param tuple
	 *            the tuple
	 * @param origins
	 *            the overall origins
	 * @param tuples
	 *            the overall tuples
	 */
	private void addAll(Collection<QueryMatches> queryMatches, Map<String, Object> tuple, Map<String, String> origins, List<Map<String, Object>> tuples) {
		if (queryMatches.isEmpty()) {
			return;
		}
		/*
		 * get one query match and clone existing set
		 */
		QueryMatches queryMatch = queryMatches.iterator().next();
		Collection<QueryMatches> queryMatches_ = HashUtil.getHashSet(queryMatches);
		queryMatches_.remove(queryMatch);
		/*
		 * add all origins
		 */
		origins.putAll(queryMatch.getOrigins());
		/*
		 * iterate over tuples
		 */
		for (Map<String, Object> thisTuple : queryMatch) {
			/*
			 * get new tuple
			 */
			Map<String, Object> tuple_ = HashUtil.getHashMap(tuple);
			tuple_.putAll(thisTuple);
			/*
			 * do more iteration
			 */
			if (queryMatches_.isEmpty()) {
				tuples.add(tuple_);
			} else {
				addAll(queryMatches_, tuple_, origins, tuples);
			}
		}
	}

	/**
	 * Method add a new sequence of tuples to the end of the internal sequence.
	 * 
	 * @param tuples
	 *            the sequence of tuples to add *
	 * 
	 * @see QueryMatches#add(Map)
	 */
	public void add(Collection<Map<String, Object>> tuples) {
		for (Map<String, Object> tuple : tuples) {
			add(tuple);
		}
	}

	/**
	 * Method add a tuple to the end of the internal sequence.
	 * 
	 * @param tuple
	 *            the tuple to add
	 */
	public void add(Map<String, Object> tuple) {
		if (isTupleAllowed(tuple)) {
			this.matches.add(tuple);
		}
	}

	/**
	 * Method converts the given collection of values to a sequence of tuples.
	 * During the iteration over all values, each value of the given set will be
	 * transformed to a tuple by binding to the non-scoped variable $$$$. *
	 * 
	 * @param sequence
	 *            the value sequence to transform
	 * @throws TMQLRuntimeException
	 *             thrown if {@link QueryMatches} contains values
	 * @see QueryMatches#convertToTuples(Collection, String)
	 */
	public void convertToTuples(Collection<?> sequence) throws TMQLRuntimeException {
		convertToTuples(sequence, getNonScopedVariable());
	}

	/**
	 * Method converts the given collection of values to a sequence of tuples.
	 * During the iteration over all values, each value of the given set will be
	 * transformed to a tuple by binding to the given variable name.
	 * 
	 * @param sequence
	 *            the value sequence to transform
	 * @param variable
	 *            the variable to bind
	 * @throws TMQLRuntimeException
	 *             thrown if {@link QueryMatches} contains values
	 */
	public void convertToTuples(Collection<?> sequence, final String variable) throws TMQLRuntimeException {
		if (!isEmpty()) {
			throw new TMQLRuntimeException("QueryMatches are not empty");
		}

		for (Object obj : sequence) {
			Map<String, Object> tuple = HashUtil.getHashMap();
			tuple.put(variable, obj);
			add(tuple);
		}

	}

	/**
	 * Combination method to realize a boolean-not operation. The operation
	 * removes all tuples from the sequence which are contained in the given
	 * {@link QueryMatches}.
	 * <p>
	 * For example, if current tuples are {{A,B},{A2,B}} and the given argument
	 * contains {A,B} the result will be {{A2,B}}
	 * 
	 * @param queryMatches
	 *            the non-matching tuple sequence
	 */
	public void not(QueryMatches queryMatches) {
		this.matches.removeAll(queryMatches.getMatches());
	}

	/**
	 * Combination method to realize a boolean-and operation. The operation
	 * removes all tuples from the sequence which are only contained in one
	 * {@link QueryMatches}.
	 * <p>
	 * For example, if current tuples are {{A,B},{A2,B}} and the given argument
	 * contains {A,B} the result will be {{A,B}}
	 * 
	 * @param queryMatches
	 *            the tuple sequence
	 */
	public void conjunction(QueryMatches queryMatches) throws TMQLRuntimeException {

		this.negation = new QueryMatches(runtime);

		/*
		 * extract symmetric keys, other keys are not important
		 */
		Set<String> symmetricKeys = HashUtil.getHashSet();
		symmetricKeys.addAll(getOrderedKeys());
		symmetricKeys.retainAll(queryMatches.getOrderedKeys());

		/*
		 * if only non-scoped variable is contained, both are equal
		 */
		if (symmetricKeys.size() == 1 && symmetricKeys.contains(getNonScopedVariable())) {
			// NOTHING TO DO
		}
		/*
		 * otherwise project to topic set
		 */
		else {
			symmetricKeys.remove(getNonScopedVariable());
			Set<Map<String, Object>> set = HashUtil.getHashSet();

			ProjectionQueryMatches projectionA = new ProjectionQueryMatches(this, symmetricKeys);
			ProjectionQueryMatches projectionB = new ProjectionQueryMatches(queryMatches, symmetricKeys);

			for (Map<String, Object> projectedTupleA : projectionA) {
				if (projectionB.contains(projectedTupleA)) {
					ProjectionQueryMatch projectionQueryMatchA = (ProjectionQueryMatch) projectedTupleA;
					ProjectionQueryMatch projectionQueryMatchB = (ProjectionQueryMatch) projectionB.get(projectionB.indexOf(projectedTupleA));
					for (Map<String, Object> tupleA : projectionQueryMatchA.getOrigins()) {
						for (Map<String, Object> tupleB : projectionQueryMatchB.getOrigins()) {
							Map<String, Object> combine = HashUtil.getHashMap();
							combine.putAll(tupleA);
							combine.putAll(tupleB);
							set.add(combine);
						}
					}
				}
			}
			/*
			 * reset values
			 */
			this.matches.clear();
			add(set);
		}

	}

	/**
	 * Combination method to realize a boolean-or operation.
	 * 
	 * @param queryMatches
	 *            the tuple sequence
	 */
	public void disjunction(QueryMatches queryMatches) throws TMQLRuntimeException {

		this.negation = new QueryMatches(runtime);

		/*
		 * extract symmetric keys, other keys are not important
		 */
		Set<String> symmetricKeys = HashUtil.getHashSet();
		symmetricKeys.addAll(getOrderedKeys());
		symmetricKeys.retainAll(queryMatches.getOrderedKeys());

		Set<Map<String, Object>> set = HashUtil.getHashSet();

		ProjectionQueryMatches projectionA = new ProjectionQueryMatches(this, symmetricKeys);
		ProjectionQueryMatches projectionB = new ProjectionQueryMatches(queryMatches, symmetricKeys);

		for (Map<String, Object> projectedTupleA : projectionA) {
			ProjectionQueryMatch projectionQueryMatchA = (ProjectionQueryMatch) projectedTupleA;
			if (projectionB.contains(projectedTupleA)) {
				ProjectionQueryMatch projectionQueryMatchB = (ProjectionQueryMatch) projectionB.get(projectionB.indexOf(projectedTupleA));
				for (Map<String, Object> tupleA : projectionQueryMatchA.getOrigins()) {
					for (Map<String, Object> tupleB : projectionQueryMatchB.getOrigins()) {
						Map<String, Object> combine = HashUtil.getHashMap();
						combine.putAll(tupleA);
						combine.putAll(tupleB);
						set.add(combine);
					}
				}
			} else {
				set.addAll(projectionQueryMatchA.getOrigins());
			}
		}

		/*
		 * add reverse tuples not contained by the left side
		 */
		for (Map<String, Object> projectedTupleB : projectionB) {
			ProjectionQueryMatch projectionQueryMatchB = (ProjectionQueryMatch) projectedTupleB;
			if (!projectionA.contains(projectedTupleB)) {
				set.addAll(projectionQueryMatchB.getOrigins());
			}
		}

		/*
		 * reset values
		 */
		this.matches.clear();
		add(set);

		// matches.addAll(queryMatches.getMatches());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Map<String, Object>> iterator() {
		return matches.iterator();
	}

	/**
	 * Method returns the name of the non-scoped variable of the
	 * {@link QueryMatches} which is used to bind variable-independent values.
	 * 
	 * @return the internal non-scoped variable
	 */
	public static String getNonScopedVariable() {
		return nonScopedVariable;
	}

	/**
	 * Method extract the value of the non-scoped variable from each tuple and
	 * add it to a temporary sequence. The method project each tuple to the
	 * non-scoped variable.
	 * 
	 * @return a sequence of all values of the non-scoped variable
	 * @throws TMQLRuntimeException
	 *             thrown if projection fails
	 * @see QueryMatches#getPossibleValuesForVariable(String)
	 */
	public List<Object> getPossibleValuesForVariable() throws TMQLRuntimeException {
		return getPossibleValuesForVariable(getNonScopedVariable());
	}

	/**
	 * Method extract the value of the given variable from each tuple and add it
	 * to a temporary sequence. The method project each tuple to the given
	 * variable variable.
	 * 
	 * @param variable
	 *            the name of the variable for projection
	 * 
	 * @return a sequence of all values of the given variable
	 * @throws TMQLRuntimeException
	 *             thrown if projection fails
	 */
	public List<Object> getPossibleValuesForVariable(final String variable) throws TMQLRuntimeException {
		/*
		 * create temporary sequence
		 */
		List<Object> sequence = HashUtil.getList();
		;

		/*
		 * iterate over all tuples
		 */
		Iterator<Map<String, Object>> iterator = iterator();
		while (iterator.hasNext()) {
			/*
			 * get current tuple
			 */
			Map<String, Object> tuple = iterator.next();
			/*
			 * extract value if exists
			 */
			if (tuple.containsKey(variable)) {
				sequence.add(tuple.get(variable));
			} else if (getOrigin(variable) != null) {
				sequence.add(tuple.get(origins.get(variable)));
			}
		}
		return sequence;
	}

	/**
	 * Method extract all tuples between the given index range and create a new
	 * instance of {@link QueryMatches} containing all values of this
	 * selection-window.
	 * 
	 * @param fromIndex
	 *            begin index
	 * @param upperIndex
	 *            the upper index
	 * @return a new {@link QueryMatches} instance containing the selection
	 *         window
	 * @throws TMQLRuntimeException
	 *             thrown if fromIndex is out of bounds
	 */
	public QueryMatches select(long fromIndex, long upperIndex) throws TMQLRuntimeException {
		/*
		 * clean first index if necessary
		 */
		long begin = fromIndex;
		if (fromIndex < 0) {
			begin = 0;
		}
		long end = upperIndex;
		if (upperIndex < 0) {
			end = 0;
		} else if (end < begin) {
			end = begin;
		}

		/*
		 * if index is [ 0 .. length ] return this
		 */
		if (begin == 0 && end >= matches.size()) {
			return this;
		} else {
			QueryMatches newMatch = new QueryMatches(runtime);
			for (long index = begin; index < end && index < matches.size(); index++) {
				newMatch.add(matches.get((int) index));
			}
			newMatch.setOrigins(origins);
			return newMatch;
		}
	}

	/**
	 * Method transform the current instance to a new {@link QueryMatches}
	 * instance containing each tuple only one times.
	 * 
	 * @return the unified query matches
	 * @throws TMQLRuntimeException
	 *             thrown if operation failed
	 */
	public QueryMatches unify() throws TMQLRuntimeException {
		List<Map<String, Object>> unified = HashUtil.getList();
		for (Map<String, Object> map : getMatches()) {
			if (!unified.contains(map)) {
				unified.add(map);
			}
		}
		return new QueryMatches(runtime, unified);
	}

	/**
	 * Method count the tuples which represent a projection of the current
	 * instance. The method will return a numeric value representing the number
	 * of tuples which representing the count of tuples which are equal after
	 * projection.
	 * 
	 * @param pattern
	 *            the tuple which will be counted
	 * @param mapping
	 *            a optional mapping containing a mapping between variable names
	 * @param countedVariable
	 *            the variable to count
	 * @return a numeric value as count of tuple equality
	 * @throws TMQLRuntimeException
	 *             thrown if count-operation fails
	 */
	public long count(Map<String, Object> pattern, final Map<String, String> mapping, final String countedVariable) throws TMQLRuntimeException {
		long count = 0;
		/*
		 * create temporary tuple sequence containing all known tuples
		 */
		Set<Map<String, Object>> knownMatchingTuples = HashUtil.getHashSet();
		/*
		 * iterate over internal tuples
		 */
		for (Map<String, Object> tuple : matches) {
			/*
			 * check if tuple matches the pattern to check
			 */
			if (CollectionsUtility.containsAll(tuple, pattern, mapping)) {
				/*
				 * create temporary tuple
				 */
				Map<String, Object> matchingTuple = HashUtil.getHashMap();
				/*
				 * map variable names
				 */
				for (String key : pattern.keySet()) {
					if (mapping.containsKey(key)) {
						key = mapping.get(key);
					}
					matchingTuple.put(key, tuple.get(key));
				}
				/*
				 * add counted variable
				 */
				matchingTuple.put(countedVariable, tuple.get(countedVariable));
				/*
				 * count
				 */
				if (!knownMatchingTuples.contains(matchingTuple)) {
					knownMatchingTuples.add(matchingTuple);
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * Method returns a list of order variables contained in the current
	 * instance. The method only returns the set of variables of the first tuple
	 * and order them by name.
	 * 
	 * @return an ordered list of variables
	 */
	public List<String> getOrderedKeys() {
		if (!isEmpty()) {
			return CollectionsUtility.getOrderedKeys(getMatches().get(0));
		}
		return Collections.emptyList();
	}

	/**
	 * Method checks if the current tuple is a valid tuple in context of the
	 * TMQL draft. The method checks if the given tuple contains protected
	 * variables and check if their values are non-equal.
	 * 
	 * <p>
	 * Protected variables are variable with the same name except the number of
	 * post-fixed primes. The current TMQL draft contains the restriction that
	 * these variable cannot be bind to the same value in the same tuple.
	 * </p>
	 * 
	 * @param tuple
	 *            the tuple to check
	 * @return <code>true</code> if the tuple is valid, <code>false</code> if it
	 *         contains protected variables with the same value.
	 */
	private final boolean isTupleAllowed(Map<String, Object> tuple) {
		for (String key : tuple.keySet()) {
			if (key.endsWith("'")) {
				final String reduction = key.substring(0, key.indexOf("'"));
				if (tuple.containsKey(reduction) && tuple.get(key).equals(tuple.get(reduction))) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Method returns the size of the internal tuple sequence.
	 * 
	 * @return a non-negative value as size of the tuple sequence
	 */
	public int size() {
		return getMatches().size();
	}

	/**
	 * The method extract the values of the non-scoped variable and create a new
	 * tuple sequence containing this values bind to the given variable name.
	 * 
	 * @param renaming
	 *            the new variable name of the non-scoped variable
	 * @return the sequence containing a copy of the values
	 * @throws TMQLRuntimeException
	 *             thrown if transformation fails.
	 * @see QueryMatches#extractAndRenameBindingsForVariable(String, String)
	 */
	public QueryMatches extractAndRenameBindingsForVariable(final String renaming) throws TMQLRuntimeException {
		return extractAndRenameBindingsForVariable(getNonScopedVariable(), renaming);
	}

	/**
	 * The method extract the values of the variable identified by the first
	 * argument and create a new tuple sequence containing this values bind to
	 * the variable name given as second argument.
	 * 
	 * @param variable
	 *            the variable name to extract
	 * @param renaming
	 *            the new variable name of the variable
	 * @return the sequence containing a copy of the values
	 * @throws TMQLRuntimeException
	 *             thrown if transformation fails.
	 */
	public QueryMatches extractAndRenameBindingsForVariable(final String variable, final String renaming) throws TMQLRuntimeException {
		QueryMatches matches = new QueryMatches(runtime);
		matches.convertToTuples(getPossibleValuesForVariable(variable), renaming);
		matches.addOrigin(variable, renaming);
		return matches;
	}

	/**
	 * method return the internal tuple sequence of non-matching tuples. The
	 * internal tuple sequence will be created during the execution of
	 * boolean-operations but isn't complete in context of the whole topic map.
	 * 
	 * @return a tuple sequence containing the non-matching tuples
	 * @throws TMQLRuntimeException
	 *             throw if instantiation of new {@link QueryMatches} fails
	 */
	public QueryMatches getNegation() throws TMQLRuntimeException {
		if (negation == null) {
			negation = new QueryMatches(runtime);
		}
		return negation;
	}

	/**
	 * Method is used to add at new tuples to the internal sequence of
	 * non-matching tuples.
	 * 
	 * @param negation
	 *            the new sequence of non-matching tuples
	 * @throws TMQLRuntimeException
	 *             thrown if instantiation of new {@link QueryMatches} fails
	 * @see QueryMatches#addNegation(QueryMatches)
	 */
	public void addNegation(QueryMatches negation) throws TMQLRuntimeException {
		addNegation(negation.getMatches());
	}

	/**
	 * * Method is used to add at new tuples to the internal sequence of
	 * non-matching tuples.
	 * 
	 * @param negations
	 *            the new sequence of non-matching tuples
	 * @throws TMQLRuntimeException
	 *             thrown if instantiation of new {@link QueryMatches} fails
	 * 
	 */
	public void addNegation(Collection<Map<String, Object>> negations) throws TMQLRuntimeException {
		if (negation == null) {
			this.negation = new QueryMatches(runtime);
		}
		for (Map<String, Object> tuple : negations) {
			this.negation.add(tuple);
		}
	}

	/**
	 * Method checks if the given tuple is contained in the internal tuple
	 * sequence.
	 * <p>
	 * The method is redirected to {@link Collection#contains(Object)}
	 * </p>
	 * 
	 * @param tuple
	 *            the tuple to check
	 * @return <code>true</code> if the tuple is contained, <code>false</code>
	 *         otherwise.
	 */
	public boolean contains(Map<String, Object> tuple) {
		return matches.contains(tuple);
	}

	/**
	 * Method returns the index of the current tuple of it is contained in the
	 * current instance.
	 * <p>
	 * The method is redirected to {@link List#indexOf(Object)}.
	 * </p>
	 * 
	 * @param tuple
	 *            the tuple to look for
	 * @return the index in this tuple sequence of the first tuple matching the
	 *         given tuple, or -1 if this sequence does not contain the given
	 *         tuple.
	 */
	public int indexOf(Map<String, Object> tuple) {
		return matches.indexOf(tuple);
	}

	/**
	 * Method returns the indexes of the current tuple of it is contained in the
	 * current instance.
	 * <p>
	 * Please note, if the tuple sequence is unique it will return a set only
	 * containing the result of the method {@link QueryMatches#indexOf(Map)}.
	 * </p>
	 * 
	 * @param tuple
	 *            the tuple to look for
	 * @return a set of the index of the given tuple
	 */
	public Set<Integer> indexesOf(Map<String, Object> tuple) {
		Set<Integer> indexes = HashUtil.getHashSet();
		for (int index = 0; index < size(); index++) {
			if (get(index).equals(tuple)) {
				indexes.add(index);
				if (!multiple) {
					break;
				}
			}
		}
		return indexes;
	}

	/**
	 * Method returns the tuple at the given index if it exists.
	 * <p>
	 * The method will redirected to {@link List#get(int)}.
	 * </p>
	 * 
	 * @param index
	 *            the index of the tuple
	 * @return the tuple at the given position and never <code>null</code>.
	 * @throws IndexOutOfBoundsException
	 *             thrown if the index is out of range (index < 0 || index >=
	 *             size()).
	 */
	public Map<String, Object> get(int index) throws IndexOutOfBoundsException {
		return matches.get(index);
	}

	/**
	 * Method retain all tuple contained by the internal sequence which will be
	 * contained by the given tuple sequence two.
	 * 
	 * @param matches
	 *            the tuple sequence to retain
	 * @throws TMQLRuntimeException
	 *             thrown if operation fails
	 */
	public void retainAll(QueryMatches matches) throws TMQLRuntimeException {
		QueryMatches results = new QueryMatches(runtime);

		/*
		 * iterate over internal sequence
		 */
		for (Map<String, Object> tupleA : this) {
			/*
			 * iterate over given sequence
			 */
			for (Map<String, Object> tupleB : matches) {

				boolean satisfy = true;
				/*
				 * check if each entry of internal tuple are contained in given
				 * tuple
				 */
				for (Entry<String, Object> entry : tupleA.entrySet()) {
					if (tupleB.containsKey(entry.getKey())) {
						if (!tupleB.get(entry.getKey()).equals(entry.getValue())) {
							satisfy = false;
							break;
						}
					}
				}
				if (satisfy) {
					/*
					 * check if each entry of given tuple are contained in
					 * internal tuple
					 */
					for (Entry<String, Object> entry : tupleB.entrySet()) {
						if (tupleA.containsKey(entry.getKey())) {
							if (!tupleA.get(entry.getKey()).equals(entry.getValue())) {
								satisfy = false;
								break;
							}
						}
					}
				}
				/*
				 * add new tuple as combination of both tuples
				 */
				if (satisfy) {
					Map<String, Object> tuple = HashUtil.getHashMap();
					tuple.putAll(tupleB);
					tuple.putAll(tupleA);
					results.add(tuple);
				}
			}
		}

		/*
		 * reset values
		 */
		this.matches.clear();
		add(results);
	}

	/**
	 * Method removes all tuples from the internal sequence which are contained
	 * in the given tuple sequence.
	 * <p>
	 * Method will be redirected to {@link Collection#removeAll(Collection)}.
	 * </p>
	 * 
	 * @param matches
	 *            the tuples to remove
	 * @throws TMQLRuntimeException
	 *             thrown if deletion fails
	 */
	public void removeAll(QueryMatches matches) throws TMQLRuntimeException {
		QueryMatches results = new QueryMatches(runtime);

		/*
		 * iterate over internal tuples
		 */
		for (Map<String, Object> tupleA : this) {
			/*
			 * iterate over given tuples
			 */
			for (Map<String, Object> tupleB : matches) {

				boolean satisfy = true;
				/*
				 * check if each entry of internal tuple are contained in given
				 * tuple
				 */
				for (Entry<String, Object> entry : tupleA.entrySet()) {
					if (tupleB.containsKey(entry.getKey())) {
						if (tupleB.get(entry.getKey()).equals(entry.getValue())) {
							satisfy = false;
							break;
						}
					}
				}
				if (satisfy) {
					/*
					 * check if each entry of given tuple are contained in
					 * internal tuple
					 */
					for (Entry<String, Object> entry : tupleB.entrySet()) {
						if (tupleA.containsKey(entry.getKey())) {
							if (tupleA.get(entry.getKey()).equals(entry.getValue())) {
								satisfy = false;
								break;
							}
						}
					}
				}
				if (!satisfy) {
					results.add(tupleA);
				}
			}
		}

		/*
		 * reset values
		 */
		this.matches.removeAll(results.getMatches());

	}

	/**
	 * Method renames the variable given by first argument with the name
	 * specified by the second argument.
	 * 
	 * @param variable
	 *            the variable to rename
	 * @param renaming
	 *            the new variable name
	 * @return the new {@link QueryMatches} with the renamed tuples
	 * @throws TMQLRuntimeException
	 *             thrown if the instantiation of the new query match fails or
	 *             the variable names are invalid
	 */
	public QueryMatches renameVariable(final String variable, final String renaming) throws TMQLRuntimeException {

		/*
		 * get ordered keys
		 */
		Collection<String> keys = getOrderedKeys();

		/*
		 * check if new variable-name is already part of internal sequence
		 */
		if (keys.contains(renaming)) {
			throw new TMQLRuntimeException("target variable is already used.");
		}

		/*
		 * check if variable-name is part of internal sequence
		 */
		if (!keys.contains(variable)) {
			throw new TMQLRuntimeException("source variable is unknown.");
		}

		/*
		 * create new instance
		 */
		QueryMatches renamed = new QueryMatches(runtime);
		/*
		 * iterate over all tuples
		 */
		for (Map<String, Object> tuple : this) {
			Map<String, Object> renamedTuple = HashUtil.getHashMap();
			/*
			 * iterate over entries
			 */
			for (Entry<String, Object> entry : tuple.entrySet()) {
				/*
				 * check if variable shall be renamed
				 */
				if (entry.getKey().equalsIgnoreCase(variable)) {
					renamedTuple.put(renaming, entry.getValue());
				} else {
					renamedTuple.put(entry.getKey(), entry.getValue());
				}
			}
			renamed.add(renamedTuple);
		}

		/*
		 * store renaming
		 */
		renamed.addOrigin(variable, renaming);
		return renamed;
	}

	/**
	 * Method create multiple copies of the internal tuple and expand the size
	 * of the tuple sequence to the given value. The method only works if the
	 * internal sequence contains exactly one tuple, otherwise an exception will
	 * be thrown.
	 * 
	 * @param size
	 *            the new size of the tuple sequence
	 * @return a new instance containing multiple copies of the tuple
	 * @throws TMQLRuntimeException
	 *             thrown if instantiation fails.
	 */
	public final QueryMatches cloneTupleAndExpandTo(int size) throws TMQLRuntimeException {
		if (size() != 1) {
			throw new TMQLRuntimeException("size of source matches has to be one");
		}

		QueryMatches clone = new QueryMatches(runtime);
		for (int index = 0; index < size; index++) {
			clone.add(getMatches().get(0));
		}
		return clone;
	}

	/**
	 * Method is used to order the internal tuples by the values of the given
	 * variable.
	 * 
	 * @param ascending
	 *            the order direction
	 * @return the ordered query match
	 * @throws TMQLRuntimeException
	 *             thrown if ordering failed
	 */
	public final QueryMatches orderBy(final boolean ascending) throws TMQLRuntimeException {
		return orderBy(ascending, getNonScopedVariable());
	}

	/**
	 * Method is used to order the internal tuples by the values of the given
	 * variable.
	 * 
	 * @param ascending
	 *            the order direction
	 * @param variable
	 * @return the ordered query match
	 * @throws TMQLRuntimeException
	 *             thrown if ordering failed
	 */
	public final QueryMatches orderBy(final boolean ascending, final String variable) throws TMQLRuntimeException {
		QueryMatches matches = new QueryMatches(runtime, this);

		Collections.sort(matches.matches, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {

				Object v1 = o1.get(variable);
				Object v2 = o2.get(variable);

				if (v1 == null) {
					return ascending ? -1 : 1;
				}
				if (v2 == null) {
					return ascending ? 1 : -1;
				}
				int com;
				/*
				 * is a number
				 */
				if (v1 instanceof Number && v2 instanceof Number) {
					com = (int) Math.round(((Number) v1).doubleValue() - ((Number) v1).doubleValue());
				}
				/*
				 * is anything else
				 */
				else {
					com = v1.toString().compareTo(v2.toString());
				}
				return ascending ? com : com * -1;
			}
		});

		return matches;
	}

	/**
	 * Getter of the internal TMQL4J runtime reference
	 * 
	 * @return the reference
	 */
	protected ITMQLRuntime getRuntime() {
		return runtime;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return matches.toString();
	}

	/**
	 * Returns the origin name of the variable
	 * 
	 * @param var
	 *            the variable name
	 * @return the origin name if exists
	 */
	public String getOrigin(final String var) {
		if (origins == null) {
			return null;
		}
		return this.origins.get(var);
	}

	/**
	 * Set the internal mapping table to the given one
	 * 
	 * @param origins
	 *            the origins to set
	 */
	public void setOrigins(Map<String, String> origins) {
		this.origins = origins;
	}

	/**
	 * Store a new mapping between the given keys
	 * 
	 * @param origin
	 *            the origins to set @
	 */
	public void addOrigin(final String origin, final String mapping) {
		if (this.origins == null) {
			this.origins = HashUtil.getHashMap();
		}
		this.origins.put(origin, mapping);
	}

	/**
	 * Returns the whole mapping.
	 * 
	 * @return the origins the mapping of origin to projected keys
	 */
	public Map<String, String> getOrigins() {
		return HashUtil.getHashMap(origins);
	}

	/**
	 * Returns the first tuple value for the non-scoped variable
	 * 
	 * @return the first tuple value or <code>null</code> if it does not exists
	 */
	public Object getFirstValue() {
		return getFirstValue(nonScopedVariable);
	}

	/**
	 * Returns the first tuple value for the given variable
	 * 
	 * @param variable
	 *            the variable
	 * @return the first tuple value or <code>null</code> if it does not exists
	 */
	public Object getFirstValue(final String variable) {
		if (isEmpty()) {
			return null;
		}
		Map<String, Object> tuple = get(0);
		return tuple.get(variable);
	}

	/**
	 * Method checks if the given value is contained by at least one tuple
	 * 
	 * @param value
	 *            the value
	 * @return <code>true</code> if at least one tuple contains this value,
	 *         <code>false</code> otherwise
	 */
	public boolean contains(Object value) {
		for (Map<String, Object> tuple : getMatches()) {
			if (tuple.values().contains(value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns an empty query match
	 * 
	 * @return an unmodifiable query matches instance
	 * @since 2.7.0
	 */
	public static QueryMatches emptyMatches() {
		return UnmodifiableQueryMatches.emptyMatches();
	}

	/**
	 * Creates a query match containing all the given values. The method create
	 * one tuple for each given value and bound them to the non-scoped variable.
	 * If the value is a map it would be added without any modification.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param values
	 *            the values
	 * @return the query match
	 * @since 2.7.0
	 */
	public static QueryMatches asQueryMatchNS(ITMQLRuntime runtime, Object... values) {
		return asQueryMatch(runtime, getNonScopedVariable(), values);
	}

	/**
	 * Creates a query match containing all the given values. The method create
	 * one tuple for each given value and bound them to the given variable. If
	 * the value is a map it would be added without any modification.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param variable
	 *            the variable
	 * @param values
	 *            the values
	 * @return the query match
	 * @since 2.7.0
	 */
	@SuppressWarnings("unchecked")
	public static QueryMatches asQueryMatch(ITMQLRuntime runtime, String variable, Object... values) {
		QueryMatches match = new QueryMatches(runtime);
		for (Object o : values) {
			if (o instanceof Map<?, ?>) {
				match.add((Map<String, Object>) o);
			} else if (o instanceof Collection<?>) {
				for (Object o_ : (Collection<?>) o) {
					Map<String, Object> tuple = HashUtil.getHashMap();
					tuple.put(variable, o_);
					match.add(tuple);
				}
			} else {
				Map<String, Object> tuple = HashUtil.getHashMap();
				tuple.put(variable, o);
				match.add(tuple);
			}
		}
		return match;
	}

	/**
	 * Utility method to handle group by expression for the current query
	 * matches.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param variables
	 *            a set of variables, the matches should group by
	 * @return the grouped query matches
	 * @since 3.0.0
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches groupBy(final ITMQLRuntime runtime, final Set<String> variables) throws TMQLRuntimeException {
		List<String> orderedKeys = getOrderedKeys();
		if (!orderedKeys.containsAll(variables)) {
			throw new TMQLRuntimeException(AT_LEAST_ONE_OF_THE_GIVEN_VARIABLES_IS_MISSED_CANNOT_GROUP_BY_NON_EXISTING_VARIABLES);
		}
		/*
		 * group by with all variables -> this
		 */
		if (orderedKeys.size() == variables.size()) {
			return this;
		}
		/*
		 * store first position of key
		 */
		List<Map<String, Object>> positions = HashUtil.getList();
		/*
		 * store projections for a specific key
		 */
		Map<Map<String, Object>, Map<String, Object>> projections = HashUtil.getHashMap();

		/*
		 * iterate over content
		 */
		for (Map<String, Object> tuple : ProjectionUtils.asTwoDimensionalMap(this)) {
			/*
			 * extract current key
			 */
			Map<String, Object> key = HashUtil.getHashMap();
			for (String variable : variables) {
				key.put(variable, tuple.get(variable));
			}
			/*
			 * create projection or load existing
			 */
			Map<String, Object> projection = projections.get(key);
			if (projection == null) {
				projection = HashUtil.getHashMap();
				projections.put(key, projection);
				positions.add(key);
			}
			/*
			 * group values by by given variables
			 */
			for (String variable : tuple.keySet()) {
				if (!variables.contains(variable)) {
					Object value = tuple.get(variable);
					Collection<Object> values = (Collection<Object>) projection.get(variable);
					/*
					 * if value is null
					 */
					if (value == null) {
						/*
						 * if no other value for projection exists add null
						 * value
						 */
						if (values == null) {
							projection.put(variable, null);
						}
					}
					/*
					 * value is not null
					 */
					else {
						/*
						 * create collection if not exists
						 */
						if (values == null) {
							values = HashUtil.getList();
							projection.put(variable, values);
						}
						/*
						 * add value if not already contained
						 */
						if (!values.contains(value)) {
							values.add(value);
						}
					}

				}
			}
		}
		/*
		 * create new query match
		 */
		QueryMatches matches = new QueryMatches(runtime);
		for (Map<String, Object> key : positions) {
			Map<String, Object> projection = projections.get(key);
			projection.putAll(key);
			matches.add(projection);
		}
		return matches;
	}

	/**
	 * Setting the column alias before serialize to JTMQR
	 * 
	 * @param columnAlias
	 *            the columnAlias to set
	 */
	public void setColumnAlias(Map<Integer, String> columnAlias) {
		this.columnAlias = columnAlias;
	}

	/**
	 * Getting the set aliases
	 * 
	 * @return the columnAlias
	 */
	public Map<Integer, String> getColumnAlias() {
		return columnAlias;
	}
}
