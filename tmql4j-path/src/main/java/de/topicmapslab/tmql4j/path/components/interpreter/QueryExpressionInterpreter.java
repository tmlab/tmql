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

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.productions.EnvironmentClause;
import de.topicmapslab.tmql4j.path.grammar.productions.FlwrExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.PathExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.QueryExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.SelectExpression;

/**
 * 
 * Special interpreter class to interpret query-expressions. *
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * query-expression ::= [ environment-clause ] select-expression
 * </p>
 * <p>
 * query-expression ::= [ environment-clause ] flwr-expression
 * </p>
 * <p>
 * query-expression ::= [ environment-clause ] path-expression
 * </p>
 * <p>
 * query-expression ::= [ environment-clause ] delete-expression
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class QueryExpressionInterpreter extends
		ExpressionInterpreterImpl<QueryExpression> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public QueryExpressionInterpreter(QueryExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * check if environment-clause contained
		 */
		if (containsExpressionsType(EnvironmentClause.class)) {
			/*
			 * call environment-clause
			 */
			getInterpretersFilteredByEypressionType(runtime,
					EnvironmentClause.class).get(0).interpret(runtime);
		}

		/*
		 * switch by grammar-type
		 */
		switch (getGrammarTypeOfExpression()) {
		/*
		 * is select-expression
		 */
		case QueryExpression.TYPE_SELECT_EXPRESSION: {
			interpretSelectExpression(runtime);
		}
			break;
		/*
		 * is flwr-expression
		 */
		case QueryExpression.TYPE_FLWR_EXPRESSION: {
			interpretFlwrExpression(runtime);
		}
			break;
		/*
		 * is path-expression
		 */
		case QueryExpression.TYPE_PATH_EXPRESSION: {
			interpretPathExpression(runtime);
		}
			break;

		default:
			throw new TMQLRuntimeException("unkown state");
		}
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
	private void interpretSelectExpression(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * redirect to subexpression
		 */
		IExpressionInterpreter<SelectExpression> ex = getInterpretersFilteredByEypressionType(
				runtime, SelectExpression.class).get(0);
		ex.interpret(runtime);
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
	private void interpretFlwrExpression(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * redirect to subexpression
		 */
		IExpressionInterpreter<FlwrExpression> ex = getInterpretersFilteredByEypressionType(
				runtime, FlwrExpression.class).get(0);
		ex.interpret(runtime);
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
	private void interpretPathExpression(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * redirect to subexpression
		 */
		IExpressionInterpreter<PathExpression> ex = getInterpretersFilteredByEypressionType(
				runtime, PathExpression.class).get(0);
		ex.interpret(runtime);
	}

}
