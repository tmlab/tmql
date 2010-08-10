package de.topicmapslab.tmql4j.draft2010.interpreter;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.draft2010.expressions.ComparisonExpression;
import de.topicmapslab.tmql4j.draft2010.interpreter.base.BaseExpressionInterpreter;
import de.topicmapslab.tmql4j.draft2010.tokens.Equals;
import de.topicmapslab.tmql4j.draft2010.tokens.MatchesRegExp;
import de.topicmapslab.tmql4j.draft2010.tokens.Unequals;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.utility.operation.QueryMatchUtils;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.lexer.token.Equality;
import de.topicmapslab.tmql4j.lexer.token.GreaterEquals;
import de.topicmapslab.tmql4j.lexer.token.GreaterThan;
import de.topicmapslab.tmql4j.lexer.token.LowerEquals;
import de.topicmapslab.tmql4j.lexer.token.LowerThan;
import de.topicmapslab.tmql4j.lexer.token.ShortcutAxisLocators;

public class ComparisonExpressionInterpeter extends
		BaseExpressionInterpreter<ComparisonExpression> {

	public ComparisonExpressionInterpeter(ComparisonExpression ex) {
		super(ex);
	}

	
	protected long getNumberOfArguments() {
		return 2;
	}

	
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		/*
		 * get the operator token
		 */
		Class<? extends IToken> operator = getExpression().getOperator();

		/*
		 * get results of all sub-expressions
		 */
		QueryMatches[] arguments = extractArguments(runtime);

		QueryMatches result = null;

		/*
		 * operator is '='
		 */
		if (operator.equals(Equals.class)
				|| operator.equals(ShortcutAxisLocators.class)) {
			result = QueryMatchUtils.contains(runtime, arguments[0],
					arguments[1]);
		}
		/*
		 * operator is '=='
		 */
		else if (operator.equals(Equality.class)) {
			result = QueryMatchUtils
					.equals(runtime, arguments[0], arguments[1]);
		}
		/*
		 * operator is '!='
		 */
		else if (operator.equals(Unequals.class)) {
			result = QueryMatchUtils.unequals(runtime, arguments[0],
					arguments[1]);
		}
		/*
		 * operator is '<'
		 */
		else if (operator.equals(LowerThan.class)) {
			result = QueryMatchUtils.lowerThan(runtime, arguments[0],
					arguments[1]);
		}
		/*
		 * operator is '<='
		 */
		else if (operator.equals(LowerEquals.class)) {
			result = QueryMatchUtils.lowerOrEquals(runtime, arguments[0],
					arguments[1]);
		}
		/*
		 * operator is '>'
		 */
		else if (operator.equals(GreaterThan.class)) {
			result = QueryMatchUtils.greaterThan(runtime, arguments[0],
					arguments[1]);
		}
		/*
		 * operator is '>='
		 */
		else if (operator.equals(GreaterEquals.class)) {
			result = QueryMatchUtils.greaterOrEquals(runtime, arguments[0],
					arguments[1]);
		}
		/*
		 * operator is '=~'
		 */
		else if (operator.equals(MatchesRegExp.class)) {
			result = QueryMatchUtils.matchesRegExp(runtime, arguments[0],
					arguments[1]);
		}
		/*
		 * operator is unknown
		 */
		else {
			throw new TMQLRuntimeException("Unknown operator '"
					+ operator.getSimpleName() + "'.");
		}

		runtime.getRuntimeContext().peek().setValue(
				VariableNames.QUERYMATCHES, result);
	}

}
