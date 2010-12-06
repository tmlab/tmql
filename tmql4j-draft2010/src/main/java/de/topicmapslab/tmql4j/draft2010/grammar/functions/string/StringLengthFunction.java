package de.topicmapslab.tmql4j.draft2010.grammar.functions.string;

import java.math.BigInteger;
import java.util.Map;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.FunctionImpl;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * Interpreter implementation of function 'string-length'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class StringLengthFunction extends FunctionImpl {

	/**
	 * 
	 */
	public static final String IDENTIFIER = "string-length";

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
			final String value = LiteralUtils.asString(o);
			Map<String, Object> result = HashUtil.getHashMap();
			result.put(QueryMatches.getNonScopedVariable(), BigInteger.valueOf(value.length()));
			matches.add(result);
		}
		return matches;
	}

}
