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
package de.topicmapslab.tmql4j.preprocessing.base;

import de.topicmapslab.tmql4j.common.context.TMQLRuntimeProperties;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.process.ProcessingTaskImpl;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.model.process.IPreprocessingTask;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.preprocessing.core.moduls.TMQLCanonizer;
import de.topicmapslab.tmql4j.preprocessing.model.ICanonizer;

/**
 * Special pre-processing task to realize the substitution of a non-canonical
 * query to the canonical query.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class CanonizerTask extends ProcessingTaskImpl implements
		IPreprocessingTask {

	/**
	 * the internal canonizer instance
	 */
	private final ICanonizer canonizer;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param query
	 *            the query to canonize
	 */
	public CanonizerTask(final TMQLRuntime runtime, IQuery query) {

		/*
		 * try to initialize runtime properties
		 */
		TMQLRuntimeProperties properties = runtime.getProperties();
		/*
		 * create new canonizer
		 */
		if (properties == null) {
			canonizer = new TMQLCanonizer(query);
		} else {
			canonizer = properties.getCanonizerImplementation(query);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void doRun() throws TMQLRuntimeException {
		canonizer.canonize();
	}

	/**
	 * {@inheritDoc}
	 */
	public IQuery getResult() throws TMQLRuntimeException {
		if (isFinish()) {
			return canonizer.getCanonizedQuery();
		}
		throw new TMQLRuntimeException(
				"Process not finished, please used run() method");
	}

}
