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
import java.util.Set;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.TupleExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.ValueExpression;
import de.topicmapslab.tmql4j.util.HashUtil;

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
		 * is value-expression
		 */
		case 0: {
			return interpretValueExpression(runtime, context, optionalArguments);
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
	private QueryMatches interpretValueExpression(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
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

		QueryMatches[] content = extractArguments(runtime, ValueExpression.class, context, optionalArguments);
		for (int index = 0; index < content.length; index++) {
			/*
			 * get corresponding expression
			 */
			IExpression ex = getExpression().getExpressionFilteredByType(ValueExpression.class).get(index);

			QueryMatches result = content[index].extractAndRenameBindingsForVariable("$" + (index));
			/*
			 * add sequence
			 */
			List<Object> values = content[index].getPossibleValuesForVariable();
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
					values = content[index].getPossibleValuesForVariable(variable);
					origin = variable;
				}
				/*
				 * check if values are empty
				 */
				if (values.isEmpty()) {
					/*
					 * add null values only for multiple results
					 */
					if (content.length != 1) {
						tuple.put("$" + index, null);
					}
					continue;
				}
				/*
				 * store as tuple
				 */
				tuple.put("$" + (index), values.size() == 1 ? values.get(0) : values);
				origins.put(variable, "$" + index);
				/*
				 * store as tuple sequence
				 */
				result = content[index].extractAndRenameBindingsForVariable(origin, variable);
			}

			/*
			 * store as tuple
			 */
			tuple.put("$" + index, values.size() == 1 ? values.get(0) : values);
			/*
			 * store results
			 */
			if (!result.isEmpty()) {
				matches.add(result);
			} else {
				matches.add(content[index]);
			}
		}

		/*
		 * create result
		 */
		QueryMatches results = new QueryMatches(runtime);
		results.setOrigins(origins);
		/*
		 * is singleton tuple-expression
		 */
		if (content.length == 1) {
			if (!matches.isEmpty()) {
				results.addAll(matches);
			} else if (!tuple.isEmpty()) {
				results.add(tuple);
			}
		}
		/*
		 * is multiple tuple-expression
		 */
		else {
			results.add(tuple);
		}
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
