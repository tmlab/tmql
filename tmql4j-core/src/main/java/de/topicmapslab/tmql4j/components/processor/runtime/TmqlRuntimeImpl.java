/*
 * TMQL4J - Javabased TMQL Engine
 *
 * Copyright: Copyright 2009 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.runtime;

import org.tmapi.core.FactoryConfigurationException;
import org.tmapi.core.TMAPIException;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;

import de.topicmapslab.tmql4j.components.processor.ITmqlProcessor;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.query.IQuery;
import de.topicmapslab.tmql4j.query.QueryFactory;

/**
 * Core component of the processing model of the TMQL4J engine. The runtime
 * encapsulate the whole TMQL process chain and store internal states and
 * results. An instance of {@link TmqlRuntimeImpl} is used as public interface
 * to Java applications using the TMQL engine.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public abstract class TmqlRuntimeImpl implements ITMQLRuntime {

	/**
	 * the internal cache instance to store values
	 */
	private final IValueStore valueStore;

	/**
	 * the internal reference of the language context
	 */
	private final ILanguageContext languageContext;

	/**
	 * the internal reference of the query processor
	 */
	private final ITmqlProcessor processor;

	/**
	 * the topic map system used to create new temporary topic maps to interpret
	 * XTM or CTM
	 */
	private TopicMapSystem topicMapSystem;

	/**
	 * constructor
	 */
	public TmqlRuntimeImpl() {
		this(null);
	}

	/**
	 * constructor
	 * 
	 * @param topicMapSystem
	 *            the topic map system used to handle CTM or XTM content
	 * @throws TMQLRuntimeException
	 *             thrown if the runtime cannot be instantiate
	 */
	public TmqlRuntimeImpl(final TopicMapSystem topicMapSystem) throws TMQLRuntimeException {
		this.topicMapSystem = topicMapSystem;
		this.valueStore = createValueStore();
		this.languageContext = createLanguageContext();
		this.processor = createTmqlProcessor();
	}

	/**
	 * {@inheritDoc}
	 */
	public TopicMapSystem getTopicMapSystem() {
		if (topicMapSystem == null) {
			try {
				topicMapSystem = TopicMapSystemFactory.newInstance().newTopicMapSystem();
			} catch (FactoryConfigurationException e) {
				throw new TMQLRuntimeException(e);
			} catch (TMAPIException e) {
				throw new TMQLRuntimeException(e);
			}
		}
		return topicMapSystem;
	}
	


	/**
	 * {@inheritDoc}
	 */
	public void setTopicMapSystem(TopicMapSystem system) {
		this.topicMapSystem = system;
	}

	/**
	 * {@inheritDoc}
	 */
	public void run(IQuery query) throws TMQLRuntimeException {
		/*
		 * before-execution call to query
		 */
		query.beforeQuery(this);
		/*
		 * redirect to real implementation
		 */
		doRun(query);
		/*
		 * after-execution call to query
		 */
		query.afterQuery(this);
	}

	protected abstract void doRun(IQuery query) throws TMQLRuntimeException;

	/**
	 * {@inheritDoc}
	 */
	public IQuery run(TopicMap topicMap, String query) throws TMQLRuntimeException {
		IQuery q = QueryFactory.getFactory().getTmqlQuery(topicMap, query);
		if (q == null) {
			throw new TMQLRuntimeException("Given query is not a TMQL query or cannot transform to TMQL.");
		}
		run(q);
		return q;
	}

	/**
	 * {@inheritDoc}
	 */
	public ILanguageContext getLanguageContext() {
		return languageContext;
	}

	/**
	 * Method to create new language context
	 * 
	 * @return the language context
	 */
	protected abstract ILanguageContext createLanguageContext();

	/**
	 * {@inheritDoc}
	 */
	public IValueStore getValueStore() {
		return valueStore;
	}

	/**
	 * Method to create new value store
	 * 
	 * @return the value store
	 */
	protected abstract IValueStore createValueStore();

	/**
	 * {@inheritDoc}
	 */
	public ITmqlProcessor getTmqlProcessor() {
		return processor;
	}

	/**
	 * Method to create a new TMQl processor
	 * 
	 * @return the processor
	 */
	protected abstract ITmqlProcessor createTmqlProcessor();

}
