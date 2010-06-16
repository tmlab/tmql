package de.topicmapslab.tmql4j.draft2010.interpreter;

import java.util.Iterator;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.draft2010.expressions.Expression;
import de.topicmapslab.tmql4j.draft2010.expressions.NumericalExpression;
import de.topicmapslab.tmql4j.draft2010.tokens.Div;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.utility.operation.QueryMatchUtils;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.lexer.token.Minus;

/**
 * Interpreter implementation of production 'numerical-expression'
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class NumericalExpressionInterpreter extends
		ExpressionInterpreterImpl<NumericalExpression> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression to interpret
	 */
	public NumericalExpressionInterpreter(NumericalExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		/*
		 * new query match instance for results
		 */
		QueryMatches results;

		/*
		 * interpret unary numerical operation
		 */
		if (getGrammarTypeOfExpression() == NumericalExpression.TYPE_UNARY) {
			results = interpretUnaryOperation(runtime);
		}
		/*
		 * interpret binary numerical operation
		 */
		else if (getGrammarTypeOfExpression() == NumericalExpression.TYPE_ADDITIVE_OPERATION
				|| getGrammarTypeOfExpression() == NumericalExpression.TYPE_MULTIPLICATIVE_OPERATION) {
			results = interpretBinaryOperation(runtime);
		}
		/*
		 * is unknown
		 */
		else {
			throw new TMQLRuntimeException(
					"Unknown grammar type of production 'numerical-expression'.");
		}

		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);
	}

	/**
	 * Interpret unary numerical operations
	 * 
	 * @param runtime
	 *            the runtime
	 * @return the result of the interpretation
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretUnaryOperation(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		QueryMatches results = new QueryMatches(runtime);
		/*
		 * interpret expression contained by numerical expression
		 */
		QueryMatches context = extractArguments(runtime, Expression.class, 0);

		/*
		 * unary operation contains sign
		 */
		if (getTmqlTokens().get(0).equals(Minus.class)) {
			results = QueryMatchUtils.sign(runtime, context);
		}
		/*
		 * no sign contained
		 */
		else {
			results = context.extractAndRenameBindingsForVariable(QueryMatches
					.getNonScopedVariable());
		}
		return results;
	}

	/**
	 * Interpret binary numerical operations
	 * 
	 * @param runtime
	 *            the runtime
	 * @return the result of the interpretation
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretBinaryOperation(TMQLRuntime runtime)
			throws TMQLRuntimeException {

		QueryMatches[] arguments = extractArguments(runtime,
				NumericalExpression.class);

		/*
		 * get iterator for interpreters and operators
		 */
		Iterator<Class<? extends IToken>> operators = getExpression()
				.getOperators().iterator();
		QueryMatches leftHand = arguments[0];

		/*
		 * call next right-hand expression
		 */
		for (int index = 1; index < arguments.length; index++) {
			Class<? extends IToken> operator = operators.next();
			/*
			 * do numerical operation
			 */
			if (operator.equals(Div.class)) {
				leftHand = QueryMatchUtils.division(runtime, leftHand,
						arguments[index]);
			} else {
				leftHand = QueryMatchUtils.operation(runtime, operator,
						leftHand, arguments[index]);
			}
		}

		return leftHand;
	}
}
