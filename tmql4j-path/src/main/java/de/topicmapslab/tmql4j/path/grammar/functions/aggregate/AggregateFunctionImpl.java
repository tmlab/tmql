/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.grammar.functions.aggregate;

import java.util.Collection;
import java.util.List;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.grammar.functions.FunctionImpl;
import de.topicmapslab.tmql4j.path.grammar.productions.Parameters;
import de.topicmapslab.tmql4j.path.grammar.productions.TupleExpression;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public abstract class AggregateFunctionImpl extends FunctionImpl {

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 2;
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, IExpressionInterpreter<?> caller) {
		IExpression expression = caller.getExpression();
		if (!expression.contains(Parameters.class)) {
			throw new TMQLRuntimeException("The number of arguments is invalid. Two expected but no was found!");
		}

		List<Object> values = HashUtil.getList();
		Parameters parameters = expression.getExpressionFilteredByType(Parameters.class).get(0);
		Collection<Object> nodes;
		IExpressionInterpreter<?> interpreter;
		/*
		 * parameters given as tuple-expression
		 */
		if (parameters.contains(TupleExpression.class)) {
			TupleExpression tupleExpression = parameters.getExpressionFilteredByType(TupleExpression.class).get(0);
			if (!isExpectedNumberOfParameters(tupleExpression.getExpressions().size())) {
				throw new TMQLRuntimeException("The number of arguments is invalid. Two expected but " + tupleExpression.getExpressions().size() + " was found!");
			}
			/*
			 * call first expression to get context of count
			 */
			interpreter = runtime.getLanguageContext().getInterpreterRegistry().interpreterInstance(tupleExpression.getExpressions().get(0));
			QueryMatches matches = interpreter.interpret(runtime, context);
			nodes = matches.getPossibleValuesForVariable();
			/*
			 * get values to aggregate
			 */
			interpreter = runtime.getLanguageContext().getInterpreterRegistry().interpreterInstance(tupleExpression.getExpressions().get(1));
		}
		/*
		 * parameters given as parameters-pair
		 */
		else {
			/*
			 * call first expression to get context of count
			 */
			interpreter = runtime.getLanguageContext().getInterpreterRegistry().interpreterInstance(parameters.getExpressions().get(0));
			QueryMatches matches = interpreter.interpret(runtime, context);
			nodes = matches.getPossibleValuesForVariable();
			/*
			 * get values to aggregate
			 */
			interpreter = runtime.getLanguageContext().getInterpreterRegistry().interpreterInstance(parameters.getExpressions().get(1));
		}

		/*
		 * calculate values
		 */
		Context newContext = new Context(context);
		newContext.setCurrentTuple(null);
		newContext.setContextBindings(null);
		for (Object node : nodes) {
			newContext.setCurrentNode(node);
			QueryMatches results = interpreter.interpret(runtime, newContext);
			values.addAll(results.getPossibleValuesForVariable());
		}
		if ( values.isEmpty()){
			return QueryMatches.emptyMatches();
		}
		return doAggregation(runtime, newContext, values);
	}

	/**
	 * Method called to handle the real aggregation after getting the values
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param values
	 *            the values
	 * @return the aggregated values as query match
	 * @throws TMQLRuntimeException
	 *             thrown if anything fails
	 */
	protected abstract QueryMatches doAggregation(ITMQLRuntime runtime, IContext context, List<Object> values) throws TMQLRuntimeException;

}
