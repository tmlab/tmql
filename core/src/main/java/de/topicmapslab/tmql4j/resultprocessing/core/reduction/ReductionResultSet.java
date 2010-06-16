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
package de.topicmapslab.tmql4j.resultprocessing.core.reduction;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleTupleResult;

/**
 * Special implementation of a simple result set supporting reduction to a
 * two-dimensional table of atomic values.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ReductionResultSet extends SimpleResultSet {

	/**
	 * constructor to create an empty result set
	 */
	public ReductionResultSet() {
		// VOID
	}

	/**
	 * constructor to create a result set containing given results
	 * 
	 * @param results
	 *            the results to add
	 */
	public ReductionResultSet(Collection<SimpleTupleResult> results) {
		super(results);
	}

	/**
	 * constructor to create a result set containing given results
	 * 
	 * @param results
	 *            the results to add
	 */
	public ReductionResultSet(SimpleTupleResult... results) {
		super(results);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canReduceTo2Dimensions() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reduceTo2Dimensions() throws UnsupportedOperationException,
			TMQLRuntimeException {
		/*
		 * create a new list for reduced results
		 */
		List<SimpleTupleResult> results = new LinkedList<SimpleTupleResult>();
		/*
		 * iterate over contained results
		 */
		for (SimpleTupleResult result : getResults()) {
			/*
			 * check if result can be reduced
			 */
			if (result.canReduceTo2Dimensions()
					&& result.getResults().size() > 1) {
				/*
				 * iterate over reduction result
				 */
				for (Collection<? extends Object> c : result
						.reduceTo2Dimensions()) {
					/*
					 * create new tuple result and add values
					 */
					try {
						SimpleTupleResult r = getResultClass().newInstance();
						r.add(c);
						results.add(r);
					} catch (Exception e) {
						throw new TMQLRuntimeException(e);
					}
				}
			}
			/*
			 * result cannot be reduced and will be add as unmodified instance
			 */
			else {
				results.add(result);
			}
		}
		/*
		 * remove old results
		 */
		clear();
		/*
		 * add new results
		 */
		addResults(results);
	}

}
