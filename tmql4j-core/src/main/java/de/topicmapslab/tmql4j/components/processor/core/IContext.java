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
	 * Returns the current index of iteration.
	 * 
	 * @return the current index
	 */
	public int getCurrentIndex();

}
