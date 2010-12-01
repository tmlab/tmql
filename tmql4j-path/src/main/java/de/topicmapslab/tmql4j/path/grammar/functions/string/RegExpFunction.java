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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * fn:regexp (s : string, re : string) return tuple-sequence
 * </code>
 * </p>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class RegExpFunction extends FunctionImpl {

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
			throw new TMQLRuntimeException(getItemIdentifier() + "() requieres 2 parameters.");
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
				List<String> matches = HashUtil.getList();
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
		return results;
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
