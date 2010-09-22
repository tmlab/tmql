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
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;

/**
 * Abstract modeling of the internal process chain. The interface represents the
 * whole runtime process of the TMQL engine. A process chain contains at least
 * on processing task ( {@link IPreprocessingTask}.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IProcessingTaskChain {

	/**
	 * Method start the execution of contained tasks in the specified order.
	 * 
	 * @throws TMQLRuntimeException
	 *             thrown if a task or a sub-operation of a task failed
	 */
	public void execute() throws TMQLRuntimeException;

	/**
	 * Method provide access to the runtime encapsulate this processing chain
	 * and store all relevant values.
	 * 
	 * @return the encapsulating runtime
	 */
	public TMQLRuntime getTmqlRuntime();

}
