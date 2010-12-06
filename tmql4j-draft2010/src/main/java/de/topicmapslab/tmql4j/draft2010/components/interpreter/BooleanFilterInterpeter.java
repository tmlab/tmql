package de.topicmapslab.tmql4j.draft2010.components.interpreter;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.BooleanFilter;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

public class BooleanFilterInterpeter extends ExpressionInterpreterImpl<BooleanFilter> {

	public BooleanFilterInterpeter(BooleanFilter ex) {
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
