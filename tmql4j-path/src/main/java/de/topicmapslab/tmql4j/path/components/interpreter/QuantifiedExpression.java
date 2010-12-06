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

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.tmapi.core.Construct;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.results.IResultSet;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.TMQLRuntimeFactory;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketSquareClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketSquareOpen;
import de.topicmapslab.tmql4j.path.grammar.lexical.Dot;
import de.topicmapslab.tmql4j.path.grammar.productions.BindingSet;
import de.topicmapslab.tmql4j.path.grammar.productions.BooleanExpression;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * 
 * Special interpreter class to interpret quantified expressions
 * 
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public abstract class QuantifiedExpression<T extends IExpression> extends ExpressionInterpreterImpl<T> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public QuantifiedExpression(T ex) {
		super(ex);
	}

	/**
	 * Method extracts the context of the quantified expression.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @return the context
	 * @throws TMQLRuntimeException
	 *             thrown by interpreter
	 */
	protected QueryMatches getBindingsContext(ITMQLRuntime runtime, IContext context) throws TMQLRuntimeException {
		/*
		 * check for iteration bindings
		 */
		QueryMatches result = null;

		/*
		 * bindings are provided by upper expression
		 */
		if (context.getContextBindings() != null) {
			result = context.getContextBindings();
		}
		/*
		 * no context provided by upper expression
		 */
		else {
			TopicMap topicMap = context.getQuery().getTopicMap();
			List<String> variables = getInterpretersFilteredByEypressionType(runtime, BindingSet.class).get(0).getVariables();
			result = new QueryMatches(runtime);
			if (variables.size() > 1) {
				/*
				 * add all topics as possible binding for extracted variable
				 */
				result.convertToTuples(topicMap.getTopics(), variables.get(1));
			}
		}
		return result;
	}

	/**
	 * Method checks if the given query matches satisfying the restriction of
	 * this quantified expression.
	 * 
	 * @param context
	 *            the context of the quantified expression
	 * @param results
	 *            the query match to check
	 * 
	 * @return <code>true</code> if it satisfies, <code>false</code> otherwise.
	 */
	protected abstract boolean doSatisfy(QueryMatches context, QueryMatches results);

	/**
	 * Method checks if the given results of a sub runtime satisfying the
	 * restriction of this quantified expression.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param results
	 *            the results to check
	 * 
	 * @return <code>true</code> if it satisfies, <code>false</code> otherwise.
	 */
	protected abstract boolean doSatisfy(ITMQLRuntime runtime, IContext context, IResultSet<?> results);

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		QueryMatches results = new QueryMatches(runtime);

		QueryMatches bindingsContext = getBindingsContext(runtime, context);
		/*
		 * binding-set is in-dependent from any variable
		 */
		if (bindingsContext.isEmpty()) {
			/*
			 * Call sub-expression binding-set
			 */
			QueryMatches bindingSet = extractArguments(runtime, BindingSet.class, 0, context);

			Context newContext = new Context(context);
			newContext.setContextBindings(bindingSet);
			/*
			 * run boolean-expression
			 */
			QueryMatches matches = extractArguments(runtime, BooleanExpression.class, 0, newContext);

			/*
			 * check if result satisfies
			 */
			if (doSatisfy(bindingSet, matches)) {
				results.add(bindingSet);
			} else {
				results.getNegation().add(bindingSet);
			}
		}
		/*
		 * binding-set is dependent from any variable
		 */
		else {
			final String query = buildQuery();
			results = interpretQuantifiedExpression(runtime, context, bindingsContext, query, false);
		}

		return results;
	}

	/**
	 * Method creates a query representing the quantified expression as
	 * stand-alone query.
	 * 
	 * @return the generated query
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	protected String buildQuery() throws TMQLRuntimeException {
		/*
		 * create query string
		 */
		final StringBuilder sb = new StringBuilder();

		BindingSet bindingSet = getExpression().getExpressionFilteredByType(BindingSet.class).get(0);
		final String variable = bindingSet.getVariables().get(0);
		for (int i = 2; i < bindingSet.getTokens().size(); i++) {
			sb.append(bindingSet.getTokens().get(i));
			sb.append(" ");
		}
		sb.append(BracketSquareOpen.TOKEN);
		sb.append(" ");
		for (String token : getExpression().getExpressionFilteredByType(BooleanExpression.class).get(0).getTokens()) {
			if (token.equals(variable)) {
				sb.append(Dot.TOKEN);
			} else {
				sb.append(token);
			}
			sb.append(" ");
		}
		sb.append(" ");
		sb.append(BracketSquareClose.TOKEN);
		return sb.toString();
	}

	/**
	 * Special interpretation method of variable dependent quantified
	 * expression.
	 * 
	 * @param parent
	 *            the parent runtime
	 * @param context
	 *            the querying context
	 * @param bindings
	 *            the possible variable bindings
	 * @param multiThreaded
	 *            <code>true</code> if each binding should be executed by its
	 *            own thread
	 * @return the result of interpretation
	 * @throws TMQLRuntimeException
	 *             thrown if at least one runtime fails
	 */
	private QueryMatches interpretQuantifiedExpression(final ITMQLRuntime parent, final IContext context, final QueryMatches bindings, final String query, final boolean multiThreaded)
			throws TMQLRuntimeException {
		final QueryMatches results = new QueryMatches(parent);

		/*
		 * create thread-group
		 */
		ThreadGroup group = new ThreadGroup("tmql4j-internal-" + UUID.randomUUID());
		/*
		 * set of all running threads
		 */
		ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(4 * Runtime.getRuntime().availableProcessors());
		/*
		 * iterate over all bindings
		 */
		for (final Map<String, Object> tuple : bindings) {
			/*
			 * create thread
			 */
			Thread thread = new Thread(group, "tmql4j-" + UUID.randomUUID()) {
				public void run() {
					try {

						/*
						 * create new embed runtime
						 */
						ITMQLRuntime runtime = TMQLRuntimeFactory.newFactory().newRuntime(parent.getTopicMapSystem());

						/*
						 * copy prefixes
						 */
						for (Entry<String, String> entry : parent.getLanguageContext().getPrefixHandler().getPrefixMap().entrySet()) {
							runtime.getLanguageContext().getPrefixHandler().registerPrefix(entry.getKey(), entry.getValue());
						}

						String query_ = new String(query);
						for (Entry<String, Object> entry : tuple.entrySet()) {
							final String variable = "\\" + entry.getKey();
							final String value;
							if (entry.getValue() instanceof Construct) {
								value = " \"" + ((Construct) entry.getValue()).getId() + "\" << id";
							} else {
								value = "\"" + entry.getValue().toString() + "\"";
							}
							query_ = query_.replaceAll(variable, value);

						}

						IQuery q = runtime.run(context.getQuery().getTopicMap(), query_);
						IResultSet<?> set = q.getResults();

						/*
						 * satisfy?
						 */
						synchronized (results) {
							Context newContext = new Context(context);
							newContext.setCurrentTuple(tuple);
							newContext.setContextBindings(null);
							if (doSatisfy(parent, newContext, set)) {
								results.add(tuple);
							} else {
								results.getNegation().add(tuple);
							}
						}
					} catch (TMQLRuntimeException ex) {
						ex.printStackTrace();
					}
				}
			};
			/*
			 * run thread
			 */
			if (multiThreaded) {
				threadPool.execute(thread);
			} else {
				thread.run();
			}

		}

		if (multiThreaded) {
			while (threadPool.getActiveCount() > 0) {
				// WAIT
			}
			threadPool.shutdown();
		}

		return results;
	}
}
