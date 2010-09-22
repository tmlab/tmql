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
import de.topicmapslab.tmql4j.parser.core.expressions.BindingSet;
import de.topicmapslab.tmql4j.parser.core.expressions.VariableAssignment;

/**
 * 
 * Special interpreter class to interpret binding-sets.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * binding-set ::= < variable-assignment >
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class BindingSetInterpreter extends
		ExpressionInterpreterImpl<BindingSet> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public BindingSetInterpreter(BindingSet ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * call all sub-expressions
		 */
		QueryMatches[] bindings = extractArguments(runtime,
				VariableAssignment.class);
		/*
		 * set results
		 */
		QueryMatches results = new QueryMatches(runtime, bindings);
		runtime.getRuntimeContext().peek().setValue(
				VariableNames.QUERYMATCHES, results);
	}

}
