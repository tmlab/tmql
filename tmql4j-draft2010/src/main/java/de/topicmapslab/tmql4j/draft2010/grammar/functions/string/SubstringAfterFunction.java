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
 * Interpreter implementation of function 'substring-after'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SubstringAfterFunction extends FunctionImpl {

	/**
	 * 
	 */
	public static final String IDENTIFIER = "substring-after";

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

		/*
		 * execute function operation
		 */
		QueryMatches matches = new QueryMatches(runtime);
		for (Object o : arguments[0].getPossibleValuesForVariable()) {
			final String value = LiteralUtils.asString(o);
			/*
			 * check if second and third arguments are not empty
			 */
			if (!arguments[1].isEmpty() && arguments[1].getOrderedKeys().contains(QueryMatches.getNonScopedVariable())) {
				try {
					/*
					 * get indexes
					 */
					final String otherValue = LiteralUtils.asString(arguments[1].getPossibleValuesForVariable().get(0));
					int index = value.indexOf(otherValue);
					/*
					 * extract substring
					 */
					final String string;
					if (index != -1) {
						if (index + otherValue.length() < value.length()) {
							string = value.substring(index + otherValue.length());
						} else {
							string = "";
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
				} catch (Exception e) {
					throw new TMQLRuntimeException(e);
				}
			}

		}
		return matches;

	}

}
