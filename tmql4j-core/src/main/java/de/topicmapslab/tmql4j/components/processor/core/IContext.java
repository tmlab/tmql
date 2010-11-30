/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.core;

import java.util.Map;

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
	 * Returns the current index of iteration.
	 * 
	 * @return the current index
	 */
	public int getCurrentIndex();

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

}
