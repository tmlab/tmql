package de.topicmapslab.tmql4j.draft2010.components.interpreter;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.ValueExpression;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * Interpreter implementation of the production 'value-expression'
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ValueExpressionInterpreter extends ExpressionInterpreterImpl<ValueExpression> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression to interpret
	 */
	public ValueExpressionInterpreter(ValueExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * redirect to contained expression
		 */
		return getInterpreters(runtime).get(0).interpret(runtime, context, optionalArguments);
	}

}
