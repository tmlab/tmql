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

import static de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.DeleteExpression.TYPE_ALL;
import static de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.DeleteExpression.TYPE_WITHOUT_WHERE_CLAUSE;
import static de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.DeleteExpression.TYPE_WITH_WHERE_CLAUSE;

import java.util.Map;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.DeleteClause;
import de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.DeleteExpression;
import de.topicmapslab.tmql4j.extension.tmml.util.DeletionHandler;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.parser.core.expressions.WhereClause;

/**
 * 
 * Special interpreter class to interpret delete-expression.
 * <p>
 * The grammar production rule of the expression is: <code>
 * delete-expression ::= DELETE [CASCADE] &lt;value-expression&gt; [ WHERE boolean-expression ]
 * </code>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class DeleteExpressionInterpreter extends
		ExpressionInterpreterImpl<DeleteExpression> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public DeleteExpressionInterpreter(DeleteExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		QueryMatches matches = new QueryMatches(runtime);
		/*
		 * switch by grammar type
		 */
		switch (getGrammarTypeOfExpression()) {
		/*
		 * is expression DELETE ALL
		 */
		case TYPE_ALL: {
			Map<String, Object> resultTuple = HashUtil.getHashMap();
			resultTuple.put("$0", new DeletionHandler(runtime).deleteAll());
			matches.add(resultTuple);
		}
			break;
		/*
		 * is delete-expression
		 */
		case TYPE_WITH_WHERE_CLAUSE:
		case TYPE_WITHOUT_WHERE_CLAUSE: {
			if (containsExpressionsType(WhereClause.class)) {
				matches = interpretWhereClause(runtime);
			}
			matches = interpretDeleteClause(runtime, matches);
		}
			break;
		}

		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				matches);
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
		IExpressionInterpreter<WhereClause> whereclause = getInterpretersFilteredByEypressionType(
				runtime, WhereClause.class).get(0);

		runtime.getRuntimeContext().push();

		/*
		 * call where-clause
		 */
		whereclause.interpret(runtime);

		Object obj = runtime.getRuntimeContext().pop().getValue(
				VariableNames.QUERYMATCHES);
		return (QueryMatches) obj;
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
	 * @param matches
	 *            the satisfying variable bindings of the where-clause
	 * @return the query matches containing the results of the interpretation of
	 *         the sub-expression
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretDeleteClause(TMQLRuntime runtime,
			QueryMatches matches) throws TMQLRuntimeException {

		/*
		 * only one delete clause has to be contained
		 */
		if (!containsExpressionsType(DeleteClause.class)) {
			throw new TMQLRuntimeException(
					"Invalid structure. not delete clause.");
		}

		/*
		 * extract the select clause
		 */
		IExpressionInterpreter<DeleteClause> deleteClause = getInterpretersFilteredByEypressionType(
				runtime, DeleteClause.class).get(0);

		/*
		 * push to the top of the stack
		 */
		runtime.getRuntimeContext().push();
		if (!matches.isEmpty()) {
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.ITERATED_BINDINGS, matches);
		}

		/*
		 * call sub expression
		 */
		deleteClause.interpret(runtime);

		/*
		 * pop from top of the stack
		 */
		Object obj = runtime.getRuntimeContext().pop().getValue(
				VariableNames.QUERYMATCHES);
		/*
		 * check result type
		 */
		if (!(obj instanceof QueryMatches)) {
			throw new TMQLRuntimeException(
					"Invalid interpretation of expression Delete-clause. Has to return an instance of Query<?>.");
		}

		return (QueryMatches) obj;
	}

}
