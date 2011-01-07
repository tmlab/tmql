/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2010.components.processor.runtime;

import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.tmql4j.components.parser.IParserTree;
import de.topicmapslab.tmql4j.components.processor.ITmqlProcessor;
import de.topicmapslab.tmql4j.components.processor.results.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.ResultSet;
import de.topicmapslab.tmql4j.components.processor.runtime.IConstructResolver;
import de.topicmapslab.tmql4j.components.processor.runtime.ILanguageContext;
import de.topicmapslab.tmql4j.components.processor.runtime.TMQLRuntimeFactory;
import de.topicmapslab.tmql4j.components.processor.runtime.TmqlConstructResolver;
import de.topicmapslab.tmql4j.components.processor.runtime.TmqlRuntimeImpl;
import de.topicmapslab.tmql4j.draft2010.components.processor.TmqlProcessor;
import de.topicmapslab.tmql4j.draft2010.components.processor.runtime.module.LanguageContext;
import de.topicmapslab.tmql4j.draft2010.query.TMQLQuery;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.extension.ExtensionPointAdapter;
import de.topicmapslab.tmql4j.extension.IExtensionPointAdapter;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * A TMQL runtime supporting the new draft
 * 
 * @author Sven Krosse
 * 
 */
public class TmqlRuntime extends TmqlRuntimeImpl {

	/**
	 * Name of the language handled by this runtime. This argument should be
	 * used for {@link TMQLRuntimeFactory#newRuntime(TopicMapSystem, String)}
	 */
	public static final String TMQL_2010 = "TMQL-2010";

	/**
	 * the extension point adapter
	 */
	private final IExtensionPointAdapter extensionPointAdapter;
	/**
	 * the construct resolver
	 */
	private final IConstructResolver constructResolver;

	/**
	 * constructor
	 */
	public TmqlRuntime() {
		this(null);
	}

	/**
	 * constructor
	 * 
	 * @param topicMapSystem
	 *            the topic map system
	 * @throws TMQLRuntimeException
	 */
	public TmqlRuntime(TopicMapSystem topicMapSystem) throws TMQLRuntimeException {
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
		return new TmqlProcessor(this);
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
		return TMQL_2010;
	}

	/**
	 * {@inheritDoc}
	 */
	protected String[] getModificationExpressionTypeNames() {
		return TMQLQuery.modificationExpressions;
	}
		
	/**
	 * {@inheritDoc}
	 */
	protected IQuery toQuery(TopicMap topicMap, String query) {
		return new TMQLQuery(topicMap, query);
	}
}
