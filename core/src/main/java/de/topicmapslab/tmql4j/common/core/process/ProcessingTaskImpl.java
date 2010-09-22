/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.common.core.process;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.model.process.IProcessingTask;

/**
 * Abstract implementation of {@link IProcessingTask} to implement core methods
 * of all tasks.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public abstract class ProcessingTaskImpl implements IProcessingTask {

	/**
	 * flag specify if current task is running
	 */
	private boolean running = false;
	/**
	 * flag specify if current task is initialized
	 */
	private boolean initialized = false;
	/**
	 * flag specify if current task is finished
	 */
	private boolean finished = false;

	/**
	 * Method checks the internal flag of running state and call the abstract
	 * method {@link ProcessingTaskImpl#doCancel()}. <br />
	 * <br />
	 * 
	 * {@inheritDoc}
	 */
	public void cancel() throws TMQLRuntimeException {
		if (!running) {
			throw new TMQLRuntimeException("Process isn't currently running.");
		}
		running = false;
		doCancel();
	}

	/**
	 * Method checks the internal flag of initialization state and call the
	 * abstract method {@link ProcessingTaskImpl#doInit()}. <br />
	 * <br />
	 * 
	 * {@inheritDoc}
	 */
	public void init() throws TMQLRuntimeException {
		if (initialized) {
			throw new TMQLRuntimeException("Process isn't already initialized.");
		}
		doInit();
		initialized = true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isFinish() {
		return finished;
	}

	/**
	 * Method checks the internal flags of initialization and running state and
	 * call the abstract method {@link ProcessingTaskImpl#doRun()}. <br />
	 * <br />
	 * 
	 * {@inheritDoc}
	 */
	public void run() throws TMQLRuntimeException {
		if (!initialized || running) {
			throw new TMQLRuntimeException(
					"Process isn't initialized or is currently running.");
		}
		running = true;
		doRun();
		running = false;
		finished = true;
	}

	/**
	 * method has to overwrite by the extending classes to implement the
	 * operations to realize the cancel-operation.
	 * 
	 * @see IProcessingTask#cancel()
	 * 
	 * @throws TMQLRuntimeException
	 *             thrown if operation is not supported or operation fails
	 */
	public void doCancel() throws TMQLRuntimeException {
	};

	/**
	 * method has to overwrite by the extending classes to implement the
	 * initialization of the current task.
	 * 
	 * @see IProcessingTask#init()
	 * 
	 * @throws TMQLRuntimeException
	 *             thrown if operation fails
	 */
	public void doInit() throws TMQLRuntimeException {
	};

	/**
	 * method has to overwrite by the extending classes to implement the
	 * execution of current task.
	 * 
	 * @see IProcessingTask#run()
	 * 
	 * @throws TMQLRuntimeException
	 *             thrown if operation fails
	 */
	public void doRun() throws TMQLRuntimeException {
	};
}
