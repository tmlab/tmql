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
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.productions.Navigation;

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
public class NavigationInterpreter extends
		ExpressionInterpreterImpl<Navigation> {

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
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * peek from stack
		 */
		Object at_ = runtime.getRuntimeContext().peek().getValue(
				VariableNames.CURRENT_TUPLE);

		QueryMatches matches = new QueryMatches(runtime);
		ITupleSequence<Object> sequence = runtime.getProperties().newSequence();
		sequence.add(at_);
		matches.convertToTuples(sequence);

		/*
		 * Iterate over all contained steps
		 */
		for (IExpressionInterpreter<Step> nextExpression : getInterpretersFilteredByEypressionType(
				runtime, Step.class)) {
			QueryMatches iterationMatches = new QueryMatches(runtime);
			/*
			 * Iterate over all possible bindings and navigate over axis
			 */
			for (Object match : matches.getPossibleValuesForVariable()) {
				/*
				 * push new instance of @_ to variable stack
				 */
				runtime.getRuntimeContext().push()
						.setValue(VariableNames.CURRENT_TUPLE,
								match);
				/*
				 * call next expression
				 */
				nextExpression.interpret(runtime);
				/*
				 * pull results from stack --> read %%%___
				 */
				IVariableSet set = runtime.getRuntimeContext().pop();
				if (!set.contains(VariableNames.QUERYMATCHES)) {
					throw new TMQLRuntimeException(
							"Missing interpretation result of step.");
				}

				iterationMatches.add((QueryMatches) set
						.getValue(VariableNames.QUERYMATCHES));

			}
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.QUERYMATCHES, iterationMatches);

			/*
			 * Combine sequences to one new sequence
			 */
			matches = iterationMatches;
		}

	}
}
