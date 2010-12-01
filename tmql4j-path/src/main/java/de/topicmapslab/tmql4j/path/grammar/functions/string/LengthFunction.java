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
package de.topicmapslab.tmql4j.path.grammar.functions.string;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.functions.FunctionImpl;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Special implementation of an {@link IFunctionInvocationInterpreter}.
 * <p>
 * This implementation realize the execution of the following function:
 * <p>
 * <code>
 * fn:length (s : string) return integer
 * </code>
 * </p>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class LengthFunction extends FunctionImpl {

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
			throw new TMQLRuntimeException(getItemIdentifier() + "() requieres 1 parameters.");
		}

		/*
		 * iterate over parameters
		 */
		for (Map<String, Object> tuple : parameters) {
			Object sequence = tuple.get("$0");
			Map<String, Object> result = HashUtil.getHashMap();
			/*
			 * check if value is a sequence
			 */
			if (sequence instanceof Collection<?>) {
				List<BigInteger> lengths = HashUtil.getList();
				/*
				 * add length of each string to a new sequence
				 */
				for (Object obj : (Collection<?>) sequence) {
					lengths.add(BigInteger.valueOf(obj.toString().length()));
				}
				result.put(QueryMatches.getNonScopedVariable(), lengths);
			}
			/*
			 * add length of the string to result tuple
			 */
			else {
				result.put(QueryMatches.getNonScopedVariable(), BigInteger.valueOf(sequence.toString().length()));
			}
			results.add(result);
		}
		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return "fn:length";
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 1;
	}
}
