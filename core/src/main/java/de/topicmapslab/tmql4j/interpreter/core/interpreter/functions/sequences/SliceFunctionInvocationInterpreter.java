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
package de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.sequences;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.model.tuplesequence.ITupleSequence;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.IFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.parser.core.expressions.FunctionInvocation;
import de.topicmapslab.tmql4j.parser.core.expressions.Parameters;

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
public class SliceFunctionInvocationInterpreter extends
		ExpressionInterpreterImpl<FunctionInvocation> implements
		IFunctionInvocationInterpreter<FunctionInvocation> {

	/**
	 * Base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression of type function-invocation to interpret
	 */
	public SliceFunctionInvocationInterpreter(FunctionInvocation ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		QueryMatches results = new QueryMatches(runtime);

		/*
		 * extract arguments
		 */
		QueryMatches parameters = extractArguments(runtime, Parameters.class, 0);

		/*
		 * check count of variables
		 */
		if (parameters.getOrderedKeys().size() < getRequiredVariableCount()) {
			throw new TMQLRuntimeException(getItemIdentifier()
					+ "() requieres " + getRequiredVariableCount()
					+ " parameter.");
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
					ITupleSequence<Object> seq = runtime.getProperties()
							.newSequence();

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

		runtime.getRuntimeContext().peek()
				.setValue(VariableNames.QUERYMATCHES, results);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return "fn:slice";
	}

	/**
	 * {@inheritDoc}
	 */
	public long getRequiredVariableCount() {
		return 3;
	}
}
