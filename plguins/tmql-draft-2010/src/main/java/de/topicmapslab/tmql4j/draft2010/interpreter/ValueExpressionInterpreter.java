package de.topicmapslab.tmql4j.draft2010.interpreter;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.expressions.ValueExpression;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;

/**
 * Interpreter implementation of the production 'value-expression'
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ValueExpressionInterpreter extends
		ExpressionInterpreterImpl<ValueExpression> {

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
	
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		/*
		 * redirect to contained expression
		 */
		getInterpreters(runtime).get(0).interpret(runtime);

	}

}
