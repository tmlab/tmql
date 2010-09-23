package de.topicmapslab.tmql4j.draft2010.interpreter.base;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.expressions.Expression;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Abstract interpret implementation of binary string functions.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public abstract class BaseExpressionInterpreter<T extends IExpression> extends
		ExpressionInterpreterImpl<T> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression to interpret
	 */
	public BaseExpressionInterpreter(T ex) {
		super(ex);
	}

	/**
	 * Returns the number of sub-expressions to call
	 * 
	 * @return the number of sub-expressions to call
	 */
	protected long getNumberOfArguments() {
		return getExpression().getExpressions().size();
	}

	/**
	 * Method extracts the number of expected arguments by calling the contained
	 * expressions
	 * 
	 * @param runtime
	 *            the current runtime
	 * @return an array containing all arguments
	 * @throws TMQLRuntimeException
	 *             thrown if execution failed
	 */
	protected QueryMatches[] extractArguments(final TMQLRuntime runtime)
			throws TMQLRuntimeException {
		return extractArguments(runtime, Expression.class);
	}
}
