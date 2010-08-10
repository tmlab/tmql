package de.topicmapslab.tmql4j.draft2010.interpreter.literals;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.expressions.literals.Literal;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;

public class LiteralInterpreter extends
		ExpressionInterpreterImpl<Literal> {

	public LiteralInterpreter(Literal ex) {
		super(ex);
	}

	
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		// TODO Auto-generated method stub

	}

}
