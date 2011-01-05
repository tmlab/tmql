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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.productions.AliasValueExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.TupleExpression;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * 
 * Special interpreter class to interpret tuple-expressions.
 * 
 * <p>
 * tuple-expression ::= ( < alias-value-expression > )
 * </p>
 * <p>
 * tuple-expression ::= null ==> ( )
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TupleExpressionInterpreter extends ExpressionInterpreterImpl<TupleExpression> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public TupleExpressionInterpreter(TupleExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {

		/*
		 * switch by grammar type
		 */
		switch (getGrammarTypeOfExpression()) {
		/*
		 * is alias-value-expression
		 */
		case 0: {
			return interpretAliasValueExpression(runtime, context, optionalArguments);
		}
			/*
			 * is null
			 */
		case 1: {
			return interpretNull(runtime, context, optionalArguments);
		}
		}
		;
		return QueryMatches.emptyMatches();
	}

	/**
	 * The method is called to interpret the given sub-expression by using the
	 * given runtime. The interpretation will call the sub-expression if the
	 * given expression isn't a leaf in parsing-tree.
	 * 
	 * <p>
	 * The interpretation will transform the value on top of the stack and put
	 * its results also on top.
	 * </p>
	 * 
	 * @param runtime
	 *            the runtime which contains all necessary information for
	 *            querying process
	 * @param context
	 *            the current querying context
	 * @param optionalArguments
	 *            optional arguments
	 * @return the query matches
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretAliasValueExpression(final ITMQLRuntime runtime, final IContext context, final Object... optionalArguments) throws TMQLRuntimeException {
		List<Future<Object>> tasks = new LinkedList<Future<Object>>();
		ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4);

		final List<String> variables = getVariables();

		final List<IExpressionInterpreter<AliasValueExpression>> interpreters = getInterpretersFilteredByEypressionType(runtime, AliasValueExpression.class);
		int index = 0;
		for (final IExpressionInterpreter<AliasValueExpression> interpreter : interpreters) {
			final int index_ = index;
			Callable<Object> callable = new Callable<Object>() {
				/**
				 * {@inheritDoc}
				 */
				public Object call() throws Exception {
					Context newContext = new Context(context);
					newContext.setCurrentIndexInTuple(index_);
					QueryMatches result = interpreter.interpret(runtime, newContext, optionalArguments);
					final List<String> keys = result.getOrderedKeys();
					if (result.isEmpty()) {
						return null;
					}
					/*
					 * check if expression contains variables
					 */
					if (!variables.isEmpty()) {
						/*
						 * variable name of the expression
						 */
						final String variable = variables.get(0);
						/*
						 * contains non-scoped stuff
						 */
						if (keys.contains(QueryMatches.getNonScopedVariable())) {
							List<Object> possibleValuesForVariable = result.getPossibleValuesForVariable();
							return possibleValuesForVariable.size() == 1 ? possibleValuesForVariable.get(0) : possibleValuesForVariable;
						}
						/*
						 * contains value stuff
						 */
						else if (keys.contains(variable)) {
							List<Object> possibleValuesForVariable = result.getPossibleValuesForVariable(variable);
							return possibleValuesForVariable.size() == 1 ? possibleValuesForVariable.get(0) : possibleValuesForVariable;
						}
						/*
						 * no results
						 */
						else if (interpreters.size() != 1) {
							return null;
						}
					}
					/*
					 * variable independent
					 */
					else {
						List<Object> possibleValuesForVariable = result.getPossibleValuesForVariable();
						if (possibleValuesForVariable.isEmpty()) {
							return result;
						}
						return possibleValuesForVariable.size() == 1 ? possibleValuesForVariable.get(0) : possibleValuesForVariable;
					}
					return null;
				}
			};
			tasks.add(threadPool.submit(callable));
			index++;
		}

		threadPool.shutdown();

		/*
		 * create result
		 */
		QueryMatches results = new QueryMatches(runtime);
		index = 0;
		Map<String, Object> tuple = HashUtil.getHashMap();
		for (Future<Object> task : tasks) {
			try {
				Object result = task.get();
				if (result == null && tasks.size() == 1) {
					return QueryMatches.emptyMatches();
				} else if (tasks.size() == 1 && result instanceof Collection<?>) {
					return QueryMatches.asQueryMatch(runtime, "$0", result);
				} else if (result instanceof QueryMatches) {
					return (QueryMatches) result;
				}
				tuple.put("$" + index++, result);
			} catch (InterruptedException e) {
				throw new TMQLRuntimeException(e);
			} catch (ExecutionException e) {
				throw new TMQLRuntimeException(e);
			}
		}
		results.add(tuple);
		return results;
	}

	/**
	 * The method is called to interpret the given sub-expression by using the
	 * given runtime. The interpretation will call the sub-expression if the
	 * given expression isn't a leaf in parsing-tree.
	 * 
	 * <p>
	 * The interpretation will transform the value on top of the stack and put
	 * its results also on top.
	 * </p>
	 * 
	 * @param runtime
	 *            the runtime which contains all necessary information for
	 *            querying process
	 * @param context
	 *            the current querying context
	 * @param optionalArguments
	 *            optional arguments
	 * @return the query matches
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretNull(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * set empty tuple sequence
		 */
		return QueryMatches.emptyMatches();
	}

}
