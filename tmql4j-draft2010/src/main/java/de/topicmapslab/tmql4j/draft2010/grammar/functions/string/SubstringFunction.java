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
 * Interpreter implementation of function 'substring'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SubstringFunction extends FunctionImpl {

	/**
	 * 
	 */
	public static final String IDENTIFIER = "substring";

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
			try {
				/*
				 * get indexes
				 */
				int beginIndex = Integer.parseInt(LiteralUtils.asString(arguments[1].getPossibleValuesForVariable().get(0)));
				int count = Integer.parseInt(LiteralUtils.asString(arguments[2].getPossibleValuesForVariable().get(0)));

				/*
				 * execute function operation
				 */
				for (Object o : arguments[0].getPossibleValuesForVariable()) {
					final String value = LiteralUtils.asString(o);

					/*
					 * extract substring
					 */
					final String string;
					if (beginIndex < value.length()) {
						if (beginIndex + count < value.length()) {
							string = value.substring(beginIndex, beginIndex + count);
						} else {
							string = value.substring(beginIndex);
						}
					} else {
						string = "";
					}
					/*
					 * store result
					 */
					Map<String, Object> result = HashUtil.getHashMap();
					result.put(QueryMatches.getNonScopedVariable(), string);
					matches.add(result);

				}
			} catch (Exception e) {
				throw new TMQLRuntimeException(e);
			}
		}
		return matches;
	}
}
