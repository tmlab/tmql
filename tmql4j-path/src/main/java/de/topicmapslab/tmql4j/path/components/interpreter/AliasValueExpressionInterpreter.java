/*
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
import de.topicmapslab.tmql4j.path.grammar.productions.AliasExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.AliasValueExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.ValueExpression;

/**
 * Interpreter class of {@link AliasExpression}
 * 
 * @author Sven Krosse
 * 
 */
public class AliasValueExpressionInterpreter extends ExpressionInterpreterImpl<AliasValueExpression> {

	/**
	 * @param ex
	 */
	public AliasValueExpressionInterpreter(AliasValueExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {

		/*
		 * execute value-expression
		 */
		QueryMatches matches = extractArguments(runtime, ValueExpression.class, 0, context, optionalArguments);
		/*
		 * execute alias if exists
		 */
		if (containsExpressionsType(AliasExpression.class)) {
			extractArguments(runtime, AliasExpression.class, context, optionalArguments);
		}
		return matches;
	}

}
