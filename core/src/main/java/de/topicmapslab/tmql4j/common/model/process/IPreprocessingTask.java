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
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.preprocessing.model.IScreener;
import de.topicmapslab.tmql4j.preprocessing.model.IWhiteSpacer;

/**
 * Special task definition representing a pre-processing task. There are three
 * sub-tasks covered by the pre-processing - the screener, the whitespacer and
 * the canonizer. The screener ( {@link IScreener} ) remove all unnecessary
 * content from the query, like comments. The whitespacer {@link IWhiteSpacer}
 * removes unnecessary whitespaces from the query or add some whitespaces to
 * support the lexical scanning. The canonizer reduce the grammar level of the
 * given query to the canonical level.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IPreprocessingTask extends IProcessingTask {

	/**
	 * Method return the result of the pre-processing task.
	 * 
	 * @return the cleaned query
	 * @throws TMQLRuntimeException
	 *             thrown if the process is not done yet
	 */
	public IQuery getResult() throws TMQLRuntimeException;

}
