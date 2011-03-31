/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.majortom.grammar.functions;

import org.tmapi.core.TopicMap;

import de.topicmapslab.majortom.model.core.ITopicMap;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.functions.FunctionImpl;

/**
 * @author Sven Krosse
 * 
 */
public class RemoveDuplicates extends FunctionImpl {

	public static final String RemoveDuplicates = "fn:remove-duplicates";

	/**
	 * {@inheritDoc}
	 */
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, IExpressionInterpreter<?> caller) {
		/*
		 * extract arguments
		 */
		QueryMatches parameters = getParameters(runtime, context, caller);
		/*
		 * check count of variables
		 */
		if (!isExpectedNumberOfParameters(parameters.getOrderedKeys().size())) {
			throw new TMQLRuntimeException(getItemIdentifier() + "() requieres 0 parameters.");
		}
		TopicMap tm = context.getQuery().getTopicMap();
		if (!( tm instanceof ITopicMap)){
			throw new TMQLRuntimeException("The current topic map is not a MaJorToM topic map.");
		}
		((ITopicMap)tm).removeDuplicates();
		return QueryMatches.emptyMatches();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return RemoveDuplicates;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 0 || numberOfParameters == 1;
	}

}
