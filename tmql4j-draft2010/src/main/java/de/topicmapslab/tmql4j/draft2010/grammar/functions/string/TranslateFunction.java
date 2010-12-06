package de.topicmapslab.tmql4j.draft2010.grammar.functions.string;

import java.util.Map;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.FunctionImpl;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * Interpreter implementation of function 'translate'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TranslateFunction extends FunctionImpl {

	/**
	 * 
	 */
	public static final String IDENTIFIER = "translate";

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
		if (!arguments[1].isEmpty() && !arguments[2].isEmpty() && arguments[1].getOrderedKeys().contains(QueryMatches.getNonScopedVariable())
				&& arguments[2].getOrderedKeys().contains(QueryMatches.getNonScopedVariable())) {

			/*
			 * extract arguments
			 */
			final String stringToReplace = LiteralUtils.asString(arguments[1].getPossibleValuesForVariable().get(0));
			final String replacementString = LiteralUtils.asString(arguments[2].getPossibleValuesForVariable().get(0));

			/*
			 * check length of arguments
			 */
			if (stringToReplace.length() < replacementString.length()) {
				throw new TMQLRuntimeException("The second argument has to be a char-sequence containing at least as much as characters like the thrid argument.");
			}
			/*
			 * execute function operation
			 */
			for (Object o : arguments[0].getPossibleValuesForVariable()) {
				String value = LiteralUtils.asString(o);

				/*
				 * replace characters
				 */
				for (int index = 0; index < stringToReplace.length(); index++) {
					value = value.replace(stringToReplace.charAt(index), replacementString.charAt(index));
				}

				/*
				 * store result
				 */
				Map<String, Object> result = HashUtil.getHashMap();
				result.put(QueryMatches.getNonScopedVariable(), value);
				matches.add(result);

			}
		}
		return matches;
	}

}
