/*
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
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.AliasExpression;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * The interpreter of {@link AliasExpression}
 * 
 * @author Sven Krosse
 * 
 */
public class AliasExpressionInterpreter extends ExpressionInterpreterImpl<AliasExpression> {

	/**
	 * base constructor to create a new instance.
	 * 
	 * @param ex
	 *            the expression which shall be handled by this instance.
	 */
	public AliasExpressionInterpreter(AliasExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * get current index
		 */
		int index = context.getCurrentIndexInTuple();
		if (index < 0) {
			throw new TMQLRuntimeException("Index of element in tuple not set!");
		}
		/*
		 * get token and register as alias
		 */
		final String token = getTokens().get(1);
		context.getTmqlProcessor().getResultProcessor().setColumnAlias(index, LiteralUtils.asString(token));
		/*
		 * return empty match
		 */
		return QueryMatches.emptyMatches();
	}

}
