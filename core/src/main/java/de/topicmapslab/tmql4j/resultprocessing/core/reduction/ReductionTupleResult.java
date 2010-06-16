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

import de.topicmapslab.java.tree.Tree;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleTupleResult;


/**
 * Special implementation of a simple tuple result supporting reduction to a
 * two-dimensional table of atomic values.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ReductionTupleResult extends SimpleTupleResult {

	/**
	 * base constructor to create an empty result
	 */
	public ReductionTupleResult() {
		// VOID
	}

	/**
	 * base constructor to create a new result containing the given values
	 * 
	 * @param results
	 *            the values to add
	 */
	public ReductionTupleResult(Collection<Object> results) {
		super(results);
	}

	/**
	 * base constructor to create a new result containing the given values
	 * 
	 * @param results
	 *            the values to add
	 */
	public ReductionTupleResult(Object... results) {
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
	public Collection<? extends Collection<? extends Object>> reduceTo2Dimensions()
			throws UnsupportedOperationException {
		/*
		 * use utility class to generate a tree-structure of all result values
		 */
		Tree tree = new Tree();
		tree.addNodes(getResults());
		/*
		 * return a list of all paths from root to leaf
		 */
		return tree.pathToLeaf();
	}

}
