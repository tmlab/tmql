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

import java.math.BigInteger;
import java.util.Map;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.util.HashUtil;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.productions.LimitClause;

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
public class LimitClauseInterpreter extends
		ExpressionInterpreterImpl<LimitClause> {

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
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {		
		/*
		 * set value of OFFSET to $_limit
		 */
		Map<String, Object> tuple = HashUtil.getHashMap();
		tuple.put(VariableNames.LIMIT,new BigInteger(getTokens().get(1)));
		QueryMatches result = new QueryMatches(runtime);
		result.add(tuple);
		/*
		 * store on top of the variable layer
		 */
		runtime.getRuntimeContext().peek().setValue(
				VariableNames.QUERYMATCHES, result);

	}
}
