/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.components.interpreter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.productions.FunctionInvocation;

/**
 * 
 * Special interpreter class to interpret function-invocations.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * function-invocation ::= item-reference parameters
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class FunctionInvocationInterpreter extends
		ExpressionInterpreterImpl<FunctionInvocation> {

	/**
	 * internal function interpreter
	 */
	private IFunctionInvocationInterpreter<?> interpreter;
	
	
	/**
	 * the Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
	

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public FunctionInvocationInterpreter(FunctionInvocation ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		logger.info("Start");

		final String identifier = getTokens().get(0);

		/*
		 * try to extract interpreter for given identifier
		 */
		if (interpreter == null) {
			Class<? extends IFunctionInvocationInterpreter<?>> clazz = runtime
					.getLanguageContext().getFunctionRegistry().getFunction(
							identifier);

			try {
				/*
				 * try to instantiate the responsible function interpreter
				 */
				Constructor<? extends IFunctionInvocationInterpreter<?>> constructor = clazz
						.getConstructor(FunctionInvocation.class);
				interpreter = constructor.newInstance(getExpression());

			} catch (SecurityException e) {
				throw new TMQLRuntimeException(
						"Internal error, during initilaization of function-invocation-interpreter of "
								+ getTokens().get(0), e);
			} catch (NoSuchMethodException e) {
				throw new TMQLRuntimeException(
						"Internal error, during initilaization of function-invocation-interpreter of "
								+ getTokens().get(0), e);
			} catch (IllegalArgumentException e) {
				throw new TMQLRuntimeException(
						"Internal error, during initilaization of function-invocation-interpreter of "
								+ getTokens().get(0), e);
			} catch (InstantiationException e) {
				throw new TMQLRuntimeException(
						"Internal error, during initilaization of function-invocation-interpreter of "
								+ getTokens().get(0), e);
			} catch (IllegalAccessException e) {
				throw new TMQLRuntimeException(
						"Internal error, during initilaization of function-invocation-interpreter of "
								+ getTokens().get(0), e);
			} catch (InvocationTargetException e) {
				throw new TMQLRuntimeException(
						"Internal error, during initilaization of function-invocation-interpreter of "
								+ getTokens().get(0), e);
			}
		}

		/*
		 * call interpreter
		 */
		runtime.getRuntimeContext().push();

		interpreter.interpret(runtime);

		/*
		 * redirect results
		 */
		IVariableSet set = runtime.getRuntimeContext().pop();
		runtime.getRuntimeContext().peek().setValue(
				VariableNames.QUERYMATCHES,
				set.getValue(VariableNames.QUERYMATCHES));

	}
}
