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
package de.topicmapslab.tmql4j.components.interpreter;

import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;

import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.module.InterpreterRegistryImpl;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;

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
 * {@link InterpreterRegistryImpl}.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 * @param <T>
 *            the expression type which specialize the extending interpreter
 *            class
 */
public abstract class ExpressionInterpreterImpl<T extends IExpression> implements IExpressionInterpreter<T> {

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

		this.clazz = (Class<T>) ((ParameterizedType) cla.getGenericSuperclass()).getActualTypeArguments()[0];
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
	public List<IExpressionInterpreter<?>> getInterpreters(final ITMQLRuntime runtime) throws TMQLRuntimeException {
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
			interpreters.add(runtime.getLanguageContext().getInterpreterRegistry().interpreterInstance(expression));
		}
		return interpreters;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <E extends IExpression> List<IExpressionInterpreter<E>> getInterpretersFilteredByEypressionType(final ITMQLRuntime runtime, Class<E> type) throws TMQLRuntimeException {
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
			interpreters.add((IExpressionInterpreter<E>) runtime.getLanguageContext().getInterpreterRegistry().interpreterInstance(expression));
		}
		return interpreters;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean containsExpressionsType(Class<? extends IExpression> type) throws TMQLRuntimeException {
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
	protected <E extends IExpression> long getNumberOfArguments(final Class<E> type) {
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
	 * @param context
	 *            the query context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return an array containing all arguments
	 * @throws TMQLRuntimeException
	 *             thrown if execution failed
	 */
	protected <E extends IExpression> QueryMatches[] extractArguments(final ITMQLRuntime runtime, final Class<E> type, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * create new array for all results
		 */
		QueryMatches[] arguments = new QueryMatches[(int) getNumberOfArguments(type)];
		/*
		 * call all sub-expressions of the type
		 */
		for (int index = 0; index < arguments.length; index++) {
			arguments[index] = extractArguments(runtime, type, index, context, optionalArguments);
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
	 * @param context
	 *            the query context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the result of interpreter execution
	 * @throws TMQLRuntimeException
	 *             thrown if execution failed
	 * @param R
	 *            the type the result will be convert to
	 */
	protected <E extends IExpression> QueryMatches extractArguments(final ITMQLRuntime runtime, final Class<E> type, int index, IContext context, Object... optionalArguments)
			throws TMQLRuntimeException {
		/*
		 * get list of argument interpreters
		 */
		List<IExpressionInterpreter<E>> interpreters = getInterpretersFilteredByEypressionType(runtime, type);

		/*
		 * check if number of arguments matches the expected argument count
		 */
		if (interpreters.size() < index) {
			throw new TMQLRuntimeException("Index out of bounds. Expects " + index + " but size is " + interpreters.size());
		}

		/*
		 * interpret contained expression at given position
		 */
		return interpreters.get(index).interpret(runtime, context, optionalArguments);
	}

	/**
	 * Extracts the topic map instance from the current runtime context
	 * 
	 * @param context
	 *            the query context
	 * @return the topic map instance and never <code>null</code>
	 * @throws TMQLRuntimeException
	 *             thrown if topic map reference is missing
	 */
	protected TopicMap getQueriedTopicMap(IContext context) throws TMQLRuntimeException {
		return context.getQuery().getTopicMap();
	}

}
