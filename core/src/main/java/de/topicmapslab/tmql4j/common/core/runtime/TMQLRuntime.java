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
package de.topicmapslab.tmql4j.common.core.runtime;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.tmapi.core.FactoryConfigurationException;
import org.tmapi.core.TMAPIException;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;

import de.topicmapslab.java.tmapi.extension.impl.ExtendedTopicMapImpl;
import de.topicmapslab.java.tmapi.extension.model.base.ExtendedTopicMap;
import de.topicmapslab.java.tmapi.extension.model.index.SupertypeSubtypeIndex;
import de.topicmapslab.tmql4j.api.DataBridgeRegistry;
import de.topicmapslab.tmql4j.api.model.IDataBridge;
import de.topicmapslab.tmql4j.common.context.TMQLRuntimeProperties;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.process.ParsingProcessingChain;
import de.topicmapslab.tmql4j.common.core.process.ProcessingTaskChainImpl;
import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.common.model.runtime.ILanguageContext;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.common.model.runtime.IValueStore;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.converter.QueryFactory;
import de.topicmapslab.tmql4j.event.model.EventManager;
import de.topicmapslab.tmql4j.extensions.core.ExtensionPointAdapter;
import de.topicmapslab.tmql4j.interpreter.core.base.context.RuntimeContextImpl;
import de.topicmapslab.tmql4j.interpreter.core.predefinition.Environment;
import de.topicmapslab.tmql4j.interpreter.model.context.IInitialContext;
import de.topicmapslab.tmql4j.interpreter.model.context.IRuntimeContext;
import de.topicmapslab.tmql4j.parser.model.IExpression;
import de.topicmapslab.tmql4j.parser.model.IParserTree;
import de.topicmapslab.tmql4j.resultprocessing.core.reduction.ReductionResultSet;
import de.topicmapslab.tmql4j.resultprocessing.core.reduction.ReductionTupleResult;
import de.topicmapslab.tmql4j.resultprocessing.model.ResultType;

