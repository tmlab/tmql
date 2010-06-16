package de.topicmapslab.tmql4j.draft2010.interpreter;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.expressions.BooleanFilter;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;

public class BooleanFilterInterpeter extends
		ExpressionInterpreterImpl<BooleanFilter> {

	public BooleanFilterInterpeter(BooleanFilter ex) {
		super(ex);
	}

	@Override
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		/*
		 * redirect to boolean-expression
		 */
		getInterpreters(runtime).get(0).interpret(runtime);

	}

}
