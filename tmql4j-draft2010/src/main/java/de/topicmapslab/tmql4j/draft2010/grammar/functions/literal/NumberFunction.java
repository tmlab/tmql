package de.topicmapslab.tmql4j.draft2010.grammar.functions.literal;

import java.util.Map;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.FunctionImpl;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * Interpreter implementation of function 'number'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class NumberFunction extends FunctionImpl {

	/**
	 * 
	 */
	public static final String IDENTIFIER = "number";

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
			Map<String, Object> result = HashUtil.getHashMap();
			/*
			 * object is a decimal number
			 */
			if (LiteralUtils.isDecimal(o.toString())) {
				result.put(QueryMatches.getNonScopedVariable(), LiteralUtils.asDecimal(o.toString()));
				matches.add(result);
			}
			/*
			 * object is an integer number
			 */
			else if (LiteralUtils.isInteger(o.toString())) {
				result.put(QueryMatches.getNonScopedVariable(), LiteralUtils.asInteger(o.toString()));
				matches.add(result);
			}
		}
		return matches;

	}

}
