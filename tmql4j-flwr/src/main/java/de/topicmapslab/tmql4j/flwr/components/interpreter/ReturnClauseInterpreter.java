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
package de.topicmapslab.tmql4j.flwr.components.interpreter;

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
import de.topicmapslab.tmql4j.flwr.grammar.productions.ReturnClause;
import de.topicmapslab.tmql4j.path.grammar.productions.Content;
import de.topicmapslab.tmql4j.path.grammar.productions.PathExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.PostfixedExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.QueryExpression;

/**
 * 
 * Special interpreter class to interpret return-clauses.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * return-clause ::= RETURN   content;
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ReturnClauseInterpreter extends ExpressionInterpreterImpl<ReturnClause> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public ReturnClauseInterpreter(ReturnClause ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(final ITMQLRuntime runtime, final IContext context, final Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * get content-interpreter
		 */
		final IExpressionInterpreter<?> interpreter = getInterpreters(runtime).get(0);

		final QueryMatches results;
		/*
		 * check if context is given by upper FLWR-expression ( where-clause )
		 */
		if (context.getContextBindings() != null) {
			results = new QueryMatches(runtime);
			int index = 0;
			List<Future<QueryMatches>> list = new LinkedList<Future<QueryMatches>>();
			ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4);
			/*
			 * iterate over all tuples
			 */
			for (final Map<String, Object> tuple : context.getContextBindings()) {
				final int index_ = index;
				Callable<QueryMatches> callable = new Callable<QueryMatches>() {

					/**
					 * {@inheritDoc}
					 */
					public QueryMatches call() throws Exception {
						Context newContext = new Context(context);
						/*
						 * get value of content
						 */
						String variable = QueryMatches.getNonScopedVariable();
						if (!interpreter.getVariables().isEmpty()) {
							variable = interpreter.getVariables().get(0);
						}
						Object match = tuple.get(variable);

						newContext.setContextBindings(null);
						newContext.setCurrentTuple(tuple);
						newContext.setCurrentNode(match);
						newContext.setCurrentIndex(index_);
						/*
						 * call sub-expression
						 */
						QueryMatches matches = interpreter.interpret(runtime, newContext, optionalArguments);
						return matches;
					}
				};
				index++;
				list.add(threadPool.submit(callable));
			}

			threadPool.shutdown();
			/*
			 * catch results
			 */
			for (Future<QueryMatches> future : list) {
				try {
					QueryMatches matches = future.get();
					if (matches.isEmpty()) {
						throw new TMQLRuntimeException("Invalid state of internal task!");
					}
					results.add(matches);
				} catch (InterruptedException e) {
					throw new TMQLRuntimeException(e);
				} catch (ExecutionException e) {
					throw new TMQLRuntimeException(e);
				}

			}

		}
		/*
		 * no context given by FLWR-expression
		 */
		else {
			/*
			 * call sub-expression
			 */
			results = interpreter.interpret(runtime, context, optionalArguments);
		}
		if (results.isEmpty()) {
			return QueryMatches.emptyMatches();
		}
		return clean(results);
	}

	/**
	 * Method removes all other variables bindings than the non-scoped variable.
	 * 
	 * @param matches
	 *            the matches to clean
	 * @return the cleaned query matches
	 * @throws TMQLRuntimeException
	 */
	private QueryMatches clean(QueryMatches matches) throws TMQLRuntimeException {
		List<Content> expressionFilteredByType = getExpression().getExpressionFilteredByType(Content.class);
		if (!expressionFilteredByType.isEmpty()) {
			Content content = expressionFilteredByType.get(0);
			if (content.getGrammarType() == Content.TYPE_QUERY_EXPRESSION) {
				List<QueryExpression> queryExpressions = content.getExpressionFilteredByType(QueryExpression.class);
				if (!queryExpressions.isEmpty()) {
					QueryExpression qEx = queryExpressions.get(0);
					List<PathExpression> pathExpressions = qEx.getExpressionFilteredByType(PathExpression.class);
					if (!pathExpressions.isEmpty()) {
						PathExpression pEx = pathExpressions.get(0);
						if (pEx.getGrammarType() == PathExpression.TYPE_POSTFIXED_EXPRESSION) {
							List<PostfixedExpression> postfixedExpressions = pEx.getExpressionFilteredByType(PostfixedExpression.class);
							if (!postfixedExpressions.isEmpty()) {
								PostfixedExpression pfEx = postfixedExpressions.get(0);
								if (pfEx.getGrammarType() == PostfixedExpression.TYPE_SIMPLE_CONTENT) {
									return matches.extractAndRenameBindingsForVariable(QueryMatches.getNonScopedVariable());
								}
							}
						}
					}
				}
			}
		}
		return matches;
	}

}
