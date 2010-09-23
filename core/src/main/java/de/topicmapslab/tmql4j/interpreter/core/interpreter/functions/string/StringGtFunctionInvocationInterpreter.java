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
package de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.string;

import java.text.ParseException;
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
import de.topicmapslab.tmql4j.interpreter.utility.operation.ComparisonUtils;
import de.topicmapslab.tmql4j.parser.core.expressions.FunctionInvocation;
import de.topicmapslab.tmql4j.parser.core.expressions.Parameters;

/**
 * Special implementation of an {@link IFunctionInvocationInterpreter}.
 * <p>
 * This implementation realize the execution of the following function:
 * <p>
 * <code>
 * fn:string-gt (a : string, b : string) return tuple-sequence
 * </code>
 * </p>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class StringGtFunctionInvocationInterpreter extends
		ExpressionInterpreterImpl<FunctionInvocation> implements
		IFunctionInvocationInterpreter<FunctionInvocation> {

	/**
	 * Base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression of type function-invocation to interpret
	 */
	public StringGtFunctionInvocationInterpreter(FunctionInvocation ex) {
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
					+ "() requieres 2 parameters.");
		}

		/*
		 * iterate over parameters
		 */
		for (Map<String, Object> tuple : parameters) {
			Object s = tuple.get("$0");
			Object b = tuple.get("$1");
			/*
			 * check if 2nd value is a sequence
			 */
			if (b instanceof Collection<?>) {
				/*
				 * extract first value
				 */
				if (!((Collection<?>) b).isEmpty()) {
					b = ((Collection<?>) b).iterator().next();
				}
			}
			Map<String, Object> result = HashUtil.getHashMap();
			try {
				/*
				 * check if 1st value is a sequence
				 */
				if (s instanceof Collection<?>) {
					ITupleSequence<String> matches = runtime.getProperties()
							.newSequence();
					for (Object obj : (Collection<?>) s) {
						/*
						 * add only string which is greater
						 */
						if (ComparisonUtils.isGreaterThan(obj, b)) {
							matches.add(obj.toString());
						}
					}
					if (!matches.isEmpty()) {
						result
								.put(QueryMatches.getNonScopedVariable(),
										matches);
					}
				}
				/*
				 * add only string which is greater
				 */
				else if (ComparisonUtils.isGreaterThan(s, b)) {
					result.put(QueryMatches.getNonScopedVariable(), s
							.toString());
				}
			} catch (ParseException e) {
				throw new TMQLRuntimeException(
						"Error during interpretation of " + getItemIdentifier()
								+ ".", e);
			}
			if (!result.isEmpty()) {
				results.add(result);
			}
		}
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return "fn:string-gt";
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 2;
	}
}
