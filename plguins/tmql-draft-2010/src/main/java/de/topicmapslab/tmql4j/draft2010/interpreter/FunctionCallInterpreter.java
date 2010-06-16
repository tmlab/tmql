package de.topicmapslab.tmql4j.draft2010.interpreter;

import java.lang.reflect.Constructor;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.expressions.FunctionCall;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.IFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;

/**
 * Interpreter class of production 'function-call' ( {@link FunctionCall} )
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class FunctionCallInterpreter extends
		ExpressionInterpreterImpl<FunctionCall> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression to interpret
	 */
	public FunctionCallInterpreter(FunctionCall ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		/*
		 * get function interpreter class
		 */
		Class<? extends IFunctionInvocationInterpreter<?>> clazz = runtime
				.getLanguageContext().getFunctionRegistry().getFunction(
						getTokens().get(0));
		if (clazz == null) {
			throw new TMQLRuntimeException("Function identifier '"
					+ getTokens().get(0) + "' is unknown.");
		}

		try {
			/*
			 * instantiate interpreter class
			 */
			Constructor<? extends IFunctionInvocationInterpreter<?>> constructor = clazz
					.getConstructor(FunctionCall.class);
			IFunctionInvocationInterpreter<?> interpreter = constructor
					.newInstance(getExpression());
			/*
			 * run function interpreter
			 */
			interpreter.interpret(runtime);
		} catch (Exception e) {
			throw new TMQLRuntimeException(
					"Function interpreter cannot be instantiate.", e);
		}

	}

}
