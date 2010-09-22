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
import de.topicmapslab.tmql4j.interpreter.model.context.IInitialContext;

/**
 * Special task definition of initialization tasks of the TMQL engine. During
 * the initialization the environment map will be created  and the initial context will be
 * instantiated.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IInitializationTask extends IProcessingTask {

	/**
	 * The result of initialization task will be an instance of initial context.
	 * The context contains all predefined variables and the predefined
	 * environment map.
	 * 
	 * @return the result of initialization process.
	 * @throws TMQLRuntimeException
	 *             thrown if task isn't done yet
	 */
	public IInitialContext getInitialContext() throws TMQLRuntimeException;

}
