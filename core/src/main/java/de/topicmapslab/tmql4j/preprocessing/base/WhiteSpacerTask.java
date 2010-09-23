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
import de.topicmapslab.tmql4j.preprocessing.core.moduls.TMQLWhiteSpacer;
import de.topicmapslab.tmql4j.preprocessing.model.IWhiteSpacer;

/**
 * Special pre-processing task to realize the white-spacing of a TMQL query.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class WhiteSpacerTask extends ProcessingTaskImpl implements
		IPreprocessingTask {

	/**
	 * internal instance of the white-spacer
	 */
	private final IWhiteSpacer spacer;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param query
	 */
	public WhiteSpacerTask(final TMQLRuntime runtime, final IQuery query) {
		/*
		 * try to load runtime properties
		 */
		TMQLRuntimeProperties properties = runtime.getProperties();
		/*
		 * create default instance of white-spacer
		 */
		if (properties == null) {
			spacer = new TMQLWhiteSpacer(runtime, query);
		}
		/*
		 * create instance from property
		 */
		else {
			spacer = properties.getWhiteSpacerImplementation(query);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void doRun() throws TMQLRuntimeException {
		spacer.execute();
	}

	/**
	 * {@inheritDoc}
	 */
	public IQuery getResult() throws TMQLRuntimeException {
		if (isFinish()) {
			return spacer.getTransformedQuery();
		}
		throw new TMQLRuntimeException(
				"Process not finished, please used run() method");
	}

}
