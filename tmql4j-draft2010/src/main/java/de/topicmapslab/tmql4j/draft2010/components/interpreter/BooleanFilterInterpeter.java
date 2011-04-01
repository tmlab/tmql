package de.topicmapslab.tmql4j.draft2010.components.interpreter;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.BooleanFilter;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * The interpreter of boolean filter of the new TMQL draft
 * @author Sven Krosse
 *
 */
public class BooleanFilterInterpeter extends ExpressionInterpreterImpl<BooleanFilter> {

	/**
	 * constructor
	 * @param ex the expression
	 */
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
