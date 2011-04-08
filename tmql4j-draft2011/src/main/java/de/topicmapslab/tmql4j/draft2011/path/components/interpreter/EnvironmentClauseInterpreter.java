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
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.EnvironmentClause;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
*
* Special interpreter class to interpret environment-clauses.
* 
*<p>
 * The grammar production rule of the expression is: <code>
 * 
 * <p>
 * environment-clause ::= ( directive | pragma ) +
 * </p>
 * 
 * </code> </p>
* 
* 
* @author Sven Krosse
* @email krosse@informatik.uni-leipzig.de
*
*/
public class EnvironmentClauseInterpreter
		extends ExpressionInterpreterImpl<EnvironmentClause> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public EnvironmentClauseInterpreter(EnvironmentClause ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * iterate over sub-expression
		 */
		for (IExpressionInterpreter<?> ex : getInterpreters(runtime)) {
			/*
			 * redirect to sub-expression
			 */
			ex.interpret(runtime, context, optionalArguments);
		}
		return QueryMatches.emptyMatches();
	}
	

}
