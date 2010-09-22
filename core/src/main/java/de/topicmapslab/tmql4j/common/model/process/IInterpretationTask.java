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
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;

/**
 * Special task definition of the interpretation task. The interpretation task
 * takes place after the parsing process and interprets the current TMQL parser
 * tree to get the results from underlying backend.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IInterpretationTask extends IProcessingTask {

	/**
	 * The result of {@link IInterpretationTask} will be an instance of
	 * {@link QueryMatches} which has to transform into an instance of
	 * {@link IResultSet} during the result processing task.
	 * 
	 * @return the overall query matches of the encapsulating query
	 * @throws TMQLRuntimeException
	 *             thrown if the process is not done yet
	 */
	public QueryMatches getResult() throws TMQLRuntimeException;

}
