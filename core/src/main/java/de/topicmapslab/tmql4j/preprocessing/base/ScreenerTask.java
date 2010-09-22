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
import de.topicmapslab.tmql4j.preprocessing.core.moduls.TMQLScreener;
import de.topicmapslab.tmql4j.preprocessing.model.IScreener;

/**
 * Special pre-processing task to realize the screening of a TMQL query.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ScreenerTask extends ProcessingTaskImpl implements
		IPreprocessingTask {

	/**
	 * the internal screener instance
	 */
	private final IScreener screener;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param query
	 *            the query to screen
	 */
	public ScreenerTask(final TMQLRuntime runtime, final IQuery query) {
		/*
		 * try to initialize runtime properties
		 */
		TMQLRuntimeProperties properties = runtime.getProperties();
		/*
		 * initialize the screener instance
		 */
		if (properties == null) {
			screener = new TMQLScreener(query);
		} else {
			screener = properties.getScreenerImplementation(query);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void doRun() throws TMQLRuntimeException {
		screener.screen();
	}

	/**
	 * {@inheritDoc}
	 */
	public IQuery getResult() throws TMQLRuntimeException {
		if (isFinish()) {
			return screener.getScreenedQuery();
		}
		throw new TMQLRuntimeException(
				"Process not finished, please use run() method first.");
	}

}
