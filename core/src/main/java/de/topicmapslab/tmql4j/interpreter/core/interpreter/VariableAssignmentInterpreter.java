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
import de.topicmapslab.tmql4j.parser.core.expressions.Content;
import de.topicmapslab.tmql4j.parser.core.expressions.VariableAssignment;

/**
 * 
 * Special interpreter class to interpret variable-assignments.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * variable-assignment ::= variable in content
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class VariableAssignmentInterpreter extends
		ExpressionInterpreterImpl<VariableAssignment> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public VariableAssignmentInterpreter(VariableAssignment ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * Read variable name
		 */
		final String variable = getTokens().get(0);

		/*
		 * call content
		 */
		QueryMatches content = extractArguments(runtime, Content.class, 0);

		QueryMatches results = null;
		/*
		 * rename non-scoped variable by variable name of this
		 * variable-assignment
		 */
		if (content.getOrderedKeys().contains(
				QueryMatches.getNonScopedVariable())) {
			results = content.extractAndRenameBindingsForVariable(variable);
		} else {
			results = new QueryMatches(runtime);
			/*
			 * iterate over all contained variables
			 */
			for (String key : content.getOrderedKeys()) {
				/*
				 * iterate over all values
				 */
				for (Object obj : content.getPossibleValuesForVariable(key)) {
					/*
					 * extract the value of the given value
					 */
					extract(obj, results, variable);
				}
			}
		}

		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);
	}

	/**
	 * Internal method to extract the value of a variable binding from the
	 * generated variables bindings of the contained variable-assignment.
	 * 
	 * @param obj
	 *            the object representing the values to extract
	 * @param matches
	 *            the extracted values
	 * @param variable
	 *            the variable which identifies the values to extract
	 * @throws TMQLRuntimeException
	 *             thrown if extraction fails
	 */
	private void extract(final Object obj, final QueryMatches matches,
			final String variable) throws TMQLRuntimeException {
		/*
		 * check if value is a sequence
		 */
		if (obj instanceof Collection<?>) {
			/*
			 * iterate over values and call method iterative
			 */
			for (Object o : (Collection<?>) obj) {
				extract(o, matches, variable);
			}
		}
		/*
		 * value is an atom
		 */
		else {
			Map<String, Object> tuple = HashUtil.getHashMap();
			tuple.put(variable, obj);
			matches.add(tuple);
		}
	}
}
