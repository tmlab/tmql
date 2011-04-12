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
package de.topicmapslab.tmql4j.draft2011.path.components.interpreter;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Desc;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.OrderByClause;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.ValueExpression;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.IndexTuple;

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
public class OrderByClauseInterpreter extends ExpressionInterpreterImpl<OrderByClause> {

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
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		Iterator<IExpressionInterpreter<ValueExpression>> iterator = getInterpretersFilteredByEypressionType(runtime, ValueExpression.class).iterator();

		List<IndexTuple> iteration = sort(runtime, context, iterator.next(), 0, context.getContextBindings().size());

		while (iterator.hasNext()) {
			boolean cancel = true;
			IExpressionInterpreter<ValueExpression> interpreter = iterator.next();
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
						Context newContext = new Context(context);
						newContext.setContextBindings(orderedMatches);
						List<IndexTuple> tuples = sort(runtime, newContext, interpreter, from, index);
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
				Context newContext = new Context(context);
				newContext.setContextBindings(orderedMatches);
				List<IndexTuple> tuples = sort(runtime, newContext, interpreter, from, results.size());
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
		return orderedMatches;
	}

	@SuppressWarnings("unchecked")
	private final List<IndexTuple> sort(final ITMQLRuntime runtime, final IContext context, IExpressionInterpreter<ValueExpression> interpreter, int from, int to) throws TMQLRuntimeException {

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
			Map<String, Object> binding = context.getContextBindings().get(index);
			Context newContext = new Context(context);
			newContext.setCurrentTuple(binding);
			newContext.setContextBindings(null);
			/*
			 * create special tuple saving their index
			 */
			IndexTuple indexTuple = new IndexTuple();
			indexTuple.origin = binding;
			/*
			 * call value-expression
			 */
			QueryMatches matches = interpreter.interpret(runtime, newContext);
			if (matches.isEmpty()) {
				indexTuple.tuple = HashUtil.getHashMap();
			} else {
				indexTuple.tuple = matches.getMatches().get(0);
			}
			results.add(indexTuple);
		}

		/*
		 * extract sorting direction
		 */
		final boolean ascending = !ParserUtils.containsTokens(interpreter.getTmqlTokens(), Desc.class);

		/*
		 * sort the tuples
		 */
		Collections.sort(results, new Comparator<IndexTuple>() {
			public int compare(IndexTuple o1, IndexTuple o2) {
				int compare = o1.compareTo(o2);
				return ascending ?  compare : -1 * compare;
			}
		});

		return results;
	}
}
