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
package de.topicmapslab.tmql4j.preprocessing.model;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.model.query.IQuery;

/**
 * Interface definition of a canonizer. The canonizer transform the given query
 * from the non-canonical level of the TMQL grammar to the canonical level using
 * term substitutions. If the reduction resolves ambiguousness the pattern wont
 * be transformed.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface ICanonizer {

	/**
	 * Starts the canonization. The query will be transformed to the canonical
	 * level by using term substitutions. During the execution all shortcuts
	 * will be transformed to their expanded pattern.
	 * 
	 * @throws TMQLRuntimeException
	 *             thrown if canonization fails
	 */
	void canonize() throws TMQLRuntimeException;

	/**
	 * Method returns the internal result of canonization. In best case the
	 * result will be a query on canonical level.
	 * 
	 * @return the canonized query
	 */
	IQuery getCanonizedQuery();

}
