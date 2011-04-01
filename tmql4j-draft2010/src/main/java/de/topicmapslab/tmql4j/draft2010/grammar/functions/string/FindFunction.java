package de.topicmapslab.tmql4j.draft2010.grammar.functions.string;

import java.lang.reflect.Method;
import java.math.BigInteger;

import de.topicmapslab.tmql4j.draft2010.grammar.functions.BinaryStringFunctionImpl;

/**
 * Interpreter implementation of function 'find'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class FindFunction extends
		BinaryStringFunctionImpl {

	/**
	 * 
	 */
	public static final String IDENTIFIER = "find";

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
		return numberOfParameters == 2;
	}

	/**
	 * {@inheritDoc}
	 */	
	public Method getMethod() throws NoSuchMethodException {
		return String.class.getMethod("indexOf", String.class);
	}

	/**
	 * {@inheritDoc}
	 */	
	public Object toResult(Object value) {
		return BigInteger.valueOf(Long.parseLong(value.toString()));
	}
}
