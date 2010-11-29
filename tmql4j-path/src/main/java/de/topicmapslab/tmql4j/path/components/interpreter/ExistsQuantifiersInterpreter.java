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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.lexical.Some;
import de.topicmapslab.tmql4j.path.grammar.productions.ExistsQuantifiers;

/**
 * 
 * Special interpreter class to interpret exists-quantifiers.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * exists-quantifier ::= some | at least integer | at most integer
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ExistsQuantifiersInterpreter extends
		ExpressionInterpreterImpl<ExistsQuantifiers> {
	
	
	/**
	 * the Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
	
	
	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public ExistsQuantifiersInterpreter(ExistsQuantifiers ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * Log it
		 */
		logger.info("Start!");

		/*
		 * set value to the variable $_quantifies
		 */
		if (getTmqlTokens().get(0).equals(Some.class)) {
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.QUANTIFIES, BigInteger.valueOf(1));
		} else {
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.QUANTIFIES,
					new BigInteger(getTokens().get(2)));
		}

		/*
		 * Log it
		 */
		logger.info(
				"Finished! Results: "
						+ runtime.getRuntimeContext().peek().getValue(
								VariableNames.QUANTIFIES));

		/*
		 * Nothing more to do -> no subexpression allowed
		 */
	}

}
