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
import de.topicmapslab.tmql4j.interpreter.utility.operation.LiteralUtils;
import de.topicmapslab.tmql4j.parser.core.expressions.FunctionInvocation;
import de.topicmapslab.tmql4j.parser.core.expressions.Parameters;

/**
 * Special implementation of an {@link IFunctionInvocationInterpreter}.
 * <p>
 * This implementation realize the execution of the following function:
 * <p>
 * <code>
 * fn:has-datatype (s: tuple-sequence) return tuple-sequence
 * </code>
 * </p>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class HasDatatypeFunctionInvocationInterpreter extends
		ExpressionInterpreterImpl<FunctionInvocation> implements
		IFunctionInvocationInterpreter<FunctionInvocation> {

	/**
	 * Base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression of type function-invocation to interpret
	 */
	public HasDatatypeFunctionInvocationInterpreter(FunctionInvocation ex) {
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
		if (!isExpectedNumberOfParameters(parameters.getOrderedKeys().size())) {
			throw new TMQLRuntimeException(getItemIdentifier()
					+ "() requieres 1 parameters.");
		}
		/*
		 * iterate over all parameters
		 */
		for (Map<String, Object> tuple : parameters) {
			Object value = tuple.get("$0");
			Map<String, Object> result = HashUtil.getHashMap();
			ITupleSequence<Object> datatypes = runtime.getProperties()
					.newSequence();

			/*
			 * extract data-types
			 */
			Object datatype = LiteralUtils
					.getDatatypeOfLiterals(runtime, value);
			if (datatype instanceof Collection<?>) {
				datatypes.addAll((Collection<?>) datatype);
			} else {
				datatypes.add(datatype);
			}
			/*
			 * store results
			 */
			result.put(QueryMatches.getNonScopedVariable(), datatypes);
			results.add(result);
		}

		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return "fn:has-datatype";
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 1;
	}
}
