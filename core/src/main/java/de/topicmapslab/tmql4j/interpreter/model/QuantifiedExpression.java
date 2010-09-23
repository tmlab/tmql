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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntimeFactory;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.parser.core.expressions.BindingSet;
import de.topicmapslab.tmql4j.parser.core.expressions.BooleanExpression;
import de.topicmapslab.tmql4j.parser.model.IExpression;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;

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
public abstract class QuantifiedExpression<T extends IExpression> extends
		ExpressionInterpreterImpl<T> {

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
	 * @return the context
	 * @throws TMQLRuntimeException
	 *             thrown by interpreter
	 */
	protected QueryMatches getContext(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * check for iteration bindings
		 */
		QueryMatches context = null;

		/*
		 * bindings are provided by upper expression
		 */
		if (runtime.getRuntimeContext().peek().contains(
				VariableNames.ITERATED_BINDINGS)) {
			context = (QueryMatches) runtime.getRuntimeContext()
					.peek().getValue(VariableNames.ITERATED_BINDINGS);
		}
		/*
		 * no context provided by upper expression
		 */
		else {
			TopicMap topicMap = (TopicMap) runtime.getRuntimeContext()
					.peek().getValue(VariableNames.CURRENT_MAP);
			List<String> variables = getInterpretersFilteredByEypressionType(
					runtime, BindingSet.class).get(0).getVariables();
			context = new QueryMatches(runtime);
			if (variables.size() > 1) {
				/*
				 * add all topics as possible binding for extracted variable
				 */
				context.convertToTuples(topicMap.getTopics(), variables.get(1));
			}
		}

		return context;
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
	protected abstract boolean doSatisfy(QueryMatches context,
			QueryMatches results);

	/**
	 * Method checks if the given results of a sub runtime satisfying the
	 * restriction of this quantified expression.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param tuple
	 *            current tuple
	 * @param results
	 *            the results to check
	 * 
	 * @return <code>true</code> if it satisfies, <code>false</code> otherwise.
	 */
	protected abstract boolean doSatisfy(TMQLRuntime runtime,
			Map<String, Object> tuple, IResultSet<?> results);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		QueryMatches results = new QueryMatches(runtime);

		QueryMatches context = getContext(runtime);
		/*
		 * binding-set is in-dependent from any variable
		 */
		if (context.isEmpty()) {
			/*
			 * Call sub-expression binding-set
			 */
			QueryMatches bindingSet = extractArguments(runtime,
					BindingSet.class, 0);
			/*
			 * run boolean-expression
			 */
			runtime.getRuntimeContext().push();
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.ITERATED_BINDINGS, bindingSet);
			QueryMatches matches = extractArguments(runtime,
					BooleanExpression.class, 0);
			runtime.getRuntimeContext().pop();

			/*
			 * check if result satisfies
			 */
			if (doSatisfy(bindingSet, matches)) {
				results.add(bindingSet);
			}else{
				results.getNegation().add(bindingSet);
			}
		}
		/*
		 * binding-set is dependent from any variable
		 */
		else {
			final String query = buildQuery();
			results = interpretQuantifiedExpression(runtime, context, query,
					runtime.getProperties().useMultipleThreads());
		}

		/*
		 * set overall query matches to results
		 */
		runtime.getRuntimeContext().peek().setValue(
				VariableNames.QUERYMATCHES, results);
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
		sb.append("FOR ");
		BindingSet bindingSet = getExpression().getExpressionFilteredByType(
				BindingSet.class).get(0);
		for (String token : bindingSet.getTokens()) {
			sb.append(token + " ");
		}
		sb.append("WHERE ");
		for (String token : getExpression().getExpressionFilteredByType(
				BooleanExpression.class).get(0).getTokens()) {
			sb.append(token + " ");
		}
		sb.append("RETURN " + bindingSet.getVariables().get(0));

		System.out.println(sb.toString());

		return sb.toString();
	}

	/**
	 * Special interpretation method of variable dependent quantified
	 * expression.
	 * 
	 * @param parent
	 *            the parent runtime
	 * @param bindings
	 *            the possible variable bindings
	 * @param multiThreaded
	 *            <code>true</code> if each binding should be executed by its
	 *            own thread
	 * @return the result of interpretation
	 * @throws TMQLRuntimeException
	 *             thrown if at least one runtime fails
	 */
	private QueryMatches interpretQuantifiedExpression(
			final TMQLRuntime parent, final QueryMatches bindings,
			final String query, final boolean multiThreaded)
			throws TMQLRuntimeException {
		final QueryMatches results = new QueryMatches(parent);

		/*
		 * create thread-group
		 */
		ThreadGroup group = new ThreadGroup("tmql4j-internal-"
				+ UUID.randomUUID());
		/*
		 * set of all running threads
		 */
		final Set<Thread> threads = HashUtil.getHashSet();
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
						ITMQLRuntime r = TMQLRuntimeFactory.newFactory()
								.newRuntime(parent.getTopicMapSystem(),
										parent.getTopicMap());

						/*
						 * copy prefixes
						 */
						TMQLRuntime runtime = (TMQLRuntime) r;
						for (Entry<String, String> entry : parent
								.getLanguageContext().getPrefixHandler()
								.getPrefixMap().entrySet()) {
							runtime.getLanguageContext().getPrefixHandler()
									.registerPrefix(entry.getKey(),
											entry.getValue());
						}
						/*
						 * set tuple as LINQ variables
						 */
						runtime.variables.putAll(tuple);

						IQuery q = runtime.run(query);
						IResultSet<?> set = q.getResults();

						/*
						 * satisfy?
						 */
						synchronized (results) {
							if (doSatisfy(parent, tuple, set)) {
								results.add(tuple);
							}else{
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
			threads.add(thread);
			if (multiThreaded) {
				thread.start();
			} else {
				thread.run();
			}

		}

		/*
		 * wait until all threads finished
		 */
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (Thread t : threads) {
				if (t.isAlive()) {
					continue;
				}
			}
			break;
		}

		return results;
	}
}
