/* 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.flwr.components.interpreter;

import java.util.StringTokenizer;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.flwr.grammar.productions.NonInterpretedContent;
import de.topicmapslab.tmql4j.flwr.grammar.productions.XMLContent;

/**
 * Interpreter for simple non-interpreted content of XML or TM content. Simple
 * transform the tokens to text.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class NonInterpretedContentInterpreter extends ExpressionInterpreterImpl<NonInterpretedContent> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public NonInterpretedContentInterpreter(NonInterpretedContent ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public String interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * transform string-represented tokens to text
		 */
		StringBuilder builder = new StringBuilder();
		for (String token : getTokens()) {
			builder.append(token);
			builder.append(" ");
		}

		/*
		 * clean white-spaces of XML
		 */
		if (getExpression().getParent() instanceof XMLContent) {
			String origin = builder.toString();
			StringTokenizer tokenizer = new StringTokenizer(origin, " ");
			builder = new StringBuilder();
			if (origin.startsWith(" ")) {
				builder.append(" ");
			}
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				builder.append(token);
				if (!token.endsWith("=") && tokenizer.hasMoreTokens()) {
					builder.append(" ");
				}
			}
			if (origin.endsWith(" ")) {
				builder.append(" ");
			}
		}

		return builder.toString();
	}

}
