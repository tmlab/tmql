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

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * fn:regexp (s : string, re : string) return tuple-sequence
 * </code>
 * </p>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class RegExpFunctionInvocationInterpreter extends
		ExpressionInterpreterImpl<FunctionInvocation> implements
		IFunctionInvocationInterpreter<FunctionInvocation> {

	/**
	 * Base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression of type function-invocation to interpret
	 */
	public RegExpFunctionInvocationInterpreter(FunctionInvocation ex) {
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

		Pattern ci = Pattern.compile("/(.*)/i");

		/*
		 * iterate over parameters
		 */
		for (Map<String, Object> tuple : parameters) {
			Object s = tuple.get("$0");
			Object re = tuple.get("$1");
			if (s == null || re == null) {
				continue;
			}
			String regexp = re.toString();
			Pattern p;
			Matcher m = ci.matcher(regexp);
			if (m.matches()) {
				p = Pattern.compile(m.group(1), Pattern.CASE_INSENSITIVE);
			} else {
				p = Pattern.compile(regexp);
			}
			System.out.println(p.pattern());
			/*
			 * check regular expression is a sequence
			 */
			if (re instanceof Collection<?> && !((Collection<?>) re).isEmpty()) {
				regexp = ((Collection<?>) re).iterator().next().toString();
			}

			Map<String, Object> result = HashUtil.getHashMap();
			/*
			 * check if value is a sequence
			 */
			if (s instanceof Collection<?>) {
				ITupleSequence<String> matches = runtime.getProperties()
						.newSequence();
				/*
				 * iterate over values and add only matching strings
				 */
				for (Object obj : (Collection<?>) s) {
					if (p.matcher(obj.toString()).matches()) {
						matches.add(obj.toString());
					}
				}
				if (!matches.isEmpty()) {
					result.put(QueryMatches.getNonScopedVariable(), matches);
				}
			}
			/*
			 * add string if it matches the regular expression
			 */
			else if (p.matcher(s.toString()).matches()) {
				result.put(QueryMatches.getNonScopedVariable(), s);
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
		return "fn:regexp";
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 2;
	}
}
