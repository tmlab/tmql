package de.topicmapslab.tmql4j.draft2010.components.interpreter;

import java.util.Iterator;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Div;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Minus;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.Expression;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.NumericalExpression;
import de.topicmapslab.tmql4j.draft2010.util.QueryMatchUtils;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;

/**
 * Interpreter implementation of production 'numerical-expression'
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class NumericalExpressionInterpreter extends ExpressionInterpreterImpl<NumericalExpression> {

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
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * interpret unary numerical operation
		 */
		if (getGrammarTypeOfExpression() == NumericalExpression.TYPE_UNARY) {
			return interpretUnaryOperation(runtime, context, optionalArguments);
		}
		/*
		 * interpret binary numerical operation
		 */
		else if (getGrammarTypeOfExpression() == NumericalExpression.TYPE_ADDITIVE_OPERATION || getGrammarTypeOfExpression() == NumericalExpression.TYPE_MULTIPLICATIVE_OPERATION) {
			return interpretBinaryOperation(runtime, context, optionalArguments);
		}
		return QueryMatches.emptyMatches();
	}

	/**
	 * Interpret unary numerical operations
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the querying context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the result of the interpretation
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretUnaryOperation(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * interpret expression contained by numerical expression
		 */
		QueryMatches matches = extractArguments(runtime, Expression.class, 0, context);

		/*
		 * unary operation contains sign
		 */
		if (getTmqlTokens().get(0).equals(Minus.class)) {
			return QueryMatchUtils.sign(runtime, matches);
		}
		/*
		 * no sign contained
		 */
		return matches.extractAndRenameBindingsForVariable(QueryMatches.getNonScopedVariable());
	}

	/**
	 * Interpret binary numerical operations
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the querying context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the result of the interpretation
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretBinaryOperation(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {

		QueryMatches[] arguments = extractArguments(runtime, NumericalExpression.class, context, optionalArguments);

		/*
		 * get iterator for interpreters and operators
		 */
		Iterator<Class<? extends IToken>> operators = getExpression().getOperators().iterator();
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
				leftHand = QueryMatchUtils.division(runtime, leftHand, arguments[index]);
			} else {
				leftHand = QueryMatchUtils.operation(runtime, operator, leftHand, arguments[index]);
			}
		}

		return leftHand;
	}
}
