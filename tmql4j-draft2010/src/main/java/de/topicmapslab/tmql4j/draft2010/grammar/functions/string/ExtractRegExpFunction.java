package de.topicmapslab.tmql4j.draft2010.grammar.functions.string;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.FunctionImpl;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * Interpreter implementation of function 'extract-regexp'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ExtractRegExpFunction extends FunctionImpl {

	/**
	 * 
	 */
	public static final String IDENTIFIER = "extract-regexp";

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

	/**
	 * {@inheritDoc}
	 */
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, IExpressionInterpreter<?> caller) {
		/*
		 * get arguments
		 */
		QueryMatches[] arguments = getParameters(runtime, context, caller);

		QueryMatches matches = new QueryMatches(runtime);
		/*
		 * check if second and third arguments are not empty
		 */
		if (!arguments[1].isEmpty() && arguments[1].getOrderedKeys().contains(QueryMatches.getNonScopedVariable())) {

			/*
			 * get regular expression
			 */
			final String regExp = LiteralUtils.asString(arguments[1].getPossibleValuesForVariable().get(0));
			final Pattern pattern = Pattern.compile(".*?(" + regExp + ").*?");

			/*
			 * execute function operation
			 */
			for (Object o : arguments[0].getPossibleValuesForVariable()) {
				final String value = LiteralUtils.asString(o);

				Matcher matcher = pattern.matcher(value);
				String string = "";
				if (matcher.find()) {
					string = matcher.group(1);
				}

				/*
				 * store result
				 */
				Map<String, Object> result = HashUtil.getHashMap();
				result.put(QueryMatches.getNonScopedVariable(), string);
				matches.add(result);

			}

		}
		return matches;
	}

}
