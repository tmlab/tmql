package de.topicmapslab.tmql4j.draft2010.components.interpreter;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.EnvironmentClause;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.Expression;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.QueryExpression;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * The interpreter of a {@link QueryExpression}
 * @author Sven Krosse
 *
 */
public class QueryExpressionInterpreter extends ExpressionInterpreterImpl<QueryExpression> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression
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
		 * call expression
		 */
		return getInterpretersFilteredByEypressionType(runtime, Expression.class).get(0).interpret(runtime, context, optionalArguments);

	}

}
