package de.topicmapslab.tmql4j.draft2010.interpreter.functions.string;

import java.lang.reflect.Method;

import de.topicmapslab.tmql4j.draft2010.expressions.FunctionCall;
import de.topicmapslab.tmql4j.draft2010.interpreter.base.BaseBinaryStringFunctionInterpreter;

/**
 * Interpreter implementation of function 'concat'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ConcatFunctionInterpreter extends
		BaseBinaryStringFunctionInterpreter<FunctionCall> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the function to interpret
	 */
	public ConcatFunctionInterpreter(FunctionCall ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getItemIdentifier() {
		return "concat";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getRequiredVariableCount() {
		return 2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Method getMethod() throws NoSuchMethodException {
		return String.class.getMethod("concat", String.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object toResult(Object value) {
		return value.toString();
	}
}
