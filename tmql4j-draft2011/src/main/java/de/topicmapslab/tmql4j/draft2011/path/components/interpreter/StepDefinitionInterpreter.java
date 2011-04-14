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
package de.topicmapslab.tmql4j.draft2011.path.components.interpreter;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.AssociationPattern;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.FilterPostfix;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.Step;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.StepDefinition;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * 
 * Special interpreter class to interpret steps.
 * <p>
 * step-definition ::= step filter*
 * </p>
 * <p>
 * step ::= // anchor
 * </p>
 * </code> </p>*
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class StepDefinitionInterpreter extends ExpressionInterpreterImpl<StepDefinition> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public StepDefinitionInterpreter(StepDefinition ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * execute step expression
		 */
		QueryMatches results = null;
		if (getExpression().contains(Step.class)) {
			results = extractArguments(runtime, Step.class, 0, context, optionalArguments);
		} else {
			results = extractArguments(runtime, AssociationPattern.class, 0, context, optionalArguments);
		}
		if (results.isEmpty()) {
			return results;
		}
		/*
		 * handle filter parts
		 */
		Context newContext = new Context(context);
		newContext.setContextBindings(results);
		for (IExpressionInterpreter<FilterPostfix> interpreter : getInterpretersFilteredByEypressionType(runtime, FilterPostfix.class)) {
			QueryMatches iteration = interpreter.interpret(runtime, newContext, optionalArguments);
			if (iteration.isEmpty()) {
				return iteration;
			}
			newContext.setContextBindings(iteration);
		}
		return newContext.getContextBindings();
	}
}
