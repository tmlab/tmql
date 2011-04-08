/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2011.path.components.processor.runtime;

import java.io.OutputStream;

import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.tmql4j.components.parser.IParserTree;
import de.topicmapslab.tmql4j.components.processor.ITmqlProcessor;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.model.ResultSet;
import de.topicmapslab.tmql4j.components.processor.runtime.IConstructResolver;
import de.topicmapslab.tmql4j.components.processor.runtime.ILanguageContext;
import de.topicmapslab.tmql4j.components.processor.runtime.TmqlConstructResolver;
import de.topicmapslab.tmql4j.components.processor.runtime.TmqlRuntimeImpl;
import de.topicmapslab.tmql4j.draft2011.path.components.processor.TmqlProcessor2011;
import de.topicmapslab.tmql4j.draft2011.path.components.processor.runtime.module.LanguageContext;
import de.topicmapslab.tmql4j.draft2011.path.query.TMQLQuery;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.extension.ExtensionPointAdapter;
import de.topicmapslab.tmql4j.extension.IExtensionPointAdapter;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * @author Sven Krosse
 * 
 */
public class TmqlRuntime2011 extends TmqlRuntimeImpl {

	public static final String TMQL_2011 = "TMQL-2011";

	private final IExtensionPointAdapter extensionPointAdapter;
	private final IConstructResolver constructResolver;

	/**
	 * constructor
	 */
	public TmqlRuntime2011() {
		this(null);
	}

	/**
	 * constructor
	 * 
	 * @param topicMapSystem
	 *            the topic map system
	 * @throws TMQLRuntimeException
	 */
	public TmqlRuntime2011(TopicMapSystem topicMapSystem) throws TMQLRuntimeException {
		super(topicMapSystem);
		this.constructResolver = new TmqlConstructResolver(this);
		this.extensionPointAdapter = new ExtensionPointAdapter(this);
		this.extensionPointAdapter.loadExtensionPoints();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IExtensionPointAdapter getExtensionPointAdapter() throws UnsupportedOperationException {
		return extensionPointAdapter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isExtensionMechanismSupported() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doRun(IQuery query, OutputStream stream) throws TMQLRuntimeException {
		if (query.getTopicMap() == null) {
			throw new TMQLRuntimeException("Topic map not set to query instance!");
		}
		if (!query.getQueryString().isEmpty()) {
			ITmqlProcessor processor = createTmqlProcessor();
			IResultSet<?> results = processor.query(query, stream);
			query.setResults(results);
		} else {
			query.setResults(ResultSet.emptyResultSet());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IQuery toQuery(TopicMap topicMap, String query) {
		return new TMQLQuery(topicMap, query);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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
	@Override
	protected ILanguageContext createLanguageContext() {
		return new LanguageContext(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ITmqlProcessor createTmqlProcessor() {
		return new TmqlProcessor2011(this);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public IConstructResolver getConstructResolver() {
		return constructResolver;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLanguageName() {
		return TMQL_2011;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String[] getModificationExpressionTypeNames() {
		return TMQLQuery.modificationExpressions;
	}
}
