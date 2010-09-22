/**
 * TMQL4J Plugin for Ontopia
 * 
 * Copyright: Copyright 2009 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * Author: Sven Krosse
 * 
 */
package net.ontopia.topicmaps.query.tmql.impl.basic;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.ontopia.topicmaps.query.core.InvalidQueryException;
import net.ontopia.topicmaps.query.core.ParsedQueryIF;
import net.ontopia.topicmaps.query.core.QueryResultIF;
import net.ontopia.topicmaps.query.tmql.impl.tmql4jextension.OntopiaParserProcessingTaskChain;
import net.ontopia.topicmaps.query.tmql.impl.tmql4jextension.WrappedOntopiaResult;
import net.ontopia.topicmaps.query.tmql.impl.tmql4jextension.WrappedOntopiaResultSet;
import net.ontopia.topicmaps.query.tmql.impl.util.ColumnHeaderGenerator;

import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.tmql4j.common.context.TMQLRuntimeProperties;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLValueStore;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.parser.core.expressions.OrderByClause;
import de.topicmapslab.tmql4j.parser.core.expressions.SelectExpression;
import de.topicmapslab.tmql4j.parser.model.IExpression;
import de.topicmapslab.tmql4j.parser.model.IParserTree;

public class TMQL4JParsedQuery implements ParsedQueryIF {

	private final IParserTree tree;
	private final ITMQLRuntime runtime;
	private final IQuery query;

	public TMQL4JParsedQuery(final TopicMapSystem topicMapSystem,
			TopicMap topicMap, final ITMQLRuntime runtime, final String query,
			final String tmid) {
		this(topicMapSystem, topicMap, runtime, new TMQLQuery(query), tmid);
	}

	public TMQL4JParsedQuery(final TopicMapSystem topicMapSystem,
			TopicMap topicMap, final ITMQLRuntime runtime, final IQuery query,
			final String tmid) {
		this.runtime = runtime;
		this.query = query;
		OntopiaParserProcessingTaskChain chain = new OntopiaParserProcessingTaskChain(
				runtime, query);
		try {
			TMQLRuntimeProperties properties = runtime.getProperties();
			properties.setProperty(
					TMQLRuntimeProperties.RESULT_SET_IMPLEMENTATION_CLASS,
					WrappedOntopiaResultSet.class.getCanonicalName());
			properties.setProperty(
					TMQLRuntimeProperties.RESULT_TUPLE_IMPLEMENTATION_CLASS,
					WrappedOntopiaResult.class.getCanonicalName());
			runtime.getValueStore().put(
					TMQLValueStore.ValueKeys.TMQL_RUNTIME_CUSTOM, tmid);
			chain.execute();
		} catch (TMQLRuntimeException e) {
			e.printStackTrace();
		}
		this.tree = chain.getParserTree();
	}

	public QueryResultIF execute() throws InvalidQueryException {
		List<String> headers = new LinkedList<String>();
		try {
			runtime.run(query);
			headers = ColumnHeaderGenerator.columnHeaders(runtime, query);
			return new TMQL4JQueryResult((WrappedOntopiaResultSet) query
					.getResults(), headers);
		} catch (TMQLRuntimeException e) {
			e.printStackTrace();
			throw new InvalidQueryException(e);
		}

	}

	public QueryResultIF execute(Map<String, ?> arg0)
			throws InvalidQueryException {
		return execute();
	}

	public Collection<String> getAllVariables() {
		return tree.root().getVariables();
	}

	public Collection<String> getCountedVariables() {
		return new LinkedList<String>();
	}

	public List<String> getOrderBy() {
		List<String> result = new LinkedList<String>();
		for (IExpression expression : tree.root().getExpressionFilteredByType(
				OrderByClause.class)) {
			for (String variable : expression.getVariables()) {
				if (!result.contains(variable)) {
					result.add(variable);
				}
			}
		}
		return result;
	}

	public List<String> getSelectedVariables() {
		List<String> result = new LinkedList<String>();

		for (IExpression expression : tree.root().getExpressionFilteredByType(
				SelectExpression.class)) {
			for (String variable : expression.getVariables()) {
				if (!result.contains(variable)) {
					result.add(variable);
				}
			}
		}

		return result;
	}

	public boolean isOrderedAscending(String variable) {
		return false;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (tree != null) {
			tree.toStringTree(builder);
		} else {
			builder.append("Parse TMQL-Query failed!");
		}
		return builder.toString();
	}

}
