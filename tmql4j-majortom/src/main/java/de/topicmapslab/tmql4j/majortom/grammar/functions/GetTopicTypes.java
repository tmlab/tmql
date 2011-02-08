/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.majortom.grammar.functions;

import de.topicmapslab.majortom.model.index.ITypeInstanceIndex;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.path.grammar.functions.FunctionImpl;

/**
 * @author Sven Krosse
 * 
 */
public class GetTopicTypes extends FunctionImpl {

	public static final String GetTopicTypes = "fn:get-topic-types";

	/**
	 * {@inheritDoc}
	 */
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context,
			IExpressionInterpreter<?> caller) {

		ITypeInstanceIndex index = context.getQuery().getTopicMap().getIndex(
				ITypeInstanceIndex.class);
		if (!index.isOpen()) {
			index.open();
		}
		return QueryMatches.asQueryMatchNS(runtime, index
				.getTopicTypes());

	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return GetTopicTypes;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 0;
	}

}
