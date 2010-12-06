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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.ontopia.topicmaps.query.core.InvalidQueryException;
import net.ontopia.topicmaps.query.core.ParsedQueryIF;
import net.ontopia.topicmaps.query.core.QueryResultIF;
import net.ontopia.topicmaps.query.tmql.impl.util.ColumnHeaderGenerator;

import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.tmql4j.components.parser.IParserTree;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.query.IQuery;

public class TMQL4JParsedQuery implements ParsedQueryIF {

	private final IParserTree tree;
	private final ITMQLRuntime runtime;
	private final IQuery query;

	public TMQL4JParsedQuery(final TopicMapSystem topicMapSystem, TopicMap topicMap, final ITMQLRuntime runtime, final String query, final String tmid) {
		this(topicMapSystem, topicMap, runtime, new Tmql4OntopiaQuery(topicMap, query, tmid), tmid);
	}

	public TMQL4JParsedQuery(final TopicMapSystem topicMapSystem, TopicMap topicMap, final ITMQLRuntime runtime, final IQuery query, final String tmid) {
		this.runtime = runtime;
		this.query = query;
		this.tree = runtime.parse(query);
	}

	public QueryResultIF execute() throws InvalidQueryException {
		List<String> headers = new LinkedList<String>();
		try {
			runtime.run(query);
			headers = ColumnHeaderGenerator.columnHeaders(runtime, query);
			return new TMQL4JQueryResult(((Tmql4OntopiaQuery) query).getTopicMapId(), query.getResults(), headers);
		} catch (TMQLRuntimeException e) {
			e.printStackTrace();
			throw new InvalidQueryException(e);
		}

	}

	public QueryResultIF execute(Map<String, ?> arg0) throws InvalidQueryException {
		return execute();
	}

	public Collection<String> getAllVariables() {
		return tree.root().getVariables();
	}

	public Collection<String> getCountedVariables() {
		return new LinkedList<String>();
	}

	public List<String> getOrderBy() {
		return Collections.emptyList();
	}

	public List<String> getSelectedVariables() {
		return Collections.emptyList();
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
