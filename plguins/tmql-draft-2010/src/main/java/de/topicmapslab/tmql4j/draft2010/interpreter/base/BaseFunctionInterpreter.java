package de.topicmapslab.tmql4j.draft2010.interpreter.base;

import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.IFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Abstract interpret implementation of binary string functions.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public abstract class BaseFunctionInterpreter<T extends IExpression> extends
		BaseExpressionInterpreter<T> implements
		IFunctionInvocationInterpreter<T> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression to interpret
	 */
	public BaseFunctionInterpreter(T ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected long getNumberOfArguments() {
		return getRequiredVariableCount();
	}
}
