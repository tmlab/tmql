package de.topicmapslab.tmql4j.draft2010.grammar.functions;

import java.lang.reflect.Method;
import java.util.Map;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * Abstract interpret implementation of binary string functions.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public abstract class BinaryStringFunctionImpl extends FunctionImpl {

	/**
	 * Method return the method to call
	 * 
	 * @return the method
	 * @throws NoSuchMethodException
	 *             thrown if method cannot be found
	 */
	public abstract Method getMethod() throws NoSuchMethodException;

	/**
	 * Method is calling to convert the result of the method to the expected
	 * type.
	 * 
	 * @param value
	 *            the value
	 * @return the result
	 */
	public abstract Object toResult(final Object value);

	/**
	 * {@inheritDoc}
	 */
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, IExpressionInterpreter<?> caller) {
		/*
		 * extract arguments
		 */
		QueryMatches[] arguments = getParameters(runtime, context, caller);
		/*
		 * execute function operation
		 */
		QueryMatches matches = new QueryMatches(runtime);
		for (Object o : arguments[0].getPossibleValuesForVariable()) {
			final String value = LiteralUtils.asString(o);
			/*
			 * check if second arguments are not empty
			 */
			if (!arguments[1].isEmpty() && arguments[1].getOrderedKeys().contains(QueryMatches.getNonScopedVariable())) {
				/*
				 * get other string literal
				 */
				final String otherValue = LiteralUtils.asString(arguments[1].getPossibleValuesForVariable().get(0));

				try {
					/*
					 * get method to call
					 */
					Method method = getMethod();
					Object obj = method.invoke(value, otherValue);
					/*
					 * store result
					 */
					Map<String, Object> result = HashUtil.getHashMap();
					result.put(QueryMatches.getNonScopedVariable(), toResult(obj));
					matches.add(result);
				} catch (Exception e) {
					throw new TMQLRuntimeException(e);
				}
			}

		}
		return matches;
	}
}
