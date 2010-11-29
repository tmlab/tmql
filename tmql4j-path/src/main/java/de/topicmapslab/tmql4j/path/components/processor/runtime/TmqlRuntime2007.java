/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.components.processor.runtime;

import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.tmql4j.components.parser.IParserTree;
import de.topicmapslab.tmql4j.components.processor.ITmqlProcessor;
import de.topicmapslab.tmql4j.components.processor.runtime.IConstructResolver;
import de.topicmapslab.tmql4j.components.processor.runtime.ILanguageContext;
import de.topicmapslab.tmql4j.components.processor.runtime.IValueStore;
import de.topicmapslab.tmql4j.components.processor.runtime.TmqlRuntimeImpl;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.extensions.IExtensionPointAdapter;
import de.topicmapslab.tmql4j.path.components.processor.TmqlProcessor2007;
import de.topicmapslab.tmql4j.path.components.processor.runtime.module.LanguageContext;
import de.topicmapslab.tmql4j.path.components.processor.runtime.module.ValueStore;
import de.topicmapslab.tmql4j.path.extensions.ExtensionPointAdapter;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * @author Sven Krosse
 * 
 */
public class TmqlRuntime2007 extends TmqlRuntimeImpl {

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
		this.extensionPointAdapter = new ExtensionPointAdapter(this);
		this.constructResolver = new TmqlConstructResolver(this);
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
	public IParserTree parse(String query) throws TMQLRuntimeException {
		// TODO implement
		throw new UnsupportedOperationException();
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
			processor.query(query);
		}
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
	protected IValueStore createValueStore() {
		return new ValueStore();
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
}
