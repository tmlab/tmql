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
package de.topicmapslab.tmql4j.select.components.interpreter;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.productions.ValueExpression;
import de.topicmapslab.tmql4j.select.grammar.productions.FromClause;

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

	/**
	 * {@inheritDoc}
	 */	
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {	
		/*
		 * redirect to value-expression
		 */
		IExpressionInterpreter<ValueExpression> ex = getInterpretersFilteredByEypressionType(
				runtime, ValueExpression.class).get(0);
		return ex.interpret(runtime, context, optionalArguments);
	}

}
