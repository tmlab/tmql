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
package de.topicmapslab.tmql4j.common.model.process;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.resultprocessing.model.IResult;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;

/**
 * Special task definitin modeling the result processing task which takes
 * places after the interpretation task {@link IInterpretationTask} and
 * transforms the query matches into a result set ( {@link IResultSet} )
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IResultprocessingTask extends IProcessingTask {

	/**
	 * The result of result processing task will be an instance of
	 * {@link IResultSet} dependent from the query style and the user-defined
	 * property.
	 * 
	 * @param <T>
	 *            the type of results contained by the result set
	 * @return the instance of result set
	 * @throws TMQLRuntimeException
	 *             thrown if the operation is not done yet
	 */
	<T extends IResult> IResultSet<T> getResults() throws TMQLRuntimeException;

}
