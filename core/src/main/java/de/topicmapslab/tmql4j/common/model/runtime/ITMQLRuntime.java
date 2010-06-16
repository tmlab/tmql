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
package de.topicmapslab.tmql4j.common.model.runtime;

import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.tmql4j.api.model.IDataBridge;
import de.topicmapslab.tmql4j.common.context.TMQLRuntimeProperties;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.converter.QueryFactory;
import de.topicmapslab.tmql4j.event.model.EventManager;
import de.topicmapslab.tmql4j.extensions.core.ExtensionPointAdapter;
import de.topicmapslab.tmql4j.interpreter.core.predefinition.Environment;
import de.topicmapslab.tmql4j.parser.model.IParserTree;

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
	 * @param query
	 *            the query to execute
	 * @return the create query
	 * @throws TMQLRuntimeException
	 *             thrown if execution fails
	 */
	public IQuery run(String query) throws TMQLRuntimeException;

	/**
	 * Method return the internal event manager of the runtime. The event
	 * manager is used to handle new events and notify the registered listener.
	 * 
	 * @return the eventManager the reference of the internal event manager
	 */
	public EventManager getEventManager();

	/**
	 * Method returns the internal reference of the {@link IDataBridge}
	 * instance.
	 * 
	 * @return the dataBridge the internal data-bridge reference
	 */
	public IDataBridge getDataBridge();

	/**
	 * Setter of the internal reference of the {@link IDataBridge} instance.
	 * 
	 * @param bridge
	 *            the internal data-bridge reference
	 */
	public void setDataBridge(IDataBridge bridge);

	/**
	 * Setter of the internal reference of the {@link IDataBridge} instance.
	 * 
	 * <p>
	 * the given class will be instantiate and used as parameter of
	 * {@link #setDataBridge(IDataBridge)}
	 * </p>
	 * 
	 * @param clazz
	 *            the class of the internal data-bridge reference
	 * @throws TMQLRuntimeException
	 *             thrown if the instantiation failed
	 */
	public void setDataBridge(Class<? extends IDataBridge> clazz)
			throws TMQLRuntimeException;

	/**
	 * Getter method of the internal TMQL runtime properties instance.
	 * 
	 * @return the internal instance reference
	 */
	public TMQLRuntimeProperties getProperties();

	/**
	 * Getter method of the internal Environment definition
	 * 
	 * @return the internal reference
	 */
	public Environment getEnvironment();

	/**
	 * Method returns the internal state of the verbose flag.
	 * 
	 * @return <code>true</code> if runtime is verbose, which means that debug
	 *         information will be printed to the given print stream,
	 *         <code>false</code> otherwise
	 */
	public boolean isVerbose();

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
	 */
	public ExtensionPointAdapter getExtensionPointAdapter();

	/**
	 * Returns the internal reference of the value store containing all values
	 * stored during the querying process.
	 * 
	 * @return the value store
	 */
	public IValueStore getValueStore();

	/**
	 * Method returns the internal reference to the topic map system used to
	 * interpret XTM or CTM content.
	 * 
	 * @return the internal reference of the topic map system
	 */
	public TopicMapSystem getTopicMapSystem();

	/**
	 * Method returns the queried topic map.
	 * 
	 * @return the queried topic map
	 */
	public TopicMap getTopicMap();

	/**
	 * Set the given topic map to use as environment map, if the environment was
	 * already initialized, it will be removed.
	 * 
	 * @param environmentMap
	 *            the environmentMap to set
	 */
	public void setEnvironmentMap(TopicMap environmentMap);

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
}
