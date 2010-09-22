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
 * fn:string-lt (a : string, b : string) return tuple-sequence
 * </code>
 * </p>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class StringLtFunctionInvocationInterpreter extends
		ExpressionInterpreterImpl<FunctionInvocation> implements
		IFunctionInvocationInterpreter<FunctionInvocation> {

	/**
	 * Base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression of type function-invocation to interpret
	 */
	public StringLtFunctionInvocationInterpreter(FunctionInvocation ex) {
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
			Object s = tuple.get("$0");
			Object b = tuple.get("$1");
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
					/*
					 * check if 2nd value is a sequence
					 */
					for (Object obj : (Collection<?>) s) {
						/*
						 * add only strings which is lower
						 */
						if (ComparisonUtils.isLowerThan(obj, b)) {
							matches.add(obj.toString());
						}
					}
					if (!matches.isEmpty()) {
						result
								.put(QueryMatches.getNonScopedVariable(),
										matches);
					}
				} else {
					/*
					 * add only strings which is lower
					 */
					if (ComparisonUtils.isLowerThan(s, b)) {
						result.put(QueryMatches.getNonScopedVariable(), s
								.toString());
					}
				}
				if (!result.isEmpty()) {
					results.add(result);
				}
			} catch (ParseException e) {
				throw new TMQLRuntimeException(
						"Error during interpretation of " + getItemIdentifier()
								+ ".", e);
			}
		}
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return "fn:string-lt";
	}

	/**
	 * {@inheritDoc}
	 */
	public long getRequiredVariableCount() {
		return 2;
	}
}
