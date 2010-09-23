package de.topicmapslab.tmql4j.draft2010.interpreter.functions.string;

import java.lang.reflect.Method;

import de.topicmapslab.tmql4j.draft2010.expressions.FunctionCall;
import de.topicmapslab.tmql4j.draft2010.interpreter.base.BaseBinaryStringFunctionInterpreter;

/**
 * Interpreter implementation of function 'starts-with'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class StartsWithFunctionInterpreter extends
		BaseBinaryStringFunctionInterpreter<FunctionCall> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the function to interpret
	 */
	public StartsWithFunctionInterpreter(FunctionCall ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	
	public String getItemIdentifier() {
		return "starts-with";
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
	
	public Method getMethod() throws NoSuchMethodException {
		return String.class.getMethod("startsWith", String.class);
	}

	/**
	 * {@inheritDoc}
	 */
	
	public Object toResult(Object value) {
		return Boolean.valueOf(value.toString());
	}

}
