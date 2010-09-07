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
 * fn:substring (s : string, form : integer, to : integer) return string
 * </code>
 * </p>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SubStringFunctionInvocationInterpreter extends
		ExpressionInterpreterImpl<FunctionInvocation> implements
		IFunctionInvocationInterpreter<FunctionInvocation> {

	/**
	 * Base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression of type function-invocation to interpret
	 */
	public SubStringFunctionInvocationInterpreter(FunctionInvocation ex) {
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
			Object oString = tuple.get("$0");
			Object oFrom = tuple.get("$1");
			Object oTo = tuple.get("$2");
			Map<String, Object> result = HashUtil.getHashMap();
			/*
			 * check if 2nd and 3rd parameters are numeric values
			 */
			if (oFrom instanceof BigInteger && oTo instanceof BigInteger) {
				BigInteger from = (BigInteger) oFrom;
				BigInteger to = (BigInteger) oTo;
				/*
				 * check if 1st value is a sequence
				 */
				if (oString instanceof Collection<?>) {
					ITupleSequence<String> seq = runtime.getProperties()
							.newSequence();
					/*
					 * add all sub-strings to the result sequence
					 */
					for (Object o : (Collection<?>) oString) {
						seq.add(secureSubString(o.toString(), from, to));
					}
					result.put(QueryMatches.getNonScopedVariable(), seq);
				} else {
					result.put(QueryMatches.getNonScopedVariable(),
							secureSubString(oString.toString(), from, to));
				}

			}
			results.add(result);
		}

		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);
	}

	/**
	 * Internal method to extract the substring specified by the given indexes.
	 * The method extract the part which are contained between the given index.
	 * If one index is out of range, the function extract the part between the
	 * index which are in the range of the given string.
	 * 
	 * @param string
	 *            the string
	 * @param from
	 *            the lower index
	 * @param to
	 *            the upper index
	 * @return the substring
	 */
	private String secureSubString(String string, BigInteger from, BigInteger to) {
		int begin = from.intValue();
		/*
		 * if lower index is negative set to zero
		 */
		if (begin < 0) {
			begin = 0;
		}
		/*
		 * if lower index is greater than string size, set to last index
		 */
		if (begin > string.length()) {
			begin = string.length() - 1;
		}
		int end = to.intValue();
		/*
		 * if upper index is negative set to zero
		 */
		if (end < 0) {
			end = 0;
		}
		/*
		 * if upper index is greater than the string size, set to the last index
		 */
		if (end > string.length()) {
			end = string.length();
		}
		/*
		 * if upper index is lower than lower index set to lower index
		 */
		if (end < begin) {
			end = begin;
		}
		return string.substring(begin, end);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return "fn:substring";
	}

	/**
	 * {@inheritDoc}
	 */
	public long getRequiredVariableCount() {
		return 3;
	}
}
