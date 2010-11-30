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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.productions.Pragma;

/**
 * 
 * Special interpreter class to interpret pragmas.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * pragma ::= %pragma identifier QIRI
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PragmaInterpreter extends ExpressionInterpreterImpl<Pragma> {

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
	public PragmaInterpreter(Pragma ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * extract identifier
		 */
		String identifier = getTokens().get(1);
		/*
		 * extract QIRI
		 */
		String qiri = getTokens().get(2);

		/*
		 * check if topic is taxonometry topic
		 */
		if (identifier.equalsIgnoreCase("taxonometry")) {
			/*
			 * check if value is tm:intransitive
			 */
			if (qiri.equalsIgnoreCase("tm:intransitive")) {
				context.setTransitive(false);
			}
			/*
			 * check if value is tm:transitive
			 */
			else if (qiri.equalsIgnoreCase("tm:transitive")) {
				context.setTransitive(true);
			} else {
				logger.warn("Value '" + qiri + "' is unknown for pragma '" + identifier + "'");
			}
		} else {
			logger.warn("Pragma '" + identifier + "' with value '" + qiri + "' is unknown.");
		}
		return QueryMatches.emptyMatches();
	}

}
