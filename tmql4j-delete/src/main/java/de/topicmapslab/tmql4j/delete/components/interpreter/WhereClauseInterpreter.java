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
package de.topicmapslab.tmql4j.delete.components.interpreter;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.delete.grammar.productions.WhereClause;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.productions.BooleanExpression;

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
public class WhereClauseInterpreter extends ExpressionInterpreterImpl<WhereClause> {

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
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * redirect to subexpression
		 */
		IExpressionInterpreter<BooleanExpression> ex = getInterpretersFilteredByEypressionType(runtime, BooleanExpression.class).get(0);
		return ex.interpret(runtime, context, optionalArguments);
	}
}
