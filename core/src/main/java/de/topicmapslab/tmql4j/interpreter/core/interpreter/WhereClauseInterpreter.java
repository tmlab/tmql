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
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.parser.core.expressions.BooleanExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.WhereClause;

/**
 * 
 * Special interpreter class to interpret where-clause.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * where-clause ::= WHERE boolean-expression
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class WhereClauseInterpreter extends
		ExpressionInterpreterImpl<WhereClause> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public WhereClauseInterpreter(WhereClause ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * redirect to subexpression
		 */
		IExpressionInterpreter<BooleanExpression> ex = getInterpretersFilteredByEypressionType(
				runtime, BooleanExpression.class).get(0);
		ex.interpret(runtime);
	}
}
