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
 * fn:count (s : tuple-sequence) return integer
 * </code>
 * </p>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class CountFunctionInvocationInterpreter extends
		ExpressionInterpreterImpl<FunctionInvocation> implements
		IFunctionInvocationInterpreter<FunctionInvocation> {

	/**
	 * Base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression of type function-invocation to interpret
	 */
	public CountFunctionInvocationInterpreter(FunctionInvocation ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		QueryMatches results = new QueryMatches(runtime);

		if (runtime.getRuntimeContext().peek().contains(
				VariableNames.ITERATED_BINDINGS)) {
			Object i = runtime.getRuntimeContext().peek().getValue(
					VariableNames.ITERATED_BINDINGS);
			if (i instanceof QueryMatches) {
				for (Map<String, Object> tuple : (QueryMatches) i) {

					runtime.getRuntimeContext().push();
					QueryMatches iteration = new QueryMatches(runtime);
					iteration.add(tuple);

					runtime.getRuntimeContext().peek().setValue(
							VariableNames.ITERATED_BINDINGS, iteration);

					Map<String, Object> tuple_ = HashUtil.getHashMap(tuple); 
					tuple_.putAll(callParameters(runtime));
					results.add(tuple_);
				}
			}
		} else {
			runtime.getRuntimeContext().push();
			results.add(callParameters(runtime));
		}

		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);
	}

	private Map<String, Object> callParameters(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * extract arguments
		 */
		QueryMatches parameters = extractArguments(runtime, Parameters.class, 0);
		Map<String, Object> result = HashUtil.getHashMap();
		int count = parameters.size();
		if ( count == 1 && parameters.getOrderedKeys().contains("$0")){
			Collection<?> col = parameters.getPossibleValuesForVariable("$0");
			count = col.size();
			if ( count == 1 ){
				Object o = col.iterator().next();
				if ( o instanceof Collection<?>){
					count = ((Collection<?>) o).size();
				}
			}
		}
		result.put(QueryMatches.getNonScopedVariable(), BigInteger.valueOf(count));
		return result;
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
	public long getRequiredVariableCount() {
		return 1;
	}
}
