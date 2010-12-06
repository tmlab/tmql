/*
 * TMQL4J - Javabased TMQL Engine
 *
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.runtime;

import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.tmql4j.components.parser.IParserTree;
import de.topicmapslab.tmql4j.components.processor.ITmqlProcessor;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.extension.IExtensionPointAdapter;
import de.topicmapslab.tmql4j.query.IQuery;
import de.topicmapslab.tmql4j.query.QueryFactory;

/**
 * Core component of the processing model of the TMQL4J engine. The runtime
 * encapsulate the whole TMQL process chain and store internal states and
 * results. An instance of {@link ITMQLRuntime} is used as public interface to
 * Java applications using the TMQL engine.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface ITMQLRuntime {

	/**
	 * Execution method of the TMQL runtime. <br />
	 * <br />
	 * Method starts the execution of the encapsulated processing chain and
	 * store the total execution time.
	 * 
	 * @param query
	 *            the query to execute
	 * @throws TMQLRuntimeException
	 *             thrown if execution fails
	 */
	public void run(IQuery query) throws TMQLRuntimeException;

	/**
	 * Execution method of the TMQL runtime. <br />
	 * <br />
	 * Method starts the execution of the encapsulated processing chain and
	 * store the total execution time.
	 * 
	 * <p>
	 * The runtime will be create a {@link IQuery} representation by calling
	 * {@link QueryFactory#buildQuery(String)} and transform the given query to
	 * a TMQL query.
	 * </p>
	 * 
	 * @param topicMap
	 *            the topicMap
	 * @param query
	 *            the query to execute
	 * @return the create query
	 * @throws TMQLRuntimeException
	 *             thrown if execution fails
	 */
	public IQuery run(TopicMap topicMap, String query) throws TMQLRuntimeException;

	/**
	 * Method return the internal instance of the language context containing
	 * all interpreters, prefixes, functions and tokens used for querying.
	 * 
	 * @return the reference of the language context
	 */
	public ILanguageContext getLanguageContext();

	/**
	 * Method get access to the internal extension point manager
	 * 
	 * @return the reference
	 * @throws UnsupportedOperationException
	 *             thrown if the extension mechanism is not supported by the
	 *             current TMQL runtime
	 */
	public IExtensionPointAdapter getExtensionPointAdapter() throws UnsupportedOperationException;

	/**
	 * Returns a boolean value represent the support of extension mechanisms of
	 * this TMQL runtime.
	 * 
	 * @return <code>true</code> if the runtime supports extensions,
	 *         <code>false</code> otherwise.
	 */
	public boolean isExtensionMechanismSupported();

	/**
	 * Setting the topic map system used to create temporary maps on merge or
	 * special exports
	 * 
	 * @param system
	 *            the system
	 */
	public void setTopicMapSystem(TopicMapSystem system);

	/**
	 * Method returns the internal reference to the topic map system used to
	 * interpret XTM or CTM content.
	 * 
	 * @return the internal reference of the topic map system
	 */
	public TopicMapSystem getTopicMapSystem();

	/**
	 * Method parse the given query and return the parser tree object
	 * 
	 * @param query
	 *            the query
	 * @return the parser tree and never <code>null</code>
	 * @throws TMQLRuntimeException
	 *             thrown if parser fails because of invalid syntax
	 */
	public IParserTree parse(final String query) throws TMQLRuntimeException;
	
	/**
	 * Method parse the given query and return the parser tree object
	 * 
	 * @param query
	 *            the query
	 * @return the parser tree and never <code>null</code>
	 * @throws TMQLRuntimeException
	 *             thrown if parser fails because of invalid syntax
	 */
	public IParserTree parse(final IQuery query) throws TMQLRuntimeException;

	/**
	 * Returns the internal instance of TMQL query processor.
	 * 
	 * @return the TMQL processor
	 */
	public ITmqlProcessor getTmqlProcessor();

	/**
	 * Returns the internal instance of the construct resolver
	 * 
	 * @return the construct resolver
	 */
	public IConstructResolver getConstructResolver();
	
	/**
	 * Returns the name of the supported language.
	 * @return the language name
	 */
	public String getLanguageName();

}
