/**
 * TMQL4J - Javabased TMQL Engine
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.update.components.interpreter;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.productions.EnvironmentClause;
import de.topicmapslab.tmql4j.update.grammar.productions.QueryExpression;
import de.topicmapslab.tmql4j.update.grammar.productions.UpdateExpression;

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
public class QueryExpressionInterpreter extends ExpressionInterpreterImpl<QueryExpression> {

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
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * check if environment-clause contained
		 */
		if (containsExpressionsType(EnvironmentClause.class)) {
			/*
			 * call environment-clause
			 */
			getInterpretersFilteredByEypressionType(runtime, EnvironmentClause.class).get(0).interpret(runtime, context, optionalArguments);
		}

		/*
		 * call subexpression
		 */
		IExpressionInterpreter<UpdateExpression> ex = getInterpretersFilteredByEypressionType(runtime, UpdateExpression.class).get(0);
		return ex.interpret(runtime, context, optionalArguments);
	}

}
