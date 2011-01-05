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
 * fn:substring (s : string, form : integer, to : integer) return string
 * </code>
 * </p>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SubStringFunction extends
FunctionImpl {

	/**
	 * 
	 */
	public static final String IDENTIFIER = "fn:substring";

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
			throw new TMQLRuntimeException(getItemIdentifier()
					+ "() requieres 3 parameters.");
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
					List<String> seq = HashUtil.getList();
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

		return results;
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
		return IDENTIFIER;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 3;
	}
}
