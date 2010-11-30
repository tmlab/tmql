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
package de.topicmapslab.tmql4j.interpreter.model;

import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;

import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.common.context.InterpreterRegistry;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.context.IVariableSet;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Base implementation of an {@link IExpressionInterpreter}.
 * <p>
 * Every implementation of an {@link IExpression} interpreter is specialized to
 * interpret an instance of {@link IExpression}. During the querying process the
 * generated parser tree containing a tree-structured representation of the
 * query will be interpreted incremental.
 * </p>
 * <p>
 * An Instance of {@link IExpression} will be an argument of the interpretation
 * process of a {@link IExpressionInterpreter} which will be instantiate by the
 * {@link InterpreterRegistry}.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 * @param <T>
 *            the expression type which specialize the extending interpreter
 *            class
 */
public abstract class ExpressionInterpreterImpl<T extends IExpression>
		implements IExpressionInterpreter<T> {

	/**
	 * the internal expression
	 */
	private final T ex;
	/**
	 * the type specified by type parameter T
	 */
	private final Class<T> clazz;

	/**
	 * base constructor to create a new instance.
	 * 
	 * @param ex
	 *            the expression which shall be handled by this instance.
	 */
	@SuppressWarnings("unchecked")
	public ExpressionInterpreterImpl(T ex) {
		this.ex = ex;
		/*
		 * extract type information
		 */
		Class<?> cla = getClass();
		while (!(cla.getGenericSuperclass() instanceof ParameterizedType)) {
			cla = cla.getSuperclass();
		}

		this.clazz = (Class<T>) ((ParameterizedType) cla.getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	/**
	 * {@inheritDoc}
	 */
	public T getExpression() {
		return ex;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<IExpressionInterpreter<?>> getInterpreters(
			final TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * temporary list of results
		 */
		List<IExpressionInterpreter<?>> interpreters = new LinkedList<IExpressionInterpreter<?>>();
		/*
		 * iterate over expressions
		 */
		for (IExpression expression : ex.getExpressions()) {
			/*
			 * create new interpreter instance
			 */
			interpreters.add(runtime.getLanguageContext()
					.getInterpreterRegistry().interpreterInstance(expression));
		}
		return interpreters;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <E extends IExpression> List<IExpressionInterpreter<E>> getInterpretersFilteredByEypressionType(
			final TMQLRuntime runtime, Class<E> type)
			throws TMQLRuntimeException {
		/*
		 * temporary list of results
		 */
		List<IExpressionInterpreter<E>> interpreters = new LinkedList<IExpressionInterpreter<E>>();
		/*
		 * iterate over expressions
		 */
		for (IExpression expression : ex.getExpressionFilteredByType(type)) {
			/*
			 * create new interpreter instance
			 */
			interpreters.add((IExpressionInterpreter<E>) runtime
					.getLanguageContext().getInterpreterRegistry()
					.interpreterInstance(expression));
		}
		return interpreters;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean containsExpressionsType(Class<? extends IExpression> type)
			throws TMQLRuntimeException {
		return !ex.getExpressionFilteredByType(type).isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getTokens() {
		return ex.getTokens();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getVariables() {
		return ex.getVariables();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Class<? extends IToken>> getTmqlTokens() {
		return ex.getTmqlTokens();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getGrammarTypeOfExpression() {
		return ex.getGrammarType();
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<T> getTypeOfExpression() {
		return clazz;
	}

	/**
	 * Returns the number of sub-expressions to call
	 * 
	 * @param type
	 *            the type of sub-expressions to call
	 * @return the number of sub-expressions to call
	 */
	protected <E extends IExpression> long getNumberOfArguments(
			final Class<E> type) {
		return getExpression().getExpressionFilteredByType(type).size();
	}

	/**
	 * Method extracts the number of expected arguments by calling the contained
	 * expressions
	 * 
	 * @param runtime
	 *            the current runtime
	 * @param type
	 *            the type of sub-expressions to call
	 * @return an array containing all arguments
	 * @throws TMQLRuntimeException
	 *             thrown if execution failed
	 */
	protected <E extends IExpression> QueryMatches[] extractArguments(
			final TMQLRuntime runtime, final Class<E> type)
			throws TMQLRuntimeException {
		/*
		 * create new array for all results
		 */
		QueryMatches[] arguments = new QueryMatches[(int) getNumberOfArguments(type)];
		/*
		 * call all sub-expressions of the type
		 */
		for (int index = 0; index < arguments.length; index++) {
			arguments[index] = extractArguments(runtime, type, index);
		}

		return arguments;
	}

	/**
	 * Method extracts the arguments by calling the contained expression at the
	 * specific index
	 * 
	 * @param runtime
	 *            the current runtime
	 * @param type
	 *            the type of sub-expressions to call
	 * @param index
	 *            index of sub-expression to call
	 * @return the result of interpreter execution
	 * @throws TMQLRuntimeException
	 *             thrown if execution failed
	 */
	protected <E extends IExpression> QueryMatches extractArguments(
			final TMQLRuntime runtime, final Class<E> type, int index)
			throws TMQLRuntimeException {
		return extractArguments(runtime, type, index,
				VariableNames.QUERYMATCHES);
	}

	/**
	 * Method extracts the arguments by calling the contained expression at the
	 * specific index
	 * 
	 * @param runtime
	 *            the current runtime
	 * @param type
	 *            the type of sub-expressions to call
	 * @param index
	 *            index of sub-expression to call
	 * @param variable
	 *            the name of the variable to extract as result of the called
	 *            sub-expression
	 * @return the result of interpreter execution
	 * @throws TMQLRuntimeException
	 *             thrown if execution failed
	 * @param E
	 *            the type of the sub-expression to call
	 * @param R
	 *            the type the result will be convert to
	 */
	protected <E extends IExpression, R extends Object> R extractArguments(
			final TMQLRuntime runtime, final Class<E> type, int index,
			final String variable) throws TMQLRuntimeException {
		/*
		 * get list of argument interpreters
		 */
		List<IExpressionInterpreter<E>> interpreters = getInterpretersFilteredByEypressionType(
				runtime, type);

		/*
		 * check if number of arguments matches the expected argument count
		 */
		if (interpreters.size() < index) {
			throw new TMQLRuntimeException("Index out of bounds. Expects "
					+ index + " but size is " + interpreters.size());
		}

		/*
		 * interpret contained expression at given position
		 */
		Object obj = extractArguments(runtime, interpreters.get(index), variable);
		return (R) obj;		
	}
	
	/**
	 * Method extracts the arguments by calling the contained expression at the
	 * specific index
	 * 
	 * @param runtime
	 *            the current runtime
	 * @param interpreter
	 *            the interpreter to call
	 * @param variable
	 *            the name of the variable to extract as result of the called
	 *            sub-expression
	 * @return the result of interpreter execution
	 * @throws TMQLRuntimeException
	 *             thrown if execution failed
	 * @param E
	 *            the type of the sub-expression to call
	 * @param R
	 *            the type the result will be convert to
	 */
	@SuppressWarnings("unchecked")
	protected <E extends IExpression, R extends Object> R extractArguments(
			final TMQLRuntime runtime, IExpressionInterpreter<E> interpreter,
			final String variable) throws TMQLRuntimeException {
		/*
		 * interpret contained expression at given position
		 */
		runtime.getRuntimeContext().push();
		interpreter.interpret(runtime);
		/*
		 * extract results of execution
		 */
		IVariableSet set = runtime.getRuntimeContext().pop();
		if (!set.contains(variable)) {
			throw new TMQLRuntimeException(
					"Missing interpretation result of expression. Should be bound to variable '"
							+ variable + "'s.");
		}

		return (R) set.getValue(variable);
	}

	/**
	 * Extracts the topic map instance from the current runtime context
	 * 
	 * @param runtime
	 *            the runtime containing the runtime context
	 * @return the topic map instance and never <code>null</code>
	 * @throws TMQLRuntimeException
	 *             thrown if topic map reference is missing
	 */
	protected TopicMap getQueriedTopicMap(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		return runtime.getRuntimeContext().getInitialContext()
				.getQueriedTopicMap();
	}

}
