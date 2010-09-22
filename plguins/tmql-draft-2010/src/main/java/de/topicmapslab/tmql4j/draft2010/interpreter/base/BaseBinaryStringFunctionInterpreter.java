package de.topicmapslab.tmql4j.draft2010.interpreter.base;

import java.lang.reflect.Method;
import java.util.Map;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.utility.operation.LiteralUtils;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Abstract interpret implementation of binary string functions.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public abstract class BaseBinaryStringFunctionInterpreter<T extends IExpression>
		extends BaseFunctionInterpreter<T> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression to interpret
	 */
	public BaseBinaryStringFunctionInterpreter(T ex) {
		super(ex);
	}

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
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * extract arguments
		 */
		QueryMatches[] arguments = extractArguments(runtime);

		/*
		 * execute function operation
		 */
		QueryMatches matches = new QueryMatches(runtime);
		for (Object o : arguments[0].getPossibleValuesForVariable()) {
			final String value = LiteralUtils.asString(o);
			/*
			 * check if second arguments are not empty
			 */
			if (!arguments[1].isEmpty()
					&& arguments[1].getOrderedKeys().contains(
							QueryMatches.getNonScopedVariable())) {
				/*
				 * get other string literal
				 */
				final String otherValue = LiteralUtils.asString(arguments[1]
						.getPossibleValuesForVariable().get(0));

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
					result.put(QueryMatches.getNonScopedVariable(),
							toResult(obj));
					matches.add(result);
				} catch (Exception e) {
					throw new TMQLRuntimeException(e);
				}
			}

		}
		/*
		 * store result
		 */
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				matches);
	}
}
