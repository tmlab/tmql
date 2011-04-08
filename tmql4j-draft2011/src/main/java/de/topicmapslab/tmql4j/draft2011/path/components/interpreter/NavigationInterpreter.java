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

import java.util.List;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.Navigation;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.StepDefinition;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * 
 * Special interpreter class to interpret naviagtions.
 * 
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * navigation ::= step [ navigation ]
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class NavigationInterpreter extends ExpressionInterpreterImpl<Navigation> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public NavigationInterpreter(Navigation ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		List<Object> values = HashUtil.getList();
		values.add(context.getCurrentNode());
		/*
		 * Iterate over all contained steps
		 */
		for (IExpressionInterpreter<StepDefinition> nextExpression : getInterpretersFilteredByEypressionType(runtime, StepDefinition.class)) {
			List<Object> results = HashUtil.getList();
			/*
			 * Iterate over all possible bindings and navigate over axis
			 */
			for (Object currentNode : values) {
				Context newContext = new Context(context);
				newContext.setCurrentNode(currentNode);
				/*
				 * call next expression
				 */
				QueryMatches matches = nextExpression.interpret(runtime, newContext, optionalArguments);
				results.addAll(matches.getPossibleValuesForVariable());
			}
			/*
			 * Combine sequences to one new sequence
			 */
			values = results;
		}
		return QueryMatches.asQueryMatchNS(runtime, values.toArray());
	}
}
