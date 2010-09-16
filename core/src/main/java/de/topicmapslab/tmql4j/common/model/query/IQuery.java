/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2009 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.common.model.query;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;

/**
 * Interface definition of a query, which can be handled by the TMQL4 engine or
 * a plug-in. Each implementation of this interface represent a query of a
 * specific language.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IQuery {

	/**
	 * Method returns the internal string representation of the query.
	 * 
	 * @return the query content as string
	 */
	public String getQueryString();

	/**
	 * Returns the querying result stored by the TMQL4J runtime after finishing
	 * the querying process.
	 * 
	 * If the querying process is not finished yet, an exception will be thrown
	 * 
	 * @return the querying results and never <code>null</code>
	 * @throws TMQLRuntimeException
	 *             thrown if querying process not finished yet
	 */
	public IResultSet<?> getResults() throws TMQLRuntimeException;

	/**
	 * Method called by the TMQL runtime after the execution querying process
	 * 
	 * @param runtime
	 *            the runtime calling the method
	 */
	public void beforeQuery(ITMQLRuntime runtime);
	
	/**
	 * Method called by the TMQL runtime after the execution querying process
	 * 
	 * @param runtime
	 *            the runtime calling the method
	 */
	public void afterQuery(ITMQLRuntime runtime);
}
