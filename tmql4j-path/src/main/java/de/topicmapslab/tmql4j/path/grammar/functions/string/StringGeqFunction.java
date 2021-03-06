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

import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.functions.FunctionImpl;
import de.topicmapslab.tmql4j.util.ComparisonUtils;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Special implementation of an {@link IFunctionInvocationInterpreter}.
 * <p>
 * This implementation realize the execution of the following function:
 * <p>
 * <code>
 * fn:string-geq (a : string, b : string) return tuple-sequence
 * </code>
 * </p>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class StringGeqFunction extends
FunctionImpl {

	/**
	 * 
	 */
	public static final String IDENTIFIER = "fn:string-geq";

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
					List<String> matches = HashUtil.getList();
					for (Object obj : (Collection<?>) s) {
						/*
						 * add only string which is greater or equal
						 */
						if (ComparisonUtils.isGreaterOrEquals(obj, b)) {
							matches.add(obj.toString());
						}
					}
					if (!matches.isEmpty()) {
						result.put(QueryMatches.getNonScopedVariable(), matches);
					}
				}
				/*
				 * add only strings which is greater of equal
				 */
				else if (ComparisonUtils.isGreaterOrEquals(s, b)) {
					result.put(QueryMatches.getNonScopedVariable(),
							s.toString());
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
		return results;
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
		return numberOfParameters == 2;
	}
}
