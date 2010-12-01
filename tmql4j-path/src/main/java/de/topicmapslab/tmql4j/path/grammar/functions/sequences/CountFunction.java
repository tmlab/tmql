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

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.components.processor.core.Context;
import de.topicmapslab.tmql4j.path.grammar.functions.FunctionImpl;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Special implementation of an {@link IFunctionInvocationInterpreter}.
 * <p>
 * This implementation realize the execution of the following function:
 * <p>
 * <code>
 * fn:count (s : tuple-sequence) return integer
 * </code>
 * </p>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class CountFunction extends FunctionImpl {

	/**
	 * {@inheritDoc}
	 */
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, IExpressionInterpreter<?> caller) {
		if (context.getContextBindings() != null) {
			QueryMatches matches = new QueryMatches(runtime);
			for (Map<String, Object> tuple : context.getContextBindings()) {
				QueryMatches iteration = new QueryMatches(runtime);
				iteration.add(tuple);
				Context newContext = new Context(context);
				newContext.setContextBindings(iteration);
				newContext.setCurrentTuple(tuple);
				Map<String, Object> tuple_ = HashUtil.getHashMap(tuple); 
				tuple_.put(QueryMatches.getNonScopedVariable(), callParameters(runtime, newContext, caller));
				matches.add(tuple_);				
			}
			return matches;
		} 
		return QueryMatches.asQueryMatchNS(runtime,callParameters(runtime, context, caller));		
	}

	/**
	 * Utility method calling the parameters and counting the results
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param caller
	 *            the caller
	 * @return the number of results
	 * @throws TMQLRuntimeException
	 */
	private BigInteger callParameters(ITMQLRuntime runtime, IContext context, IExpressionInterpreter<?> caller) throws TMQLRuntimeException {
		/*
		 * extract arguments
		 */
		QueryMatches parameters = getParameters(runtime, context, caller);
		int count = parameters.size();
		if (count == 1 && parameters.getOrderedKeys().contains("$0")) {
			Collection<?> col = parameters.getPossibleValuesForVariable("$0");
			count = col.size();
			if (count == 1) {
				Object o = col.iterator().next();
				if (o instanceof Collection<?>) {
					count = ((Collection<?>) o).size();
				}
			}
		}
		return BigInteger.valueOf(count);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return "fn:count";
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 1;
	}
}
