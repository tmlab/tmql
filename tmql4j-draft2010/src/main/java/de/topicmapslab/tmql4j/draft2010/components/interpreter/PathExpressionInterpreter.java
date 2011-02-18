package de.topicmapslab.tmql4j.draft2010.components.interpreter;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.PathExpression;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.PathStep;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.SimpleExpression;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * Interpreter of an expression of the type {@link PathExpression}.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PathExpressionInterpreter extends ExpressionInterpreterImpl<PathExpression> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression to interpret
	 */
	public PathExpressionInterpreter(PathExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		Context newContext = new Context(context);
		/*
		 * interpret simple-expression first if exists
		 */
		if (getGrammarTypeOfExpression() == PathExpression.TYPE_SIMPLEEXPRESSION) {
			QueryMatches matches = extractArguments(runtime, SimpleExpression.class, 0, context);
			newContext.setContextBindings(matches);
		}
		/*
		 * context set to queried topic map
		 */
		else {
			newContext.setContextBindings(QueryMatches.asQueryMatchNS(runtime, context.getQuery().getTopicMap()));
		}

		/*
		 * iterate over all path-steps and call interpreters
		 */
		for (IExpressionInterpreter<PathStep> step : getInterpretersFilteredByEypressionType(runtime, PathStep.class)) {
			QueryMatches matches = step.interpret(runtime, newContext, optionalArguments);
			newContext.setContextBindings(matches);
		}
		return newContext.getContextBindings();
	}
}
