/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.interpreter.core.interpreter;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.model.context.IVariableSet;
import de.topicmapslab.tmql4j.parser.core.expressions.ProjectionPostfix;
import de.topicmapslab.tmql4j.parser.core.expressions.TupleExpression;

/**
 * 
 * Special interpreter class to interpret projection-post-fixes.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 *  projection-postfix ::= tuple-expression
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ProjectionPostfixInterpreter extends
		ExpressionInterpreterImpl<ProjectionPostfix> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public ProjectionPostfixInterpreter(ProjectionPostfix ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		/*
		 * get current context
		 */
		Object obj = runtime.getRuntimeContext().peek().getValue(
				VariableNames.POSTFIXED);
		if (!(obj instanceof QueryMatches)) {
			throw new TMQLRuntimeException(
					"Missing projection context, variable %_projection not set.");
		}

		QueryMatches projection = new QueryMatches(runtime);

		/*
		 * iterate over tuples
		 */
		for (Object tuple : ((QueryMatches) obj).getPossibleValuesForVariable()) {
			runtime.getRuntimeContext().push();
			/*
			 * set current tuple to stack
			 */
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.CURRENT_TUPLE, tuple);

			/*
			 * call subexpression
			 */
			IExpressionInterpreter<TupleExpression> ex = getInterpretersFilteredByEypressionType(
					runtime, TupleExpression.class).get(0);
			ex.interpret(runtime);

			/*
			 * get results
			 */
			IVariableSet set = runtime.getRuntimeContext().pop();
			projection.add((QueryMatches) set
					.getValue(VariableNames.QUERYMATCHES));
		}

		runtime.getRuntimeContext().peek().setValue(
				VariableNames.QUERYMATCHES, projection);

	}
}
