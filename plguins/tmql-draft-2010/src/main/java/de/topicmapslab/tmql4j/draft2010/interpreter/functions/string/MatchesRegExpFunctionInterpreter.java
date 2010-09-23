package de.topicmapslab.tmql4j.draft2010.interpreter.functions.string;

import java.lang.reflect.Method;

import de.topicmapslab.tmql4j.draft2010.expressions.FunctionCall;
import de.topicmapslab.tmql4j.draft2010.interpreter.base.BaseBinaryStringFunctionInterpreter;

/**
 * Interpreter implementation of function 'matches-regexp'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class MatchesRegExpFunctionInterpreter extends
		BaseBinaryStringFunctionInterpreter<FunctionCall> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the function to interpret
	 */
	public MatchesRegExpFunctionInterpreter(FunctionCall ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	
	public String getItemIdentifier() {
		return "matches-regexp";
	}

	/**
	 * {@inheritDoc}
	 */
	
	public long getRequiredVariableCount() {
		return 2;
	}

	
	public Method getMethod() throws NoSuchMethodException {
		return String.class.getMethod("matches", String.class);
	}

	
	public Object toResult(Object value) {
		return Boolean.parseBoolean(value.toString());
	}

}
