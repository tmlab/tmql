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
package de.topicmapslab.tmql4j.path.grammar.functions.sequences;

import java.util.Map;
import java.util.Map.Entry;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.path.grammar.functions.FunctionImpl;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Special implementation of an {@link IFunctionInvocationInterpreter}.
 * <p>
 * This implementation realize the execution of the following function:
 * <p>
 * <code>
 * fn:zagzig (s : tuple-sequence) return tuple-sequence
 * </code>
 * </p>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ZagZigFunction extends
FunctionImpl{

	/**
	 * {@inheritDoc}
	 */
	public de.topicmapslab.tmql4j.components.processor.core.QueryMatches interpret(ITMQLRuntime runtime, IContext context, IExpressionInterpreter<?> caller) {
		QueryMatches results = new QueryMatches(runtime);

		/*
		 * extract arguments
		 */
		QueryMatches parameters = getParameters(runtime, context, caller);

		/*
		 * iterate over parameters
		 */
		for (Map<String, Object> tuple : parameters) {
			for (Entry<String, Object> entry : tuple.entrySet()) {
				/*
				 * iterate over all values and create singleton tuples
				 */
				Map<String, Object> result = HashUtil.getHashMap();
				result.put("$0", entry.getValue());
				results.add(result);
			}
		}
		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return "fn:zagzig";
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 1;
	}
}
