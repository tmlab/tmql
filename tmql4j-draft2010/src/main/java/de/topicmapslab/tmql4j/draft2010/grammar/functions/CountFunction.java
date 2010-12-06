package de.topicmapslab.tmql4j.draft2010.grammar.functions;

import java.math.BigInteger;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;

/**
 * Interpreter implementation of function 'count'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class CountFunction extends FunctionImpl {

	/**
	 * 
	 */
	public static final String IDENTIFIER = "count";

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
		return QueryMatches.asQueryMatchNS(runtime, BigInteger.valueOf(arguments[0].size()));
	}
}
