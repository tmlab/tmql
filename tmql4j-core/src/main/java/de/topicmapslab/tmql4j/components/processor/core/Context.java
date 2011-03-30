/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.core;

import java.io.OutputStream;
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
	private int currentIndexInSequence = -1;
	private int currentIndexInTuple = -1;
	private Map<String, Object> currentTuple;
	private Object currentNode;
	private boolean transitive;
	private Map<String, String> prefixes;
	private final ITmqlProcessor processor;
	private final OutputStream stream;
	private Map<String, Object> features;

	/**
	 * constructor
	 * 
	 * @param processor
	 *            the TMQL processor
	 * @param query
	 *            the handled query
	 */
	public Context(ITmqlProcessor processor, IQuery query) {
		this(processor, query, null);
	}

	/**
	 * constructor
	 * 
	 * @param processor
	 *            the TMQL processor
	 * @param query
	 *            the handled query
	 * @param stream
	 *            the stream
	 */
	public Context(ITmqlProcessor processor, IQuery query, OutputStream stream) {
		this.processor = processor;
		this.query = query;
		this.stream = stream;
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
		this.currentIndexInSequence = clone.getCurrentIndexInSequence();
		this.currentIndexInTuple = clone.getCurrentIndexInTuple();
		this.currentNode = clone.getCurrentNode();
		this.transitive = clone.isTransitive();
		this.prefixes = clone.getPrefixes();
		this.stream = clone.getOutputStream();
		this.features = clone.getCustomFeatures();
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
	@Override
	public IQuery getQuery() {
		return query;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public QueryMatches getContextBindings() {
		return context;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCurrentIndexInSequence() {
		return currentIndexInSequence;
	}

	/**
	 * @param currentIndex
	 *            the current index
	 */
	public void setCurrentIndexInSequence(int currentIndex) {
		this.currentIndexInSequence = currentIndex;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCurrentIndexInTuple() {
		return currentIndexInTuple;
	}

	/**
	 * @param currentIndex
	 *            the current index
	 */
	public void setCurrentIndexInTuple(int currentIndex) {
		this.currentIndexInTuple = currentIndex;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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
	@Override
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
	@Override
	public boolean isTransitive() {
		return transitive;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void setTransitive(boolean transitive) {
		this.transitive = transitive;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPrefix(String qiri) {
		if (prefixes == null) {
			return null;
		}
		return prefixes.get(qiri);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String> getPrefixes() {
		if (prefixes == null) {
			return Collections.emptyMap();
		}
		return Collections.unmodifiableMap(prefixes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPrefix(String qiri, String reference) {
		if (prefixes == null) {
			prefixes = HashUtil.getHashMap();
		}
		prefixes.put(qiri, reference);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ITmqlProcessor getTmqlProcessor() {
		return processor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OutputStream getOutputStream() {
		return stream;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCustomFeature(String key, Object value) {
		if (features == null) {
			features = HashUtil.getHashMap();
		}
		features.put(key, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Object> T getCustomFeature(String key) {
		if (features == null) {
			return null;
		}
		return (T) features.get(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> getCustomFeatures() {
		return features;
	}

}
