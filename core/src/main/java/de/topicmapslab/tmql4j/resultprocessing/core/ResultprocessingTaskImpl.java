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
package de.topicmapslab.tmql4j.resultprocessing.core;

import de.topicmapslab.tmql4j.common.context.TMQLRuntimeProperties;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.process.ProcessingTaskImpl;
import de.topicmapslab.tmql4j.common.model.process.IInterpretationTask;
import de.topicmapslab.tmql4j.common.model.process.IResultprocessingTask;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.resultprocessing.base.ResultProcessorImpl;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.resultprocessing.model.IResult;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultProcessor;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;

/**
 * Base implementation of a {@link IResultprocessingTask}. The result processing
 * task takes places after the interpretation task {@link IInterpretationTask}
 * and transforms the query matches into a result set ( {@link IResultSet} )
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ResultprocessingTaskImpl extends ProcessingTaskImpl implements
		IResultprocessingTask {

	/**
	 * the internal instance of result processor
	 */
	private final IResultProcessor processor;
	/**
	 * the query matches
	 */
	private final QueryMatches matches;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param matches
	 *            the matches to transform
	 */
	public ResultprocessingTaskImpl(final ITMQLRuntime runtime,
			QueryMatches matches) {
		this.matches = matches;
		/*
		 * try to load properties
		 */
		TMQLRuntimeProperties properties = runtime.getProperties();
		/*
		 * create default result processor instance
		 */
		if (properties == null) {
			processor = new ResultProcessorImpl(runtime, SimpleResultSet.class);
		}
		/*
		 * create instance of result processor from property
		 */
		else {
			processor = properties.getResultProcessorImplementation();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void doRun() throws TMQLRuntimeException {
		processor.proceed(matches);
	}

	/**
	 * {@inheritDoc}
	 */
	public <T extends IResult> IResultSet<T> getResults()
			throws TMQLRuntimeException {
		if (isFinish()) {
			return processor.getResultSet();
		}
		throw new TMQLRuntimeException(
				"process not finished, please use run() method first.");
	};

}
