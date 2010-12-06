package de.topicmapslab.tmql4j.draft2010.components.interpreter;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.Filter;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.PathSpecification;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.PathStep;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * Interpreter of an expression of the type {@link PathStep}.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PathStepInterpreter extends ExpressionInterpreterImpl<PathStep> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression to interpret
	 */
	public PathStepInterpreter(PathStep ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {

		QueryMatches matches = getInterpretersFilteredByEypressionType(runtime, PathSpecification.class).get(0).interpret(runtime, context, optionalArguments);
		Context newContext = new Context(context);
		newContext.setContextBindings(matches);
		/*
		 * interpret filters
		 */
		for (IExpressionInterpreter<Filter> filter : getInterpretersFilteredByEypressionType(runtime, Filter.class)) {
			QueryMatches filtered = filter.interpret(runtime, newContext, optionalArguments);
			newContext.setContextBindings(filtered);
		}
		return newContext.getContextBindings();
	}

}
