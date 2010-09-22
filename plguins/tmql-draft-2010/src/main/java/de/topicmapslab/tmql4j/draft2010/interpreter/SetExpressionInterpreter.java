package de.topicmapslab.tmql4j.draft2010.interpreter;

import java.util.Iterator;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.draft2010.expressions.Expression;
import de.topicmapslab.tmql4j.draft2010.expressions.SetExpression;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.utility.operation.QueryMatchUtils;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.lexer.token.Intersect;
import de.topicmapslab.tmql4j.lexer.token.Substraction;
import de.topicmapslab.tmql4j.lexer.token.Union;

/**
 * Interpreter implementation of production 'set-expression'
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SetExpressionInterpreter extends
		ExpressionInterpreterImpl<SetExpression> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression to interpret
	 */
	public SetExpressionInterpreter(SetExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		QueryMatches[] arguments = extractArguments(runtime, Expression.class);

		/*
		 * get iterator for operators
		 */
		Iterator<Class<? extends IToken>> operators = getExpression()
				.getOperators().iterator();

		QueryMatches leftHand = arguments[0];

		for (int index = 1; index < arguments.length; index++) {
			/*
			 * get operator
			 */
			Class<? extends IToken> operator = operators.next();

			/*
			 * handle operator for set operation
			 */
			if (operator.equals(Union.class)) {
				leftHand = QueryMatchUtils.union(runtime, leftHand,
						arguments[index]);
			} else if (operator.equals(Substraction.class)) {
				leftHand = QueryMatchUtils.minus(runtime, leftHand,
						arguments[index]);
			} else if (operator.equals(Intersect.class)) {
				leftHand = QueryMatchUtils.intersect(runtime, leftHand,
						arguments[index]);
			} else {
				throw new TMQLRuntimeException("Unknown operator '"
						+ operator.getSimpleName() + "'.");
			}
		}

		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				leftHand);

	}

}
