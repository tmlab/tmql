package de.topicmapslab.tmql4j.draft2010.grammar.functions.string;

import java.lang.reflect.Method;

import de.topicmapslab.tmql4j.draft2010.grammar.functions.BinaryStringFunctionImpl;

/**
 * Interpreter implementation of function 'matches-regexp'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class MatchesRegExpFunction extends BinaryStringFunctionImpl {

	/**
	 * 
	 */
	public static final String IDENTIFIER = "matches-regexp";
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
		return String.class.getMethod("matches", String.class);
	}
	/**
	 * {@inheritDoc}
	 */
	public Object toResult(Object value) {
		return Boolean.parseBoolean(value.toString());
	}

}
