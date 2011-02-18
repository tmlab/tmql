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
package de.topicmapslab.tmql4j.flwr.components.interpreter;

import java.math.BigInteger;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.flwr.grammar.productions.LimitClause;
import de.topicmapslab.tmql4j.grammar.productions.PreparedExpression;

/**
 * 
 * Special interpreter class to interpret limit-clauses.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * limit-clause ::= LIMIT integer
 * </code>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class LimitClauseInterpreter extends ExpressionInterpreterImpl<LimitClause> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public LimitClauseInterpreter(LimitClause ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public BigInteger interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		if (containsExpressionsType(PreparedExpression.class)) {
			QueryMatches matches = getInterpretersFilteredByEypressionType(runtime, PreparedExpression.class).get(0).interpret(runtime, context, optionalArguments);
			return new BigInteger(matches.getFirstValue().toString());
		}
		return new BigInteger(getTokens().get(1));
	}
}
