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
package de.topicmapslab.tmql4j.insert.components.interpreter;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.insert.grammar.productions.TMContent;
import de.topicmapslab.tmql4j.insert.util.CTMConverter;

/**
 * 
 * Special interpreter class to interpret tm-content.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * tm-content ::= ctm-fragment
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TMContentInterpreter extends ExpressionInterpreterImpl<TMContent> {

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
	public TMContentInterpreter(TMContent ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		StringBuilder builder = new StringBuilder();
		/*
		 * iterate over all internal interpreter instances
		 */
		for (IExpressionInterpreter<?> interpreter : getInterpreters(runtime)) {
			/*
			 * is simple text content
			 */
			if (interpreter instanceof NonInterpretedContentInterpreter) {
				String content = interpreter.interpret(runtime, context, optionalArguments);
				builder.append(content);
			}
			/*
			 * is embed query
			 */
			else {
				QueryMatches matches = interpreter.interpret(runtime, context, optionalArguments);
				Collection<?> values = null;
				/*
				 * values are bound to '$$$$' because of embed path-expression
				 */
				if (matches.getOrderedKeys().contains(QueryMatches.getNonScopedVariable())) {
					values = matches.getPossibleValuesForVariable();
				}
				/*
				 * values are bound to '$0' because of embed flwr- or
				 * select-expression
				 */
				else if (matches.getOrderedKeys().contains("$0")) {
					values = matches.getPossibleValuesForVariable("$0");
				} else {
					logger.warn("Variable binding is missing. Expects '$0' or '$$$$' but found '" + matches.getOrderedKeys() + "'");
					continue;
				}
				/*
				 * results as CTM
				 */
				builder.append(CTMConverter.toCTMString(values));
			}
		}
		return QueryMatches.asQueryMatchNS(runtime, builder.toString());
	}
}