/**
 * Core component of the processing model of the TMQL4J engine. The runtime
 * encapsulate the whole TMQL process chain and store internal states and
 * results. An instance of {@link TMQLRuntime} is used as public interface to
 * Java applications using the TMQL engine.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TMQLRuntime implements ITMQLRuntime {

	/**
	 * the internal cache instance to store values
	 */
	private final IValueStore store;

	/**
	 * the internal reference of the language context
	 */
	private final ILanguageContext languageContext;

	/**
	 * the initial context containing the predefined environment and variables
	 */
	private IInitialContext initialContext;
	/**
	 * runtime context containing a stack of current variable bindings and
	 * representing the current state of execution
	 */
	private IRuntimeContext runtimeContext;
	/**
	 * the topic map system used to create new temporary topic maps to interpret
	 * XTM or CTM
	 */
	private TopicMapSystem topicMapSystem;
	/**
	 * a topic map which shall be queried
	 */
	private final TopicMap topicMap;
	/**
	 * internal flag representing verbose mode
	 */
	private final boolean verbose;
	/**
	 * the print stream to print debug information if verbose flag is
	 * <code>true</code>
	 */
	private final PrintStream printStream;

	/**
	 * flag indicates the transitivity
	 */
	private boolean actsTransitive = false;

	/**
	 * the internal event manager which is used to handle events during the TMQL
	 * execution
	 */
	private final EventManager eventManager;

	/**
	 * the internal reference to the TMQL-date-bridge
	 */
	private IDataBridge dataBridge;

	/**
	 * the internal runtime properties
	 */
	private final TMQLRuntimeProperties properties;

	/**
	 * the environment
	 */
	private Environment environment = null;

	/**
	 * initial variable bindings
	 */
	public final Map<String, Object> variables = HashUtil.getHashMap();

	/**
	 * the extension adapter
	 */
	private final ExtensionPointAdapter adapter = new ExtensionPointAdapter();

	/**
	 * the topic map to use as environment map
	 */
	private TopicMap environmentMap;

	/**
	 * base constructor
	 * 
	 * @param topicMapSystem
	 *            the topic map system used to handle CTM or XTM content
	 * @param topicMaps
	 *            a collection of topic maps to query
	 * @see TMQLRuntime#TMQLRuntime(TopicMapSystem, Collection, boolean,
	 *      boolean, PrintStream, ExpressionTypes...))
	 * @throws TMQLRuntimeException
	 *             thrown if the data-bridge instance cannot be instantiate
	 */
	protected TMQLRuntime(final TopicMapSystem topicMapSystem,
			final TopicMap topicMap) throws TMQLRuntimeException {
		this(topicMapSystem, topicMap, false, false, null);
	}

	/**
	 * base constructor
	 * 
	 * @param topicMapSystem
	 *            the topic map system used to handle CTM or XTM content
	 * @param topicMap
	 *            a topic maps to query
	 * @param expressionTypes
	 *            a list of all allowed expression types *
	 * @see TMQLRuntime#TMQLRuntime(TopicMapSystem, Collection, boolean,
	 *      boolean, PrintStream, ExpressionTypes...))
	 * @throws TMQLRuntimeException
	 *             thrown if the data-bridge instance cannot be instantiate
	 */
	protected TMQLRuntime(final TopicMapSystem topicMapSystem,
			final TopicMap topicMap,
			Class<? extends IExpression>... expressionTypes)
			throws TMQLRuntimeException {
		this(topicMapSystem, topicMap, false, false, null, expressionTypes);
	}

	/**
	 * Extended constructor to set verbose mode <br />
	 * <br />
	 * constructor is calling
	 * {@link TMQLRuntime#TMQLRuntime(TopicMapSystem, Collection, boolean, boolean, PrintStream, ExpressionTypes...)
	 * )} with default print stream {@link System#out}.
	 * 
	 * @param topicMapSystem
	 *            the topic map system used to handle CTM or XTM content
	 * @param topicMap
	 *            a topic maps to query
	 * @param verbose
	 *            flag used to define verbose mode
	 * @throws TMQLRuntimeException
	 *             thrown if the data-bridge instance cannot be instantiate
	 * 
	 */
	protected TMQLRuntime(final TopicMapSystem topicMapSystem,
			final TopicMap topicMap, final boolean verbose)
			throws TMQLRuntimeException {
		this(topicMapSystem, topicMap, verbose, true, System.out);
	}

	/**
	 * Extended constructor to set internal caching mode. <br />
	 * <br />
	 * constructor is calling
	 * {@link TMQLRuntime#TMQLRuntime(TopicMapSystem, Collection, boolean, boolean, PrintStream, ExpressionTypes...)
	 * )} with default print stream {@link System#out}
	 * 
	 * @param topicMapSystem
	 *            the topic map system used to handle CTM or XTM content
	 * @param topicMap
	 *            a topic maps to query
	 * @param verbose
	 *            flag used to define verbose mode
	 * @param autoReindex
	 *            enable or disable auto indexing of internal indexes -
	 *            {@link SupertypeSubtypeIndex}
	 * @see TMQLRuntime#TMQLRuntime(TopicMapSystem, Collection, boolean,
	 *      boolean, PrintStream, ExpressionTypes...))
	 * @throws TMQLRuntimeException
	 *             thrown if the data-bridge instance cannot be instantiate
	 */
	protected TMQLRuntime(final TopicMapSystem topicMapSystem,
			final TopicMap topicMap, final boolean verbose,
			final boolean autoReindex) throws TMQLRuntimeException {
		this(topicMapSystem, topicMap, verbose, autoReindex, System.out);
	}

	/**
	 * Extended constructor to set internal caching mode, set internal verbose
	 * mode and print stream.
	 * 
	 * @param topicMapSystem
	 *            the topic map system used to handle CTM or XTM content
	 * @param topicMap
	 *            a topic maps to query
	 * @param verbose
	 *            flag used to define verbose mode
	 * @param autoReindex
	 *            enable or disable auto indexing of internal indexes -
	 *            {@link SupertypeSubtypeIndex}
	 * @param printStream
	 *            the print stream used to print out debug information if
	 *            verbose is enabled
	 * @throws TMQLRuntimeException
	 *             thrown if the data-bridge instance cannot be instantiate
	 */
	@SuppressWarnings("unchecked")
	protected TMQLRuntime(final TopicMapSystem topicMapSystem,
			final TopicMap topicMap, final boolean verbose,
			final boolean autoReindex, final PrintStream printStream)
			throws TMQLRuntimeException {
		this(topicMapSystem, topicMap, verbose, autoReindex, printStream,
				new Class[0]);
	}

	/**
	 * Extended constructor to set internal caching mode, set internal verbose
	 * mode and print stream.
	 * 
	 * @param topicMapSystem
	 *            the topic map system used to handle CTM or XTM content
	 * @param topicMap
	 *            a topic maps to query
	 * @param verbose
	 *            flag used to define verbose mode
	 * @param autoReindex
	 *            enable or disable auto indexing of internal indexes -
	 *            {@link SupertypeSubtypeIndex}
	 * @param printStream
	 *            the print stream used to print out debug information if
	 *            verbose is enabled
	 * @param allowedExpressionTypes
	 *            a list of all allowed expression types *
	 * @throws TMQLRuntimeException
	 *             thrown if the data-bridge instance cannot be instantiate
	 */
	protected TMQLRuntime(final TopicMapSystem topicMapSystem,
			final TopicMap topicMap, final boolean verbose,
			final boolean autoReindex, final PrintStream printStream,
			Class<? extends IExpression>... allowedExpressionTypes)
			throws TMQLRuntimeException {
		this.topicMapSystem = topicMapSystem;
		this.topicMap = topicMap;
		this.properties = new TMQLRuntimeProperties(this);

		/*
		 * is auto indexing enabled
		 */
		if (autoReindex) {
			/*
			 * wrap topic map to extended topic map if necessary
			 */
			ExtendedTopicMap extendedTopicMap = null;
			if (topicMap instanceof ExtendedTopicMap) {
				extendedTopicMap = (ExtendedTopicMap) topicMap;
			} else {
				extendedTopicMap = new ExtendedTopicMapImpl(topicMap);
			}
			/*
			 * get super-type-sub-type-index of the topic map
			 */
			SupertypeSubtypeIndex index = (SupertypeSubtypeIndex) extendedTopicMap
					.getIndex(SupertypeSubtypeIndex.class);
			if (!index.isOpen()) {
				index.open();
			}
			/*
			 * run re-indexer
			 */
			index.reindex(topicMap);
		}
		this.verbose = verbose;
		this.printStream = printStream;
		this.store = new TMQLValueStore();
		this.eventManager = new EventManager();
		this.dataBridge = DataBridgeRegistry.getRegistry()
				.newDataBridgeInstance();
		try {
			this.languageContext = new TMQLLanguageContext(this,
					allowedExpressionTypes);
		} catch (Exception e) {
			throw new TMQLRuntimeException(
					"Language context cannot be initialized", e);
		}
		/*
		 * loading extensions
		 */
		adapter.loadExtensionPoints(this);

	}

	/**
	 * {@inheritDoc}
	 */
	public TopicMapSystem getTopicMapSystem() {
		if ( topicMapSystem == null ){
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
	public TopicMap getTopicMap() {
		return topicMap;
	}

	/**
	 * Method returns the current content of the runtime. The runtime context
	 * store current variable bindings and the state of the execution.
	 * 
	 * @return the runtime context
	 * @throws TMQLRuntimeException
	 *             thrown if initialization of runtime context failed
	 * @see TMQLRuntime#initRuntimeContext()
	 */
	public IRuntimeContext getRuntimeContext() throws TMQLRuntimeException {
		if (runtimeContext == null) {
			runtimeContext = new RuntimeContextImpl(initialContext);
		}
		return runtimeContext;
	}

	/**
	 * Method used to set the runtime context to current runtime. Method is
	 * called by the running processing chain.
	 * 
	 * @param runtimeContext
	 *            the new runtime context
	 */
	public void setRuntimeContext(IRuntimeContext runtimeContext) {
		this.runtimeContext = runtimeContext;
	}

	/**
	 * Method returns the initial context of a TMQL execution containing the
	 * predefined system variables and the predefined environment.
	 * 
	 * @return the initial context
	 */
	public IInitialContext getInitialContext() {
		return initialContext;
	}

	/**
	 * Method used to set the initial context used as initial state of the
	 * processing model. Method is called by the running processing chain after
	 * finishing the initialization task. After set the internal reference the
	 * runtime tries to initialize predefined TMQL functions.
	 * 
	 * @param initialContext
	 *            the initial context
	 * @throws TMQLRuntimeException
	 *             thrown if registration of TMQL function fails
	 */
	public void setInitialContext(IInitialContext initialContext)
			throws TMQLRuntimeException {
		this.initialContext = initialContext;
	}

	/**
	 * Method returns the internal state of the verbose flag.
	 * 
	 * @return <code>true</code> if runtime is verbose, which means that debug
	 *         information will be printed to the given print stream,
	 *         <code>false</code> otherwise
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * Method return the internal reference of the print stream used to print
	 * out debug information of verbose flag is set
	 * 
	 * @return the reference of the print stream
	 */
	public PrintStream getPrintStream() {
		return printStream;
	}

	/**
	 * {@inheritDoc}
	 */
	public void run(IQuery query) throws TMQLRuntimeException {		

		/*
		 * read expected result type
		 */
		final String resultType = properties.getResultType();
		/*
		 * check if result type is XML or CTM
		 */
		if (resultType.equalsIgnoreCase(ResultType.CTM.name())
				|| resultType.equalsIgnoreCase(ResultType.XML.name())) {
			/*
			 * change to reduction result set
			 */
			properties.setProperty(
					TMQLRuntimeProperties.RESULT_SET_IMPLEMENTATION_CLASS,
					ReductionResultSet.class.getCanonicalName());
			properties.setProperty(
					TMQLRuntimeProperties.RESULT_TUPLE_IMPLEMENTATION_CLASS,
					ReductionTupleResult.class.getCanonicalName());
		}
		/*
		 * initialize and execute new processing chain
		 */
		new ProcessingTaskChainImpl(this, query).execute();

		/*
		 * store result as part of the query
		 */
		if (query instanceof TMQLQuery) {
			((TMQLQuery) query).setResults(store.getResultSet());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public IQuery run(String query) throws TMQLRuntimeException {
		IQuery q = QueryFactory.getFactory().getTmqlQuery(query);
		if (q == null) {
			throw new TMQLRuntimeException(
					"Given query is not a TMQL query or cannot transform to TMQL.");
		}
		run(q);
		return q;
	}

	/**
	 * @TODO REMOVE HERE AND AT AS PREPARED QUERY
	 * 
	 *       Set the initial variables ( LINQ ) to the current runtime context
	 *       on top
	 * 
	 * @throws TMQLRuntimeException
	 *             if setting variables failed
	 */
	public void setLINQVariables() throws TMQLRuntimeException {
		if (!variables.isEmpty()) {
			/*
			 * set LINQ variables
			 */
			for (Entry<String, Object> entry : variables.entrySet()) {
				runtimeContext.peek()
						.setValue(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * Method check if given variable is a system or write-only variable. System
	 * variables are defined at the initial context and write-only variables
	 * start with the prefix <b>$_</b>.
	 * 
	 * @param name
	 *            the name of the variable to check
	 * @return <code>true</code> if variable is a system or write-only variable
	 */
	public boolean isSystemVariable(String name) {
		return getInitialContext().getPredefinedVariableSet().contains(name)
				|| name.startsWith("$_");
	}

	/**
	 * Method return the internal event manager of the runtime. The event
	 * manager is used to handle new events and notify the registered listener.
	 * 
	 * @return the eventManager the reference of the internal event manager
	 */
	public EventManager getEventManager() {
		return eventManager;
	}

	/**
	 * Method returns the internal reference of the {@link IDataBridge}
	 * instance.
	 * 
	 * @return the dataBridge the internal data-bridge reference
	 */
	public IDataBridge getDataBridge() {
		return dataBridge;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDataBridge(IDataBridge bridge) {
		this.dataBridge = bridge;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDataBridge(Class<? extends IDataBridge> clazz)
			throws TMQLRuntimeException {
		try {
			setDataBridge(clazz.newInstance());
		} catch (InstantiationException e) {
			throw new TMQLRuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new TMQLRuntimeException(e);
		}
	}

	/**
	 * Getter method of the internal TMQL runtime properties instance.
	 * 
	 * @return the internal instance reference
	 */
	public TMQLRuntimeProperties getProperties() {
		return properties;
	}

	/**
	 * {@inheritDoc}
	 */
	public ILanguageContext getLanguageContext() {
		return languageContext;
	}

	/**
	 * {@inheritDoc}
	 */
	public IValueStore getValueStore() {
		return store;
	}

	/**
	 * {@inheritDoc}
	 */
	public ExtensionPointAdapter getExtensionPointAdapter() {
		return adapter;
	}

	/**
	 * Getter method of the internal Environment definition
	 * 
	 * @return the internal reference
	 */
	public Environment getEnvironment() {
		/*
		 * check if environment already set
		 */
		if (environment == null) {
			try {
				if (environmentMap == null) {
					environmentMap = getTopicMapSystem()
							.createTopicMap("http://tmql4j.topicmapslab.de/"
									+ UUID.randomUUID());
				}
				this.environment = new Environment(this, environmentMap);
			} catch (Exception e) {
				throw new TMQLRuntimeException(
						"Environment cannot be initialized", e);
			}
		}
		return environment;
	}
	
	/**
	 * 
	 * @param environmentMap
	 *            the environmentMap to set
	 */
	public void setEnvironmentMap(TopicMap environmentMap) {
		/*
		 * remove environment map
		 */
		if (environment != null) {
			environment = null;
		}
		/*
		 * set the new topic map
		 */
		this.environmentMap = environmentMap;
	}

	/**
	 * @param actsTransitive
	 *            the actsTransitive to set
	 */
	public void setActsTransitive(boolean actsTransitive) {
		this.actsTransitive = actsTransitive;
	}

	/**
	 * @return the actsTransitive
	 */
	public boolean isActsTransitive() {
		return actsTransitive;
	}

	/**
	 * {@inheritDoc}
	 */
	public IParserTree parse(final String query) throws TMQLRuntimeException {
		new ParsingProcessingChain(this, new TMQLQuery(query));
		return getValueStore().getParserTree();
	}

}
