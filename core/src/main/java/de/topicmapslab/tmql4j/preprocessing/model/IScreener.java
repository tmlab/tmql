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
 * Interface definition of a screener. The screener remove comments, spaces and
 * other unnecessary content from the given query.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IScreener {

	/**
	 * Starts the internal screening process. The screener remove all comments
	 * and spaces from the internal query and create a new cleaned query.
	 * 
	 * @throws TMQLRuntimeException
	 *             thrown if the query cannot be cleaned
	 */
	void screen() throws TMQLRuntimeException;

	/**
	 * Method returns the result of screening process.
	 * 
	 * @return the screened query
	 */
	IQuery getScreenedQuery();

}
