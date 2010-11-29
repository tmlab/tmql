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
import de.topicmapslab.tmql4j.path.grammar.productions.OffsetClause;

/**
 * 
 * Special interpreter class to interpret offset-clauses.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * offset-clause ::=  OFFSET  value-expression 
 * </code>
 * </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class OffsetClauseInterpreter extends
		ExpressionInterpreterImpl<OffsetClause> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public OffsetClauseInterpreter(OffsetClause ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * set value of OFFSET to $_lower
		 */
		Map<String, Object> tuple = HashUtil.getHashMap();
		tuple.put(VariableNames.OFFSET,new BigInteger(getTokens().get(1)));
		QueryMatches result = new QueryMatches(runtime);
		result.add(tuple);
		/*
		 * store on top of the variable layer
		 */
		runtime.getRuntimeContext().peek().setValue(
				VariableNames.QUERYMATCHES, result);
	}

}
