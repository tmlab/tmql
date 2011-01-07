/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.components.processor.runtime;

import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.tmql4j.components.parser.IParserTree;
import de.topicmapslab.tmql4j.components.processor.ITmqlProcessor;
import de.topicmapslab.tmql4j.components.processor.results.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.ResultSet;
import de.topicmapslab.tmql4j.components.processor.runtime.IConstructResolver;
import de.topicmapslab.tmql4j.components.processor.runtime.ILanguageContext;
import de.topicmapslab.tmql4j.components.processor.runtime.TmqlConstructResolver;
import de.topicmapslab.tmql4j.components.processor.runtime.TmqlRuntimeImpl;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.extension.ExtensionPointAdapter;
import de.topicmapslab.tmql4j.extension.IExtensionPointAdapter;
import de.topicmapslab.tmql4j.path.components.processor.TmqlProcessor2007;
import de.topicmapslab.tmql4j.path.components.processor.runtime.module.LanguageContext;
import de.topicmapslab.tmql4j.path.query.TMQLQuery;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * @author Sven Krosse
 * 
 */
public class TmqlRuntime2007 extends TmqlRuntimeImpl {

	public static final String TMQL_2007 = "TMQL-2007";

	private final IExtensionPointAdapter extensionPointAdapter;
	private final IConstructResolver constructResolver;

	/**
	 * constructor
	 */
	public TmqlRuntime2007() {
		this(null);
	}

	/**
	 * constructor
	 * 
	 * @param topicMapSystem
	 *            the topic map system
	 * @throws TMQLRuntimeException
	 */
	public TmqlRuntime2007(TopicMapSystem topicMapSystem) throws TMQLRuntimeException {
		super(topicMapSystem);
		this.constructResolver = new TmqlConstructResolver(this);
		this.extensionPointAdapter = new ExtensionPointAdapter(this);
		this.extensionPointAdapter.loadExtensionPoints();
	}

	/**
	 * {@inheritDoc}
	 */
	public IExtensionPointAdapter getExtensionPointAdapter() throws UnsupportedOperationException {
		return extensionPointAdapter;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExtensionMechanismSupported() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doRun(IQuery query) throws TMQLRuntimeException {
		if (query.getTopicMap() == null) {
			throw new TMQLRuntimeException("Topic map not set to query instance!");
		}
		if (!query.getQueryString().isEmpty()) {
			ITmqlProcessor processor = getTmqlProcessor();
			IResultSet<?> results = processor.query(query);
			query.setResults(results);
		} else {
			query.setResults(ResultSet.emptyResultSet());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected IQuery toQuery(TopicMap topicMap, String query) {
		return new TMQLQuery(topicMap, query);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IParserTree parse(String query) throws TMQLRuntimeException {
		IQuery q = new TMQLQuery(null, query);
		/*
		 * add restrictions
		 */
		addRestrictions(q);
		/*
		 * before-execution call to query
		 */
		q.beforeQuery(this);
		/*
		 * redirect to real implementation
		 */
		IParserTree tree = getTmqlProcessor().parse(q);
		/*
		 * after-execution call to query
		 */
		q.afterQuery(this);
		return tree;
	}

	/**
	 * {@inheritDoc}
	 */
	protected ILanguageContext createLanguageContext() {
		return new LanguageContext(this);
	}

	/**
	 * {@inheritDoc}
	 */
	protected ITmqlProcessor createTmqlProcessor() {
		return new TmqlProcessor2007(this);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public IConstructResolver getConstructResolver() {
		return constructResolver;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLanguageName() {
		return TMQL_2007;
	}

	/**
	 * {@inheritDoc}
	 */
	protected String[] getModificationExpressionTypeNames() {
		return TMQLQuery.modificationExpressions;
	}
}
