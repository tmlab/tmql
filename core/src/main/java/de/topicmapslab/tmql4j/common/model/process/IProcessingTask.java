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

/**
 * Interface representing a task of the {@link IProcessingTaskChain}. A task
 * represents only one module of the TMQL engine and encapsulate the all
 * operations and sub-tasks which are necessary to satisfy their job.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IProcessingTask {

	/**
	 * Method is used to initialize the processing task before it can be
	 * execute. <b>Hint:</b> Method has to be called before calling
	 * {@link IProcessingTask#run()}
	 * 
	 * @throws TMQLRuntimeException
	 *             thrown if task is already initialized or the initialization
	 *             fails
	 */
	void init() throws TMQLRuntimeException;

	/**
	 * Method called to execute the internal tasks and operation to realize the
	 * internal job of the specific task. <b>Hint:</b> Method has to be called
	 * after calling {@link IProcessingTask#init()}
	 * 
	 * @throws TMQLRuntimeException
	 *             thrown if the task is not already initialized or the
	 *             execution fails.
	 */
	void run() throws TMQLRuntimeException;

	/**
	 * Method checks the internal state of the task.
	 * 
	 * @return <code>true</code> if the task is finished, <code>false</code>
	 *         otherwise.
	 */
	boolean isFinish();

	/**
	 * Method is called to cancel the execution of this task if it runs. Inherit
	 * classes has to overwrite the method to provide this functionality.
	 * 
	 * @throws TMQLRuntimeException
	 *             thrown if task is currently not running or the operation can
	 *             not canceled.
	 */
	void cancel() throws TMQLRuntimeException;

}
