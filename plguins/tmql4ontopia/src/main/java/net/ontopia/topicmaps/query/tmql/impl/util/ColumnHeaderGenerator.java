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

import net.ontopia.topicmaps.query.tmql.impl.tmql4jextension.WrappedOntopiaResult;
import net.ontopia.topicmaps.query.tmql.impl.tmql4jextension.WrappedOntopiaResultSet;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.expressions.Expression;
import de.topicmapslab.tmql4j.parser.core.expressions.Content;
import de.topicmapslab.tmql4j.parser.core.expressions.FlwrExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.PathExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.Postfix;
import de.topicmapslab.tmql4j.parser.core.expressions.PostfixedExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.PredicateInvocation;
import de.topicmapslab.tmql4j.parser.core.expressions.ProjectionPostfix;
import de.topicmapslab.tmql4j.parser.core.expressions.QueryExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.ReturnClause;
import de.topicmapslab.tmql4j.parser.core.expressions.SelectClause;
import de.topicmapslab.tmql4j.parser.core.expressions.SelectExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.SimpleContent;
import de.topicmapslab.tmql4j.parser.core.expressions.TupleExpression;
import de.topicmapslab.tmql4j.parser.model.IExpression;
import de.topicmapslab.tmql4j.parser.model.IParserTree;

public class ColumnHeaderGenerator {

	private ColumnHeaderGenerator() {

	}

	public static final List<String> columnHeaders(ITMQLRuntime runtime,
			IQuery query) throws TMQLRuntimeException {
		IParserTree tree = runtime.getValueStore().getParserTree();
		IExpression expression = tree.root();
		return columnHeaders(runtime, expression, query);
	}

	private static final List<String> columnHeaders(ITMQLRuntime runtime,
			IExpression expression, IQuery query) throws TMQLRuntimeException {
		List<SelectExpression> selectExpressions = expression
				.getExpressionFilteredByType(SelectExpression.class);
		if (!selectExpressions.isEmpty()) {
			return columnHeadersOfSelectExpression(runtime, selectExpressions
					.get(0), query);
		} else {
			List<FlwrExpression> flwrExpressions = expression
					.getExpressionFilteredByType(FlwrExpression.class);
			if (!flwrExpressions.isEmpty()) {
				return columnHeadersOfFlwrExpression(runtime, flwrExpressions
						.get(0), query);
			} else {
				List<PathExpression> pathExpressions = expression
						.getExpressionFilteredByType(PathExpression.class);
				if (!pathExpressions.isEmpty()) {
					return columnHeadersOfPathExpression(runtime,
							pathExpressions.get(0), query);
				} else {
					List<Expression> expressions = expression
							.getExpressionFilteredByType(Expression.class);
					if (!expressions.isEmpty()) {
						return columnHeadersOfExpression(runtime, expressions
								.get(0), query);
					} else {
						throw new TMQLRuntimeException(
								"Invalid parser-tree structure.");
					}
				}

			}
		}
	}

	private static final List<String> columnHeadersOfSelectExpression(
			ITMQLRuntime runtime, IExpression expression, IQuery query)
			throws TMQLRuntimeException {
		List<String> headers = new LinkedList<String>();
		for (IExpression ex : expression.getExpressionFilteredByType(
				SelectClause.class).get(0).getExpressions()) {
			headers.add(expressionToString(ex));
		}
		return headers.isEmpty() ? defaultColumnHeaders(runtime, query)
				: headers;
	}

	private static final List<String> columnHeadersOfExpression(
			ITMQLRuntime runtime, IExpression expression, IQuery query)
			throws TMQLRuntimeException {
		List<String> headers = new LinkedList<String>();
		headers.add(expressionToString(expression));
		return headers;
	}

	private static final List<String> columnHeadersOfFlwrExpression(
			ITMQLRuntime runtime, IExpression expression, IQuery query)
			throws TMQLRuntimeException {
		List<String> headers = new LinkedList<String>();
		IExpression returnClause = expression.getExpressionFilteredByType(
				ReturnClause.class).get(0);
		IExpression contentClause = returnClause.getExpressionFilteredByType(
				Content.class).get(0);
		if (contentClause.getExpressionFilteredByType(QueryExpression.class)
				.isEmpty()) {
			headers.add(expressionToString(contentClause));
		} else {
			headers.addAll(columnHeaders(runtime, contentClause
					.getExpressionFilteredByType(QueryExpression.class).get(0),
					query));
		}

		return headers.isEmpty() ? defaultColumnHeaders(runtime, query)
				: headers;
	}

	private static final List<String> columnHeadersOfPathExpression(
			ITMQLRuntime runtime, IExpression expression, IQuery query)
			throws TMQLRuntimeException {
		List<String> headers = new LinkedList<String>();
		IExpression path = expression;

		if (!path.getExpressionFilteredByType(PredicateInvocation.class)
				.isEmpty()) {
			headers.add(expressionToString(path));
		} else {
			IExpression postfixedExpression = path.getExpressionFilteredByType(
					PostfixedExpression.class).get(0);
			/*
			 * tuple-expression | simple-content {postfix}
			 */
			List<String> filters = new LinkedList<String>();
			if (!postfixedExpression.getExpressionFilteredByType(Postfix.class)
					.isEmpty()) {
				IExpression postfix = postfixedExpression
						.getExpressionFilteredByType(Postfix.class).get(0);
				if (postfix.getGrammarType() == 1) {
					IExpression tupleExpression = postfix
							.getExpressionFilteredByType(
									ProjectionPostfix.class).get(0)
							.getExpressionFilteredByType(TupleExpression.class)
							.get(0);
					for (IExpression valueExpression : tupleExpression
							.getExpressions()) {
						filters.add("(" + expressionToString(valueExpression)
								+ ")");
					}
				} else {
					filters.add(expressionToString(postfix));
				}
			}
			/*
			 * tuple-expression
			 */
			if (postfixedExpression.getGrammarType() == 0) {
				IExpression tupleExpression = postfixedExpression
						.getExpressionFilteredByType(TupleExpression.class)
						.get(0);
				for (IExpression valueExpression : tupleExpression
						.getExpressions()) {
					String prefix = expressionToString(valueExpression);
					if (filters.isEmpty()) {
						headers.add(prefix);
					} else {
						for (String filter : filters) {
							headers.add(prefix + filter);
						}
					}
				}
			}
			/*
			 * simple-content
			 */
			else {
				String prefix = expressionToString(postfixedExpression
						.getExpressionFilteredByType(SimpleContent.class)
						.get(0));
				if (filters.isEmpty()) {
					headers.add(prefix);
				} else {
					for (String filter : filters) {
						headers.add(prefix + filter);
					}
				}

			}
		}
		return headers.isEmpty() ? defaultColumnHeaders(runtime, query)
				: headers;
	}

	private static final List<String> defaultColumnHeaders(
			ITMQLRuntime runtime, IQuery query) throws TMQLRuntimeException {
		List<String> headers = new LinkedList<String>();
		WrappedOntopiaResultSet resultSet = (WrappedOntopiaResultSet) query
				.getResults();
		if (resultSet != null) {
			long size = 0;
			if (resultSet.size() > 0) {
				size = ((WrappedOntopiaResult) resultSet.first()).size();
			}
			for (int index = 0; index < size; index++) {
				headers.add("Column " + index);
			}
		}
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
