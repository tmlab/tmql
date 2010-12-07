/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.interpreter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.prepared.IPreparedStatement;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.PreparedExpression;

/**
 * @author Sven Krosse
 *
 */
public class PreparedExpressionInterpreter extends ExpressionInterpreterImpl<PreparedExpression> {

	private static final Logger logger = LoggerFactory.getLogger(PreparedExpressionInterpreter.class);
	
	/**
	 * constructor
	 * @param ex the expression
	 */
	public PreparedExpressionInterpreter(PreparedExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		if ( context.getQuery() instanceof IPreparedStatement){
			IPreparedStatement statement = (IPreparedStatement) context.getQuery();
			return QueryMatches.asQueryMatchNS(runtime, statement.get(getExpression()));
		}
		logger.warn("Prepared expression only valid for prepared statements");
		return QueryMatches.emptyMatches();
	}

}
