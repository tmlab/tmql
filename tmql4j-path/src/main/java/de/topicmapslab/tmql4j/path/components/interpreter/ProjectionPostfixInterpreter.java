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
package de.topicmapslab.tmql4j.path.components.interpreter;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.components.processor.core.Context;
import de.topicmapslab.tmql4j.path.grammar.productions.ProjectionPostfix;
import de.topicmapslab.tmql4j.path.grammar.productions.TupleExpression;

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
public class ProjectionPostfixInterpreter extends ExpressionInterpreterImpl<ProjectionPostfix> {

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
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * return empty matches if current context is empty
		 */
		if (context.getContextBindings() == null) {
			return QueryMatches.emptyMatches();
		}
		QueryMatches projection = new QueryMatches(runtime);

		/*
		 * iterate over tuples
		 */
		for (Object node : context.getContextBindings().getPossibleValuesForVariable()) {
			Context newContext = new Context(context);
			newContext.setContextBindings(null);
			newContext.setCurrentNode(node);
			/*
			 * call subexpression
			 */
			IExpressionInterpreter<TupleExpression> ex = getInterpretersFilteredByEypressionType(runtime, TupleExpression.class).get(0);
			QueryMatches matches = ex.interpret(runtime, newContext, optionalArguments);
			projection.add(matches);
		}
		return projection;
	}
}
