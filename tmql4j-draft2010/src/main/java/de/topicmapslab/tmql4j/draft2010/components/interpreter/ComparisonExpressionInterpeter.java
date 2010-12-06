package de.topicmapslab.tmql4j.draft2010.components.interpreter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Equality;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Equals;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.GreaterEquals;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.GreaterThan;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.LowerEquals;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.LowerThan;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.MatchesRegExp;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Unequals;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.ComparisonExpression;
import de.topicmapslab.tmql4j.draft2010.util.QueryMatchUtils;
import de.topicmapslab.tmql4j.draft2010.util.RuntimeUtils;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;

public class ComparisonExpressionInterpeter extends ExpressionInterpreterImpl<ComparisonExpression> {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public ComparisonExpressionInterpeter(ComparisonExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {

		/*
		 * get the operator token
		 */
		Class<? extends IToken> operator = getExpression().getOperator();

		/*
		 * get results of all sub-expressions
		 */
		QueryMatches[] arguments = RuntimeUtils.getParameters(runtime, context, this);

		/*
		 * operator is '='
		 */
		if (operator.equals(Equals.class)) {
			return QueryMatchUtils.contains(runtime, arguments[0], arguments[1]);
		}
		/*
		 * operator is '=='
		 */
		else if (operator.equals(Equality.class)) {
			return QueryMatchUtils.equals(runtime, arguments[0], arguments[1]);
		}
		/*
		 * operator is '!='
		 */
		else if (operator.equals(Unequals.class)) {
			return QueryMatchUtils.unequals(runtime, arguments[0], arguments[1]);
		}
		/*
		 * operator is '<'
		 */
		else if (operator.equals(LowerThan.class)) {
			return QueryMatchUtils.lowerThan(runtime, arguments[0], arguments[1]);
		}
		/*
		 * operator is '<='
		 */
		else if (operator.equals(LowerEquals.class)) {
			return QueryMatchUtils.lowerOrEquals(runtime, arguments[0], arguments[1]);
		}
		/*
		 * operator is '>'
		 */
		else if (operator.equals(GreaterThan.class)) {
			return QueryMatchUtils.greaterThan(runtime, arguments[0], arguments[1]);
		}
		/*
		 * operator is '>='
		 */
		else if (operator.equals(GreaterEquals.class)) {
			return QueryMatchUtils.greaterOrEquals(runtime, arguments[0], arguments[1]);
		}
		/*
		 * operator is '=~'
		 */
		else if (operator.equals(MatchesRegExp.class)) {
			return QueryMatchUtils.matchesRegExp(runtime, arguments[0], arguments[1]);
		}
		logger.warn("Operator '" + operator.getSimpleName() + "' is unknown!");
		return QueryMatches.emptyMatches();
	}

}
