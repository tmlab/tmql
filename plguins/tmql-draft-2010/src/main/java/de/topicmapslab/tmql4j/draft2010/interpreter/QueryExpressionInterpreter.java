package de.topicmapslab.tmql4j.draft2010.interpreter;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.expressions.Expression;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.parser.core.expressions.EnvironmentClause;
import de.topicmapslab.tmql4j.parser.core.expressions.QueryExpression;

public class QueryExpressionInterpreter extends
		ExpressionInterpreterImpl<QueryExpression> {

	public QueryExpressionInterpreter(QueryExpression ex) {
		super(ex);
	}

	@Override
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * is new expression type
		 */
		if (containsExpressionsType(Expression.class)) {
			
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
			
			getInterpretersFilteredByEypressionType(runtime, Expression.class)
					.get(0).interpret(runtime);
		}
		/*
		 * redirect to origin one
		 */
		else {
			new de.topicmapslab.tmql4j.interpreter.core.interpreter.QueryExpressionInterpreter(
					getExpression()).interpret(runtime);
		}
	}

}
