package de.topicmapslab.tmql4j.draft2010.interpreter;

import java.util.Map;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.draft2010.expressions.PathExpression;
import de.topicmapslab.tmql4j.draft2010.expressions.PathStep;
import de.topicmapslab.tmql4j.draft2010.expressions.SimpleExpression;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.model.context.IVariableSet;

/**
 * Interpreter of an expression of the type {@link PathExpression}.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PathExpressionInterpreter extends
		ExpressionInterpreterImpl<PathExpression> {

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
	@Override
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		QueryMatches context = null;

		/*
		 * interpret simple-expression first if exists
		 */
		if (getGrammarTypeOfExpression() == PathExpression.TYPE_SIMPLEEXPRESSION) {
			context = extractArguments(runtime, SimpleExpression.class, 0);
		}
		/*
		 * context set to queried topic map
		 */
		else {
			context = new QueryMatches(runtime);
			Map<String, Object> tuple = HashUtil.getHashMap();
			tuple.put(QueryMatches.getNonScopedVariable(), runtime
					.getRuntimeContext().peek().getValue(
							VariableNames.CURRENT_MAP));
			context.add(tuple);
		}

		/*
		 * iterate over all path-steps and call interpreters
		 */
		for (IExpressionInterpreter<PathStep> step : getInterpretersFilteredByEypressionType(
				runtime, PathStep.class)) {
			/*
			 * set new variable layer with current context
			 */
			runtime.getRuntimeContext().push();
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.ITERATED_BINDINGS, context);

			step.interpret(runtime);
			IVariableSet set = runtime.getRuntimeContext().pop();
			if (!set.contains(VariableNames.QUERYMATCHES)) {
				throw new TMQLRuntimeException(
						"Missing result of interpretation of path-step");
			}

			context = (QueryMatches) set.getValue(VariableNames.QUERYMATCHES);
		}

		/*
		 * store overall result
		 */
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				context);
	}
}
