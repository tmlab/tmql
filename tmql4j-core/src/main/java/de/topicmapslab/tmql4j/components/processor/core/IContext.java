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
import java.util.Map;

import de.topicmapslab.tmql4j.components.processor.ITmqlProcessor;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * @author Sven Krosse
 * 
 */
public interface IContext {

	/**
	 * Returns the query currently proceeded.
	 * 
	 * @return the query
	 */
	public IQuery getQuery();

	/**
	 * Returns the context relevant bindings.
	 * 
	 * @return the bindings or <code>null</code> if no binding is relevant for
	 *         current execution
	 */
	public QueryMatches getContextBindings();

	/**
	 * Returns the current tuple of iteration
	 * 
	 * @return the current tuple of iteration
	 */
	public Map<String, Object> getCurrentTuple();

	/**
	 * Returns the current node of iteration
	 * 
	 * @return the current node of iteration
	 */
	public Object getCurrentNode();

	/**
	 * Returns the current index of iteration in context of the sequence of
	 * tuples.
	 * 
	 * @return the current index
	 */
	public int getCurrentIndexInSequence();

	/**
	 * Returns the current index of iteration in context of the tuple.
	 * 
	 * @return the current index
	 */
	public int getCurrentIndexInTuple();

	/**
	 * Checks if the current context is transitive
	 * 
	 * @return <code>true</code> if the pragma definition for transitivity was
	 *         set, <code>false</code> otherwise
	 */
	public boolean isTransitive();

	/**
	 * @param transitive
	 *            the transitive to set
	 */
	public void setTransitive(boolean transitive);

	/**
	 * Returns the known prefix for the given reference
	 * 
	 * @param qiri
	 *            the QIRI of the prefix
	 * @return the stored reference of the prefix or <code>null</code>
	 */
	public String getPrefix(final String qiri);

	/**
	 * Add a new prefix to the map of known prefixes
	 * 
	 * @param qiri
	 *            the QIRI of the prefix
	 * @param reference
	 *            the reference
	 */
	public void setPrefix(final String qiri, final String reference);

	/**
	 * Returns the all known prefixes
	 * 
	 * @return a unmodifiable map of all known prefixes
	 */
	public Map<String, String> getPrefixes();

	/**
	 * Returns the TMQL processor proceeding this query execution
	 * 
	 * @return the query processor
	 */
	public ITmqlProcessor getTmqlProcessor();

	/**
	 * Returns the output stream or <code>null</code>
	 * 
	 * @return the stream
	 */
	public OutputStream getOutputStream();

	/**
	 * Method to store an custom feature in an internal map, for example proceed
	 * by a user defined pragma.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 * @since 3.1.0
	 */
	public void setCustomFeature(final String key, final Object value);

	/**
	 * Returns the user defined feature or <code>null</code> if the feature not
	 * set.
	 * 
	 * @param <T>
	 *            the type of return value
	 * @param key
	 *            the key
	 * @return the feature value or <code>null</code>
	 * @since 3.1.0
	 */
	public <T extends Object> T getCustomFeature(final String key);

	/**
	 * Returns all user defined feature or <code>null</code> if no features set.
	 * 
	 * @return the features or <code>null</code>
	 * @since 3.1.0
	 */
	public Map<String, Object> getCustomFeatures();

}
