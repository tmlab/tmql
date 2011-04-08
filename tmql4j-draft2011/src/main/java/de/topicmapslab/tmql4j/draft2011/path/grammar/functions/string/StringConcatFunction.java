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
package de.topicmapslab.tmql4j.draft2011.path.grammar.functions.string;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.grammar.functions.FunctionImpl;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * Special implementation of an {@link IFunctionInvocationInterpreter}.
 * <p>
 * This implementation realize the execution of the following function:
 * <p>
 * <code>
 * 
 * </code>
 * </p>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class StringConcatFunction extends FunctionImpl {

	/**
	 * 
	 */
	public static final String IDENTIFIER = "fn:string-concat";

	/**
	 * {@inheritDoc}
	 */
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context,
			IExpressionInterpreter<?> caller) {
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
		List<String> list = new ArrayList<String>();
		for (Map<String, Object> tuple : parameters) {
			addResults(tuple, "", list, 0);
		}
		/*
		 * add all results
		 */
		return QueryMatches.asQueryMatchNS(runtime, list.toArray());
	}

	/**
	 * Internal method to add next part to result strings or add the string to
	 * results
	 * 
	 * @param tuple
	 *            the tuple of values
	 * @param current
	 *            the current string
	 * @param results
	 *            the results
	 * @param currentIndex
	 *            the current index
	 */
	private void addResults(final Map<String, Object> tuple,
			final String current, final List<String> results, int currentIndex) {
		/*
		 * generate variable of current index
		 */
		final String variable = "$" + currentIndex;
		/*
		 * more parts of string available
		 */
		if (tuple.containsKey(variable)) {
			Object value = tuple.get(variable);
			/*
			 * is collection
			 */
			if (value instanceof Collection<?>) {
				for (Object val : (Collection<?>) value) {
					String newString = current + LiteralUtils.asString(val);
					addResults(tuple, newString, results, currentIndex + 1);
				}
			}
			/*
			 * is anything else
			 */
			else {
				String newString = current + LiteralUtils.asString(value);
				addResults(tuple, newString, results, currentIndex + 1);
			}
		}
		/*
		 * no more parts available
		 */
		else {
			results.add(current);
		}
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
		return numberOfParameters >= 2;
	}
}
