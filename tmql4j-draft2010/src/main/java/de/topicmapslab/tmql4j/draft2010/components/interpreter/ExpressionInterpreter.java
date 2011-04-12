package de.topicmapslab.tmql4j.draft2010.components.interpreter;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.Expression;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * Interpreter of an expression of the type {@link Expression}.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ExpressionInterpreter extends ExpressionInterpreterImpl<Expression> {

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
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * redirect to real filter expression
		 */
		return getInterpreters(runtime).get(0).interpret(runtime, context, optionalArguments);
	}

}
