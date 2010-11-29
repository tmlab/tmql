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
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.productions.FromClause;
import de.topicmapslab.tmql4j.path.grammar.productions.ValueExpression;

/**
 * 
 * Special interpreter class to interpret from-clauses.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * form-clause ::= FROM   value-expression
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class FromClauseInterpreter extends
		ExpressionInterpreterImpl<FromClause> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public FromClauseInterpreter(FromClause ex) {
		super(ex);
	}

	/*
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * redirect to value-expression
		 */
		IExpressionInterpreter<ValueExpression> ex = getInterpretersFilteredByEypressionType(
				runtime, ValueExpression.class).get(0);
		ex.interpret(runtime);
	}

}
