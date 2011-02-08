/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2010.util;

import java.util.List;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * @author Sven Krosse
 *
 */
public class RuntimeUtils {

	/**
	 * Utility method to extract the parameters
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param caller
	 *            the calling interpreter
	 * @return the result of function execution
	 * @throws TMQLRuntimeException
	 */
	public static QueryMatches[] getParameters(ITMQLRuntime runtime, IContext context, IExpressionInterpreter<?> caller) throws TMQLRuntimeException {
		List<IExpressionInterpreter<?>> interpreters = caller.getInterpreters(runtime);
		QueryMatches[] parameters = new QueryMatches[interpreters.size()];
		int index = 0;
		for (IExpressionInterpreter<?> interpreter : interpreters) {
			parameters[index++] = interpreter.interpret(runtime, context);
		}
		return parameters;
	}
	
}
