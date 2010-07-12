/**
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2009 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.extension.tmml.interpreter;

import java.util.Map;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.UpdateClause;
import de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.UpdateExpression;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.model.context.IVariableSet;
import de.topicmapslab.tmql4j.parser.core.expressions.WhereClause;

/**
 * 
 * Special interpreter class to interpret update-expressions.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 *  * <p>
 * update-expression ::= UPDATE anchor [parameter] ( SET | ADD ) value-expression ( ',' anchor [parameter] ( SET | ADD ) value-expression )? WHERE boolean-expression
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class UpdateExpressionInterpreter extends
		ExpressionInterpreterImpl<UpdateExpression> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public UpdateExpressionInterpreter(UpdateExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		/*
		 * execute the contained expressions
		 */
		QueryMatches matches = interpretWhereClause(runtime);

		Map<String, Object> tuple = HashUtil.getHashMap();
		QueryMatches results = new QueryMatches(runtime);
			
		/*
		 * iterate over update-clauses
		 */
		for (IExpressionInterpreter<UpdateClause> interpreter : getInterpretersFilteredByEypressionType(
				runtime, UpdateClause.class)) {
			runtime.getRuntimeContext().push();
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.ITERATED_BINDINGS, matches);

			/*
			 * call update-clause
			 */
			interpreter.interpret(runtime);

			IVariableSet set = runtime.getRuntimeContext().pop();
			Object obj = set.getValue(VariableNames.QUERYMATCHES);
			if (!(obj instanceof QueryMatches)) {
				throw new TMQLRuntimeException(
						"Invalid interpretation of update clause, has to return an instance of QueryMatches.");
			}
			
			QueryMatches match = (QueryMatches) obj;
			tuple.put("$" + tuple.size(), match.getPossibleValuesForVariable()
					.get(0));
		}

		results.add(tuple);

		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);

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
	 * @return the query matches containing the results of the interpretation of
	 *         the sub-expression
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretWhereClause(TMQLRuntime runtime)
			throws TMQLRuntimeException {

		if (containsExpressionsType(WhereClause.class)) {

			/*
			 * get expression interpreter for where-clause
			 */
			IExpressionInterpreter<WhereClause> navigation = getInterpretersFilteredByEypressionType(
					runtime, WhereClause.class).get(0);

			/*
			 * push to stack
			 */
			runtime.getRuntimeContext().push();

			/*
			 * interpret
			 */
			navigation.interpret(runtime);

			/*
			 * get results from stack
			 */
			IVariableSet set = runtime.getRuntimeContext().pop();

			Object obj = set.getValue(VariableNames.QUERYMATCHES);
			if (!(obj instanceof QueryMatches)) {
				throw new TMQLRuntimeException(
						"Invalid interpretation of where clause. Has to return an instance of QueryMatches");
			}

			return (QueryMatches) obj;
		}
		return new QueryMatches(runtime);
	}

}
