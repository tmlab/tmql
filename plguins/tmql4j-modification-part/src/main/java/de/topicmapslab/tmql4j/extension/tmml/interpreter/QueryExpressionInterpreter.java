/**
 * TMQL4J - Javabased TMQL Engine
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.extension.tmml.interpreter;

import static de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.QueryExpression.TYPE_DELETE_EXPRESSION;
import static de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.QueryExpression.TYPE_INSERT_EXPRESSION;
import static de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.QueryExpression.TYPE_MERGE_EXPRESSION;
import static de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.QueryExpression.TYPE_UPDATE_EXPRESSION;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.DeleteExpression;
import de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.InsertExpression;
import de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.MergeExpression;
import de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.QueryExpression;
import de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.UpdateExpression;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.parser.core.expressions.EnvironmentClause;


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
		 * is insert-expression
		 */
		case TYPE_INSERT_EXPRESSION: {
			interpretInsertExpression((TMQLRuntime) runtime);
		}
			break;
		/*
		 * is update-expression
		 */
		case TYPE_UPDATE_EXPRESSION: {
			interpretUpdateExpression((TMQLRuntime) runtime);
		}
			break;
		/*
		 * is merge-expression
		 */
		case TYPE_MERGE_EXPRESSION: {
			interpretMergeExpression((TMQLRuntime) runtime);
		}
			break;
		/*
		 * is delete-expression
		 */
		case TYPE_DELETE_EXPRESSION: {
			interpretDeleteExpression((TMQLRuntime) runtime);
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
	private void interpretInsertExpression(TMQLRuntime runtime)
			throws TMQLRuntimeException {

		/*
		 * call subexpression
		 */
		IExpressionInterpreter<?> ex = getInterpretersFilteredByEypressionType(
				runtime, InsertExpression.class).get(0);
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
	private void interpretUpdateExpression(TMQLRuntime runtime)
			throws TMQLRuntimeException {

		/*
		 * call subexpression
		 */
		IExpressionInterpreter<UpdateExpression> ex = getInterpretersFilteredByEypressionType(
				runtime, UpdateExpression.class).get(0);
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
	private void interpretMergeExpression(TMQLRuntime runtime)
			throws TMQLRuntimeException {

		/*
		 * call-subexpression
		 */
		IExpressionInterpreter<MergeExpression> ex = getInterpretersFilteredByEypressionType(
				runtime, MergeExpression.class).get(0);
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
	private void interpretDeleteExpression(TMQLRuntime runtime)
			throws TMQLRuntimeException {

		/*
		 * call subexpression
		 */
		IExpressionInterpreter<DeleteExpression> ex = getInterpretersFilteredByEypressionType(
				runtime, DeleteExpression.class).get(0);
		ex.interpret(runtime);

	}

}
