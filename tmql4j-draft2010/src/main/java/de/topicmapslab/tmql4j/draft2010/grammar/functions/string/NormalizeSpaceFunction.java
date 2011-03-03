package de.topicmapslab.tmql4j.draft2010.grammar.functions.string;

import java.util.Map;
import java.util.StringTokenizer;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.FunctionImpl;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * Interpreter implementation of function 'normalize-space'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class NormalizeSpaceFunction extends FunctionImpl {

	/**
	 * 
	 */
	public static final String IDENTIFIER = "normalize-space";

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
		return numberOfParameters == 1;
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
			/*
			 * normalize string
			 */
			final String value = LiteralUtils.asString(o);
			StringTokenizer tokenizer = new StringTokenizer(value, " ");
			StringBuilder builder = new StringBuilder();
			while (tokenizer.hasMoreTokens()) {
				builder.append(tokenizer.nextToken() + " ");
			}
			/*
			 * store value as tuple
			 */
			Map<String, Object> result = HashUtil.getHashMap();
			result.put(QueryMatches.getNonScopedVariable(), builder.toString().trim());
			matches.add(result);
		}
		return matches;
	}

}
