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

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.impl.tmapi2.MemoryTopicMapSystemImpl;
import net.ontopia.topicmaps.query.core.DeclarationContextIF;
import net.ontopia.topicmaps.query.core.InvalidQueryException;
import net.ontopia.topicmaps.query.core.ParsedModificationStatementIF;
import net.ontopia.topicmaps.query.core.ParsedQueryIF;
import net.ontopia.topicmaps.query.core.QueryProcessorIF;
import net.ontopia.topicmaps.query.core.QueryResultIF;
import net.ontopia.topicmaps.query.tmql.impl.util.ColumnHeaderGenerator;
import net.ontopia.utils.OntopiaException;

import org.tmapi.core.FactoryConfigurationException;
import org.tmapi.core.TMAPIException;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.TMQLRuntimeFactory;
import de.topicmapslab.tmql4j.draft2010.components.processor.runtime.TmqlRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.query.IQuery;

public class TMQL4JQueryProcessor implements QueryProcessorIF {

	private TopicMapSystemFactory factory;
	private TopicMapSystem topicMapSystem;
	private TopicMap topicMap;
	private String tmid;
	private ITMQLRuntime runtime;

	/**
	 * constructor
	 * 
	 * @param topicMap
	 *            the topic map
	 */
	public TMQL4JQueryProcessor(TopicMapIF topicMap) {
		try {
			factory = TopicMapSystemFactory.newInstance();
			topicMapSystem = factory.newTopicMapSystem();
			TopicMap topicmap = ((MemoryTopicMapSystemImpl) topicMapSystem).createTopicMap(topicMap);
			this.tmid = "";
			this.runtime = TMQLRuntimeFactory.newFactory().newRuntime(topicMapSystem, TmqlRuntime.TMQL_2010);
			this.topicMap = topicmap;
		} catch (TMAPIException e) {
			e.printStackTrace();
		} catch (TMQLRuntimeException e) {
			e.printStackTrace();
		}

	}

	/**
	 * constructor
	 * 
	 * @param topicMap
	 *            the topic map
	 * @param tmid
	 *            the topic map id
	 * @throws OntopiaException
	 */
	public TMQL4JQueryProcessor(TopicMapIF topicMap, final String tmid) throws OntopiaException {
		try {
			factory = TopicMapSystemFactory.newInstance();
			topicMapSystem = factory.newTopicMapSystem();
			TopicMap topicmap = ((MemoryTopicMapSystemImpl) topicMapSystem).createTopicMap(topicMap);
			this.tmid = tmid;
			this.runtime = TMQLRuntimeFactory.newFactory().newRuntime(topicMapSystem, TmqlRuntime.TMQL_2010);
			this.topicMap = topicmap;
		} catch (TMAPIException e) {
			e.printStackTrace();
			throw new OntopiaException("Error", e);
		} catch (TMQLRuntimeException e) {
			e.printStackTrace();
			throw new OntopiaException("Error", e);
		}

	}

	/**
	 * constructor
	 * 
	 * @param topicMap
	 *            the topic map
	 * @throws OntopiaException
	 */
	public TMQL4JQueryProcessor(TopicMap topicMap) throws OntopiaException {
		this.tmid = "";

		try {
			this.factory = TopicMapSystemFactory.newInstance();
		} catch (FactoryConfigurationException e) {
			this.factory = new net.ontopia.topicmaps.impl.tmapi2.TopicMapSystemFactory();
			e.printStackTrace();
		}
		try {
			this.topicMapSystem = factory.newTopicMapSystem();
			this.runtime = TMQLRuntimeFactory.newFactory().newRuntime(topicMapSystem, TmqlRuntime.TMQL_2010);
			this.topicMap = topicMap;
		} catch (TMAPIException e) {
			e.printStackTrace();
			throw new OntopiaException("Error", e);
		} catch (TMQLRuntimeException e) {
			e.printStackTrace();
			throw new OntopiaException("Error", e);
		}

	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public QueryResultIF execute(String query) throws InvalidQueryException {
		List<String> headers = new LinkedList<String>();
		IQuery q = new Tmql4OntopiaQuery(topicMap, query, tmid);
		try {
			runtime.run(q);
			headers = ColumnHeaderGenerator.columnHeaders(runtime, q);
			return new TMQL4JQueryResult(tmid, q.getResults(), headers);

		} catch (TMQLRuntimeException e) {
			e.printStackTrace();
			throw new InvalidQueryException(e);
		}

	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public QueryResultIF execute(String query, DeclarationContextIF context) throws InvalidQueryException {
		return execute(query);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public QueryResultIF execute(String query, Map<String, ?> arg1) throws InvalidQueryException {
		return execute(query);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public QueryResultIF execute(String query, Map<String, ?> arg1, DeclarationContextIF arg2) throws InvalidQueryException {
		return execute(query);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public void load(String arg0) throws InvalidQueryException {
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public void load(Reader arg0) throws InvalidQueryException, IOException {
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public ParsedQueryIF parse(String query) throws InvalidQueryException {
		return new TMQL4JParsedQuery(topicMapSystem, topicMap, runtime, query, tmid);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public ParsedQueryIF parse(String query, DeclarationContextIF arg1) throws InvalidQueryException {
		return new TMQL4JParsedQuery(topicMapSystem, topicMap, runtime, query, tmid);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public int update(String arg0) throws InvalidQueryException {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public ParsedModificationStatementIF parseUpdate(String arg0) throws InvalidQueryException {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public ParsedModificationStatementIF parseUpdate(String arg0, DeclarationContextIF arg1) throws InvalidQueryException {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public int update(String arg0, DeclarationContextIF arg1) throws InvalidQueryException {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public int update(String arg0, Map<String, ?> arg1) throws InvalidQueryException {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public int update(String arg0, Map<String, ?> arg1, DeclarationContextIF arg2) throws InvalidQueryException {
		throw new UnsupportedOperationException();
	}

}
