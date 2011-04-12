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
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.Variable;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * 
 * Special interpreter class to interpret variable-expressions.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * variable ::= ( $ | % | @ ) [ a-zA-Z0-9_ ]+
 * <p>
 * </code>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class VariableInterpreter extends ExpressionInterpreterImpl<Variable> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public VariableInterpreter(Variable ex) {
		super(ex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		final String variable =getTokens().get(0);
		if ( context.getCurrentTuple() != null && context.getCurrentTuple().containsValue(variable)){
			return QueryMatches.asQueryMatchNS(runtime, context.getCurrentTuple().get(variable));
		}
		return QueryMatches.emptyMatches();
	}

}
