package de.topicmapslab.tmql4j.draft2010.interpreter;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.draft2010.expressions.Filter;
import de.topicmapslab.tmql4j.draft2010.expressions.PathSpecification;
import de.topicmapslab.tmql4j.draft2010.expressions.PathStep;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.model.context.IVariableSet;

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
	
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		QueryMatches context = extractArguments(runtime,
				PathSpecification.class, 0);

		/*
		 * interpret filters
		 */
		for (IExpressionInterpreter<Filter> filter : getInterpretersFilteredByEypressionType(
				runtime, Filter.class)) {
			/*
			 * create new variable layer
			 */
			runtime.getRuntimeContext().push();
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.ITERATED_BINDINGS, context);

			filter.interpret(runtime);

			/*
			 * extract and check results
			 */
			IVariableSet set = runtime.getRuntimeContext().pop();
			if (!set.contains(VariableNames.QUERYMATCHES)) {
				throw new TMQLRuntimeException(
						"Missing result of interpretation of filter");
			}
			context = (QueryMatches) set.getValue(VariableNames.QUERYMATCHES);
		}

		/*
		 * store current context as result
		 */
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				context);
	}

}
