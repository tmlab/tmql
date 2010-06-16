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

import java.util.Collection;
import java.util.Map;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.parser.core.expressions.ParameterPair;
import de.topicmapslab.tmql4j.parser.core.expressions.Parameters;
import de.topicmapslab.tmql4j.parser.core.expressions.TupleExpression;

/**
 * 
 * Special interpreter class to interpret parameters.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * parameters ::= tuple-expression | ( &lt; identifier : value-expression &gt; )
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ParametersInterpreter extends
		ExpressionInterpreterImpl<Parameters> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public ParametersInterpreter(Parameters ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * switch by grammar type
		 */
		switch (getGrammarTypeOfExpression()) {
		/*
		 * is tuple-expression
		 */
		case 0: {
			interpreteTupleExpression(runtime);
		}
			break;
		/*
		 * is parameters-pair
		 */
		case 1: {
			interpreteParameterPairs(runtime);
		}
			break;
		default:
			throw new TMQLRuntimeException("Unknown state.");
		}
	}

	/**
	 * The method is called to interpret the given sub-expression by using the
	 * given runtime. The interpretation will call the sub-expression if the
	 * given expression isn't a leaf in parsing-tree.
	 * 
	 * <p>
	 * The interpretation will transform the value on top of the stack and put
	 * its results also on top.
	 * </p>
	 * 
	 * @param runtime
	 *            the runtime which contains all necessary information for
	 *            querying process
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private void interpreteTupleExpression(TMQLRuntime runtime)
			throws TMQLRuntimeException {

		QueryMatches matches = extractArguments(runtime, TupleExpression.class,
				0);
		Collection<String> keys = matches.getOrderedKeys();
		int index = 0;
		/*
		 * iterate over variables
		 */
		for (String variable : getVariables()) {
			/*
			 * generate unique variable name
			 */
			while (keys.contains("$" + index)) {
				index++;
			}
			/*
			 * rename tuple
			 */
			if (matches.getOrderedKeys().contains(variable)) {
				matches = matches.renameVariable(variable, "$" + index);
			} else {
				Map<String, Object> tuple = HashUtil.getHashMap();
				tuple.put("$" + index, runtime.getProperties().newSequence());
				matches.add(tuple);
			}
			index++;
		}

		/*
		 * set on top of stack
		 */
		runtime.getRuntimeContext().peek().setValue(
				VariableNames.QUERYMATCHES, matches);
	}

	/**
	 * The method is called to interpret the given sub-expression by using the
	 * given runtime. The interpretation will call the sub-expression if the
	 * given expression isn't a leaf in parsing-tree.
	 * 
	 * <p>
	 * The interpretation will transform the value on top of the stack and put
	 * its results also on top.
	 * </p>
	 * 
	 * @param runtime
	 *            the runtime which contains all necessary information for
	 *            querying process
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private void interpreteParameterPairs(TMQLRuntime runtime)
			throws TMQLRuntimeException {

		QueryMatches[] parameters = extractArguments(runtime,
				ParameterPair.class);

		/*
		 * iterate over subexpressions
		 */
		for (int index = 0; index < parameters.length; index++) {
			/*
			 * rename variables
			 */
			parameters[index] = parameters[index].renameVariable(QueryMatches
					.getNonScopedVariable(), "$" + index);
		}

		/*
		 * set on top of stack
		 */
		runtime.getRuntimeContext().peek().setValue(
				VariableNames.QUERYMATCHES,
				new QueryMatches(runtime, parameters));

	}

}
