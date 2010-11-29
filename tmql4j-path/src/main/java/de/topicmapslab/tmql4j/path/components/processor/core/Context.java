/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.components.processor.core;

import java.util.Map;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * @author Sven Krosse
 * 
 */
public class Context implements IContext {

	private final IQuery query;
	private QueryMatches context;
	private int iterationIndex = -1;
	private Map<String, Object> currentTuple;

	/**
	 * constructor
	 * 
	 * @param query
	 *            the handled query
	 */
	public Context(IQuery query) {
		this.query = query;
	}

	/**
	 * constructor
	 * 
	 * @param clone
	 *            the clone
	 */
	public Context(IContext clone) {
		this.query = clone.getQuery();
		this.context = clone.getContextBindings();
		this.currentTuple = clone.getCurrentTuple();
		this.iterationIndex = clone.getCurrentIndex();
	}

	/**
	 * Internal method to set the binding
	 * 
	 * @param context
	 *            the context bindings
	 */
	public void setContextBindings(QueryMatches context) {
		this.context = context;
	}

	/**
	 * {@inheritDoc}
	 */
	public IQuery getQuery() {
		return query;
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryMatches getContextBindings() {
		return context;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getCurrentIndex() {
		return iterationIndex;
	}

	/**
	 * @param currentIndex
	 *            the current index
	 */
	public void setCurrentIndex(int currentIndex) {
		this.iterationIndex = currentIndex;
	}

	/**
	 * {@inheritDoc}
	 */
	public Map<String, Object> getCurrentTuple() {
		return currentTuple;
	}

	/**
	 * 
	 * @param currentTuple
	 *            the currentTuple to set
	 */
	public void setCurrentTuple(Map<String, Object> currentTuple) {
		this.currentTuple = currentTuple;
	}

}
