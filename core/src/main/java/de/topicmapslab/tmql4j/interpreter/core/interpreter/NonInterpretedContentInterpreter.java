/* 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.interpreter.core.interpreter;

import java.util.Map;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.parser.core.expressions.NonInterpretedContent;

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

		final String content = builder.toString().trim();

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
		runtime.getRuntimeContext().peek().setValue(
				VariableNames.QUERYMATCHES, result);
	}

}
