package de.topicmapslab.tmql4j.draft2010.interpreter;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.expressions.Expression;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;

/**
 * Interpreter of an expression of the type {@link Expression}.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ExpressionInterpreter extends
		ExpressionInterpreterImpl<Expression> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression to interpret
	 */
	public ExpressionInterpreter(Expression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * redirect to sub-expression
		 */
		getInterpreters(runtime).get(0).interpret(runtime);

	}

}
