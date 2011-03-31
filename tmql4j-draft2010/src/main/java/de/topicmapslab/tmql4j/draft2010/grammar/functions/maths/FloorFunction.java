package de.topicmapslab.tmql4j.draft2010.grammar.functions.maths;

import java.math.BigDecimal;
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
 * Interpreter implementation of function 'floor'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class FloorFunction extends FunctionImpl {

	/**
	 * 
	 */
	public static final String IDENTIFIER = "floor";

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
				BigDecimal d = LiteralUtils.asDecimal(o.toString());
				BigInteger i = BigInteger.valueOf((long) Math.floor(d.doubleValue()));
				result.put(QueryMatches.getNonScopedVariable(), i);
				matches.add(result);
			}
			/*
			 * object is an integer number
			 */
			else if (LiteralUtils.isInteger(o.toString())) {
				BigInteger i = LiteralUtils.asInteger(o.toString());
				result.put(QueryMatches.getNonScopedVariable(), i);
				matches.add(result);
			}
		}
		return matches;
	}

}
