package de.topicmapslab.tmql4j.draft2010.grammar.functions.string;

import java.lang.reflect.Method;

import de.topicmapslab.tmql4j.draft2010.grammar.functions.BinaryStringFunctionImpl;

/**
 * Interpreter implementation of function 'contains'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ContainsFunction extends BinaryStringFunctionImpl {

	/**
	 * 
	 */
	public static final String IDENTIFIER = "contains";

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
		return String.class.getMethod(IDENTIFIER, CharSequence.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object toResult(Object value) {
		return Boolean.valueOf(value.toString());
	}
}
