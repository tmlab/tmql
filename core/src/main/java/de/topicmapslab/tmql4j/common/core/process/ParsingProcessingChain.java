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
package de.topicmapslab.tmql4j.common.core.process;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.parser.model.IParser;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;

public class ParsingProcessingChain extends ProcessingTaskChainImpl {

	public ParsingProcessingChain(TMQLRuntime runtime, IQuery query) {
		super(runtime, query);
	}
	
	@Override
	protected QueryMatches doInterpretationTask(TMQLRuntime runtime,
			IParser parser) throws TMQLRuntimeException {
		return null;
	}
	
	@Override
	protected IResultSet<?> doResultProcessingTask(TMQLRuntime runtime,
			QueryMatches matches) throws TMQLRuntimeException {		
		return null;
	}

}
