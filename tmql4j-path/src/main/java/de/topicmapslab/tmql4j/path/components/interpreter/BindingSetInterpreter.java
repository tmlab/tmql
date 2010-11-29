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
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.productions.BindingSet;
import de.topicmapslab.tmql4j.path.grammar.productions.VariableAssignment;

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
public class BindingSetInterpreter extends ExpressionInterpreterImpl<BindingSet> {

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
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * call all sub-expressions
		 */
		QueryMatches[] bindings = extractArguments(runtime, VariableAssignment.class, context, optionalArguments);
		return new QueryMatches(runtime, bindings);
	}

}
