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

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.util.HashUtil;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.path.grammar.lexical.Desc;
import de.topicmapslab.tmql4j.path.grammar.productions.OrderByClause;
import de.topicmapslab.tmql4j.path.grammar.productions.ValueExpression;


/**
 * 
 * Special interpreter class to interpret order-by-clauses.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * order-by-clause ::=  ORDER BY &lt;value-expression [ ASC | DESC ]&gt;
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class OrderByClauseInterpreter extends
		ExpressionInterpreterImpl<OrderByClause> {
	
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
	public OrderByClauseInterpreter(OrderByClause ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		/*
		 * Log it
		 */
		logger.info("Start");

		/*
		 * get value of variable %_order from the top of the stack
		 */
		Object obj = runtime.getRuntimeContext().peek().getValue(
				VariableNames.ORDER);
		/*
		 * check binded value, has to be an instance of Map<String,
		 * ITupleSequence<?>>
		 */
		QueryMatches context = (QueryMatches) obj;

		Iterator<IExpressionInterpreter<ValueExpression>> iterator = getInterpretersFilteredByEypressionType(
				runtime, ValueExpression.class).iterator();

		List<IndexTuple> iteration = sort(runtime, context, iterator.next(), 0,
				context.size());

		while (iterator.hasNext()) {
			boolean cancel = true;
			IExpressionInterpreter<ValueExpression> interpreter = iterator
					.next();
			List<IndexTuple> results = new LinkedList<IndexTuple>();
			results.addAll(iteration);
			iteration.clear();

			/*
			 * add ordered bindings
			 */
			QueryMatches orderedMatches = new QueryMatches(runtime);
			for (IndexTuple tuple : results) {
				orderedMatches.add(tuple.origin);
			}

			int from = 0;
			IndexTuple tuple = results.get(0);
			for (int index = 1; index < results.size(); index++) {
				IndexTuple t = results.get(index);
				if (tuple.compareTo(t) != 0) {
					if (index - from > 1) {
						List<IndexTuple> tuples = sort(runtime, orderedMatches,
								interpreter, from, index);
						iteration.addAll(tuples);
						cancel = false;
					} else {
						iteration.add(tuple);
					}
					from = index;
				}
				tuple = t;
			}

			if (results.size() - from > 1) {
				List<IndexTuple> tuples = sort(runtime, orderedMatches,
						interpreter, from, results.size());
				iteration.addAll(tuples);
				cancel = false;
			}

			if (cancel) {
				break;
			}
		}

		/*
		 * add ordered bindings
		 */
		QueryMatches orderedMatches = new QueryMatches(runtime);
		for (IndexTuple tuple : iteration) {
			orderedMatches.add(tuple.origin);
		}

		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				orderedMatches);
	}

	@SuppressWarnings("unchecked")
	private final List<IndexTuple> sort(final TMQLRuntime runtime,
			final QueryMatches context,
			IExpressionInterpreter<ValueExpression> interpreter, int from,
			int to) throws TMQLRuntimeException {

		/*
		 * initialize the cache of results of value-expression
		 */
		List<IndexTuple> results = new LinkedList<IndexTuple>();

		/*
		 * iterate over possible bindings
		 */
		for (int index = from; index < to; index++) {
			/*
			 * get binding at current index
			 */
			Map<String, Object> binding = context.get(index);
			/*
			 * push to stack
			 */
			runtime.getRuntimeContext().push();

			/*
			 * iterate over tuple
			 */
			for (Entry<String, Object> entry : binding.entrySet()) {
				/*
				 * set variable to value
				 */
				runtime.getRuntimeContext().peek().setValue(entry.getKey(),
						entry.getValue());
			}

			/*
			 * call value-expression
			 */
			interpreter.interpret(runtime);

			/*
			 * pop from stack
			 */
			Object obj = runtime.getRuntimeContext().pop().getValue(
					VariableNames.QUERYMATCHES);
			/*
			 * check result type. has to be an instance of ITupleSequence<?>
			 */
			if (!(obj instanceof QueryMatches)) {
				throw new TMQLRuntimeException(
						"Invalid interpretation of expression value-expression. Has to return an instance of QueryMatches");
			}

			/*
			 * Check size of tuple-sequence, has to be one
			 */
			QueryMatches matches = (QueryMatches) obj;
			if (matches.isEmpty()) {
				Map<String, Object> map = HashUtil.getHashMap();
				matches.add(map);
			}
			/*
			 * create special tuple saving their index
			 */
			IndexTuple indexTuple = new IndexTuple();
			indexTuple.origin = binding;
			indexTuple.tuple = matches.getMatches().get(0);
			results.add(indexTuple);
		}

		/*
		 * extract sorting direction
		 */
		final boolean ascending = !ParserUtils.containsTokens(interpreter
				.getTmqlTokens(), Desc.class);

		/*
		 * sort the tuples
		 */
		Collections.sort(results, new Comparator<IndexTuple>() {
			public int compare(IndexTuple o1, IndexTuple o2) {
				int compare = o1.compareTo(o2);
				return ascending ? -1 * compare : compare;
			}
		});

		return results;
	}

	/**
	 * Internal class representing a tuple and saving the current index at the
	 * tuple sequence
	 * 
	 * @author Sven Krosse
	 * @email krosse@informatik.uni-leipzig.de
	 * 
	 */
	private class IndexTuple implements Comparable<IndexTuple> {
		/**
		 * the index at the tuple-sequence
		 */
		public Map<String, Object> origin;
		/**
		 * the tuple
		 */
		public Map<String, Object> tuple;

		/**
		 * {@inheritDoc}
		 */
		public int compareTo(IndexTuple o) {
			if (tuple.isEmpty()) {
				return -1;
			}
			if (o.tuple.isEmpty()) {
				return 1;
			}
			return o.tuple.get(QueryMatches.getNonScopedVariable()).toString()
					.compareTo(
							tuple.get(QueryMatches.getNonScopedVariable())
									.toString());
		}
	}
}
