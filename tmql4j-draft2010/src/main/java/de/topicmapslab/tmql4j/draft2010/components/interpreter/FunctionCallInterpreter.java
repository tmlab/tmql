package de.topicmapslab.tmql4j.draft2010.components.interpreter;

import java.lang.reflect.Constructor;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.FunctionCall;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IFunction;

/**
 * Interpreter class of production 'function-call' ( {@link FunctionCall} )
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class FunctionCallInterpreter extends ExpressionInterpreterImpl<FunctionCall> {

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
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * get function interpreter class
		 */
		Class<? extends IFunction> clazz = runtime.getLanguageContext().getFunctionRegistry().getFunction(getTokens().get(0));
		if (clazz == null) {
			throw new TMQLRuntimeException("Function identifier '" + getTokens().get(0) + "' is unknown.");
		}

		try {
			/*
			 * instantiate interpreter class
			 */
			Constructor<? extends IFunction> constructor = clazz.getConstructor();
			IFunction interpreter = constructor.newInstance();
			/*
			 * run function interpreter
			 */
			return interpreter.interpret(runtime, context, this);
		} catch (Exception e) {
			throw new TMQLRuntimeException("Function interpreter cannot be instantiate.", e);
		}

	}

}
