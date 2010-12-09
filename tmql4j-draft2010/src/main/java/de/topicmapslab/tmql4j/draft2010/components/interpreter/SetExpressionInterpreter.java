package de.topicmapslab.tmql4j.draft2010.components.interpreter;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Intersect;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Subtraction;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Union;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.SetExpression;
import de.topicmapslab.tmql4j.draft2010.util.QueryMatchUtils;
import de.topicmapslab.tmql4j.draft2010.util.RuntimeUtils;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;

/**
 * Interpreter implementation of production 'set-expression'
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SetExpressionInterpreter extends ExpressionInterpreterImpl<SetExpression> {
	/**
	 * the logger
	 */
	private final Logger logger = LoggerFactory.getLogger(getClass());

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
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		QueryMatches[] arguments = RuntimeUtils.getParameters(runtime, context, this);
		/*
		 * get iterator for operators
		 */
		Iterator<Class<? extends IToken>> operators = getExpression().getOperators().iterator();

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
				leftHand = QueryMatchUtils.union(runtime, leftHand, arguments[index]);
			} else if (operator.equals(Subtraction.class)) {
				leftHand = QueryMatchUtils.minus(runtime, leftHand, arguments[index]);
			} else if (operator.equals(Intersect.class)) {
				leftHand = QueryMatchUtils.intersect(runtime, leftHand, arguments[index]);
			} else {
				logger.warn("Operator '" + operator.getSimpleName() + "' is unknown!");
				return QueryMatches.emptyMatches();
			}
		}

		return leftHand;

	}

}
