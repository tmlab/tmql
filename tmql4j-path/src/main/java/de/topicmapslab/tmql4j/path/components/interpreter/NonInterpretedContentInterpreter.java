/* 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.components.interpreter;

import java.util.Map;
import java.util.StringTokenizer;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.util.HashUtil;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.productions.NonInterpretedContent;
import de.topicmapslab.tmql4j.path.grammar.productions.XMLContent;

/**
 * Interpreter for simple non-interpreted content of XML or TM content. Simple
 * transform the tokens to text.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class NonInterpretedContentInterpreter extends
		ExpressionInterpreterImpl<NonInterpretedContent> {

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
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
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

		String content = builder.toString();

		/*
		 * store text as tuple
		 */
		Map<String, Object> tuple = HashUtil.getHashMap();
		tuple.put(QueryMatches.getNonScopedVariable(), content);
		QueryMatches result = new QueryMatches(runtime);
		result.add(tuple);

		/*
		 * store result
		 */
		runtime.getRuntimeContext().peek()
				.setValue(VariableNames.QUERYMATCHES, result);
	}

}
