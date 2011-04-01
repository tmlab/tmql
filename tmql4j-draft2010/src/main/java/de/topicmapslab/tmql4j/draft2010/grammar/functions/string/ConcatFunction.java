package de.topicmapslab.tmql4j.draft2010.grammar.functions.string;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.FunctionImpl;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * Interpreter implementation of function 'concat'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ConcatFunction extends FunctionImpl {

	/**
	 * 
	 */
	public static final String IDENTIFIER = "concat";

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

	/**
	 * {@inheritDoc}
	 */
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context,
			IExpressionInterpreter<?> caller) {
		/*
		 * extract arguments
		 */
		QueryMatches[] arguments = getParameters(runtime, context, caller);
		/*
		 * execute function operation
		 */
		List<String> list = new ArrayList<String>();
		addResults(arguments, "", list, 0);
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
	private void addResults(final QueryMatches[] values, final String current,
			final List<String> results, int currentIndex) {
		/*
		 * more parts of string available
		 */
		if (values.length > currentIndex) {
			for (Object value : values[currentIndex].getPossibleValuesForVariable()) {
				/*
				 * is collection
				 */
				if (value instanceof Collection<?>) {
					for (Object val : (Collection<?>) value) {
						String newString = current + LiteralUtils.asString(val);
						addResults(values, newString, results, currentIndex + 1);
					}
				}
				/*
				 * is anything else
				 */
				else {
					String newString = current + LiteralUtils.asString(value);
					addResults(values, newString, results, currentIndex + 1);
				}
			}
		}
		/*
		 * no more parts available
		 */
		else {
			results.add(current);
		}
	}
}
