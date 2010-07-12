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
package de.topicmapslab.tmql4j.interpreter.core.interpreter;

import java.util.Map;
import java.util.Set;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.model.tuplesequence.ITupleSequence;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.parser.core.expressions.TupleExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.ValueExpression;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * 
 * Special interpreter class to interpret tuple-expressions.
 * 
 * <p>
 * tuple-expression ::= ( < value-expression [ asc | desc ] > )
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
public class TupleExpressionInterpreter extends
		ExpressionInterpreterImpl<TupleExpression> {

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
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * switch by grammar type
		 */
		switch (getGrammarTypeOfExpression()) {
		/*
		 * is value-expression
		 */
		case 0: {
			interpretValueExpression(runtime);
		}
			break;
		/*
		 * is null
		 */
		case 1: {
			interpretNull(runtime);
		}
			break;
		}
		;
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
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private void interpretValueExpression(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * variable store of multiple tuple-expressions
		 */
		Map<String, Object> tuple = HashUtil.getHashMap();
		/*
		 * store mappings
		 */
		Map<String, String> origins = HashUtil.getHashMap();
		/*
		 * results store of singleton tuple-expressions
		 */
		Set<QueryMatches> matches = HashUtil.getHashSet();

		QueryMatches[] context = extractArguments(runtime,
				ValueExpression.class);
		for (int index = 0; index < context.length; index++) {
			/*
			 * get corresponding expression
			 */
			IExpression ex = getExpression().getExpressionFilteredByType(
					ValueExpression.class).get(index);

			QueryMatches result = context[index]
					.extractAndRenameBindingsForVariable("$" + (index));
			/*
			 * add sequence
			 */
			ITupleSequence<Object> values = context[index]
					.getPossibleValuesForVariable();
			/*
			 * check if expression contains variables
			 */
			if (!ex.getVariables().isEmpty()) {
				/*
				 * variable name of the expression
				 */
				final String variable = ex.getVariables().get(0);
				/*
				 * current variable name
				 */
				String origin = QueryMatches.getNonScopedVariable();
				/*
				 * check if values are empty
				 */
				if (values.isEmpty()) {
					values = context[index]
							.getPossibleValuesForVariable(variable);
					origin = variable;
				}
				/*
				 * check if values are empty
				 */
				if (values.isEmpty()) {
					continue;
				}
				/*
				 * store as tuple
				 */
				tuple.put("$" + (index), values.size() == 1 ? values.get(0)
						: values);
				origins.put(variable,"$"+index);
				/*
				 * store as tuple sequence
				 */
				result = context[index].extractAndRenameBindingsForVariable(
						origin, variable);
			}

			/*
			 * store as tuple
			 */
			tuple.put("$" + index, values.size() == 1 ? values.get(0) : values);
			/*
			 * store results
			 */
			matches.add(result);
		}

		/*
		 * create result
		 */
		QueryMatches results = null;
		/*
		 * is singleton tuple-expression
		 */
		if (context.length == 1) {
			results = new QueryMatches(runtime, matches);
			results.setOrigins(origins);
		}
		/*
		 * is multiple tuple-expression
		 */
		else {
			results = new QueryMatches(runtime);
			results.setOrigins(origins);
			results.add(tuple);
		}

		/*
		 * set results
		 */
		runtime.getRuntimeContext().peek().setValue(
				VariableNames.QUERYMATCHES, results);
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
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private void interpretNull(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * set empty tuple sequence
		 */
		runtime.getRuntimeContext().peek().setValue(
				VariableNames.QUERYMATCHES, new QueryMatches(runtime));

	}

}
