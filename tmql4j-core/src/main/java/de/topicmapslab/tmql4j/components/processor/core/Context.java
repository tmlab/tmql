/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.core;

import java.util.Collections;
import java.util.Map;

import de.topicmapslab.tmql4j.components.processor.ITmqlProcessor;
import de.topicmapslab.tmql4j.query.IQuery;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class Context implements IContext {

	private final IQuery query;
	private QueryMatches context;
	private int iterationIndex = -1;
	private Map<String, Object> currentTuple;
	private Object currentNode;
	private boolean transitive;
	private Map<String, String> prefixes;
	private final ITmqlProcessor processor;

	/**
	 * constructor
	 * 
	 * @param processor
	 *            the TMQL processor
	 * @param query
	 *            the handled query
	 */
	public Context(ITmqlProcessor processor, IQuery query) {
		this.processor = processor;
		this.query = query;
	}

	/**
	 * constructor
	 * 
	 * @param clone
	 *            the clone
	 */
	public Context(IContext clone) {
		this.processor = clone.getTmqlProcessor();
		this.query = clone.getQuery();
		this.context = clone.getContextBindings();
		this.currentTuple = clone.getCurrentTuple();
		this.iterationIndex = clone.getCurrentIndex();
		this.currentNode = clone.getCurrentNode();
		this.transitive = clone.isTransitive();
		this.prefixes = clone.getPrefixes();
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

	/**
	 * 
	 * {@inheritDoc}
	 */
	public Object getCurrentNode() {
		return currentNode;
	}

	/**
	 * @param currentNode
	 *            the currentNode to set
	 */
	public void setCurrentNode(Object currentNode) {
		this.currentNode = currentNode;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public boolean isTransitive() {
		return transitive;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public void setTransitive(boolean transitive) {
		this.transitive = transitive;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPrefix(String qiri) {
		if (prefixes == null) {
			return null;
		}
		return prefixes.get(qiri);
	}

	/**
	 * {@inheritDoc}
	 */
	public Map<String, String> getPrefixes() {
		if (prefixes == null) {
			return Collections.emptyMap();
		}
		return Collections.unmodifiableMap(prefixes);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPrefix(String qiri, String reference) {
		if (prefixes == null) {
			prefixes = HashUtil.getHashMap();
		}
		prefixes.put(qiri, reference);
	}

	/**
	 * {@inheritDoc}
	 */
	public ITmqlProcessor getTmqlProcessor() {
		return processor;
	}
	
}
