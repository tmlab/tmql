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
package de.topicmapslab.tmql4j.draft2011.path.grammar.functions.sequences;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.grammar.functions.FunctionImpl;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Special implementation of an {@link IFunctionInvocationInterpreter}.
 * <p>
 * This implementation realize the execution of the following function:
 * <p>
 * <code>
 * fn:slice (s : tuple-sequence, low : integer, high : integer) return tuple-sequence
 * </code>
 * </p>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SliceFunction extends FunctionImpl {

	/**
	 * 
	 */
	public static final String IDENTIFIER = "fn:slice";

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
		 * check count of variables
		 */
		if (!isExpectedNumberOfParameters(parameters.getOrderedKeys().size())) {
			throw new TMQLRuntimeException(getItemIdentifier() + "() requieres 3 parameters.");
		}

		/*
		 * iterate over parameters
		 */
		for (Map<String, Object> tuple : parameters) {
			Object sequence = tuple.get("$0");
			Object low = tuple.get("$1");
			Object high = tuple.get("$2");
			Map<String, Object> result = HashUtil.getHashMap();
			/*
			 * check if 2nd and 3rd parameters are a numeric value
			 */
			if (low instanceof BigInteger && high instanceof BigInteger) {
				/*
				 * check if first parameter is a collection
				 */
				if (sequence instanceof Collection<?>) {
					Object[] values = ((Collection<?>) sequence).toArray();
					long lLow = ((BigInteger) low).longValue();
					if (lLow < 0) {
						lLow = 0;
					} else if (lLow >= values.length) {
						lLow = values.length - 1;
					}
					long lHigh = ((BigInteger) high).longValue();
					if (lHigh < 0) {
						lHigh = 0;
					} else if (lHigh > values.length) {
						lHigh = values.length;
					}
					List<Object> seq = HashUtil.getList();

					for (long index = lLow; index < lHigh; index++) {
						seq.add(values[(int) index]);
					}
					if (!seq.isEmpty()) {
						result.put(QueryMatches.getNonScopedVariable(), seq);
					}
				}
				/*
				 * at value if it isn't a sequence
				 */
				else {
					result.put(QueryMatches.getNonScopedVariable(), sequence);
				}
			}
			if (!result.isEmpty()) {
				results.add(result);
			}
		}

		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return IDENTIFIER;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 3;
	}
}
