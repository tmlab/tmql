/**
 * TMQL4J Plugin for Ontopia
 * 
 * Copyright: Copyright 2009 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * Author: Sven Krosse
 * 
 */
package net.ontopia.topicmaps.query.tmql.impl.util;

import java.util.LinkedList;
import java.util.List;

import de.topicmapslab.tmql4j.components.parser.IParserTree;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.Expression;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.query.IQuery;

public class ColumnHeaderGenerator {

	private ColumnHeaderGenerator() {

	}

	public static final List<String> columnHeaders(ITMQLRuntime runtime, IQuery query) throws TMQLRuntimeException {
		IParserTree tree = runtime.parse(query);
		IExpression expression = tree.root();
		return columnHeaders(runtime, expression, query);
	}

	private static final List<String> columnHeaders(ITMQLRuntime runtime, IExpression expression, IQuery query) throws TMQLRuntimeException {

		List<Expression> expressions = expression.getExpressionFilteredByType(Expression.class);
		if (!expressions.isEmpty()) {
			return columnHeadersOfExpression(runtime, expressions.get(0), query);
		} else {
			throw new TMQLRuntimeException("Invalid parser-tree structure.");
		}
	}

	private static final List<String> columnHeadersOfExpression(ITMQLRuntime runtime, IExpression expression, IQuery query) throws TMQLRuntimeException {
		List<String> headers = new LinkedList<String>();
		headers.add(expressionToString(expression));
		return headers;
	}

	private static final String expressionToString(IExpression expression) {
		StringBuilder builder = new StringBuilder();
		for (String token : expression.getTokens()) {
			builder.append(token + " ");
		}
		return builder.toString().trim();
	}

}
