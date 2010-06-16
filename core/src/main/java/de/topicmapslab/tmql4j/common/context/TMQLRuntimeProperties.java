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
package de.topicmapslab.tmql4j.common.context;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.common.core.exception.TMQLInitializationException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.core.tuplesequence.UniqueTupleSequence;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.common.model.tuplesequence.ITupleSequence;
import de.topicmapslab.tmql4j.lexer.core.TMQLLexer;
import de.topicmapslab.tmql4j.lexer.model.ILexer;
import de.topicmapslab.tmql4j.parser.core.TMQLParser;
import de.topicmapslab.tmql4j.parser.model.IParser;
import de.topicmapslab.tmql4j.preprocessing.core.moduls.TMQLCanonizer;
import de.topicmapslab.tmql4j.preprocessing.core.moduls.TMQLScreener;
import de.topicmapslab.tmql4j.preprocessing.core.moduls.TMQLWhiteSpacer;
import de.topicmapslab.tmql4j.preprocessing.model.ICanonizer;
import de.topicmapslab.tmql4j.preprocessing.model.IScreener;
import de.topicmapslab.tmql4j.preprocessing.model.IWhiteSpacer;
import de.topicmapslab.tmql4j.resultprocessing.base.ResultProcessorImpl;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleTupleResult;
import de.topicmapslab.tmql4j.resultprocessing.model.IResult;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultProcessor;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;

/**
 * Class representing the runtime properties of the TMQL engine. An instance of
 * the class encapsulate important properties of a {@link TMQLRuntime}.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
@SuppressWarnings("serial")
public class TMQLRuntimeProperties extends Properties {

	/**
	 * a string constant identifies the class name of the {@link ITupleSequence}
	 * implementation
	 */
	public static final String TUPLE_SEQUENCE_CLASS = "tuple-sequence-class";

	/**
	 * a string constant identifies the class name of the {@link ICanonizer}
	 * implementation
	 */
	public static final String CANONIZER_IMPLEMENTATION_CLASS = "canonizer-implementation-class";
	/**
	 * a string constant identifies the class name of the {@link IScreener}
	 * implementation
	 */
	public static final String SCREENER_IMPLEMENTATION_CLASS = "screener-implementation-class";
	/**
	 * a string constant identifies the class name of the {@link IWhiteSpacer}
	 * implementation
	 */
	public static final String WHITESPACER_IMPLEMENTATION_CLASS = "whitespacer-implementation-class";
	/**
	 * a string constant identifies the class name of the {@link ILexer}
	 * implementation
	 */
	public static final String LEXER_IMPLEMENTATION_CLASS = "lexer-implementation-class";
	/**
	 * a string constant identifies the class name of the {@link IParser}
	 * implementation
	 */
	public static final String PARSER_IMPLEMENTATION_CLASS = "parser-implementation-class";
	/**
	 * a string constant identifies the class name of the
	 * {@link IResultProcessor} implementation
	 */
	public static final String RESULTPROCESSOR_IMPLEMENTATION_CLASS = "result-processor-implementation-class";
	/**
	 * a string constant identifies the class name of the {@link IResultSet}
	 * implementation
	 */
	public static final String RESULT_SET_IMPLEMENTATION_CLASS = "result-set-implementation-class";
	/**
	 * a string constant identifies the class name of {@link IResult}
	 * implementation
	 */
	public static final String RESULT_TUPLE_IMPLEMENTATION_CLASS = "result-tuple-implementation-class";
	/**
	 * a string constant identifies the property of type transitivity
	 */
	public static final String TMDM_TYPE_TRANSITIVITY = "tmdm-type-transitivity";
	/**
	 * a string constant identifies the property to activate the query
	 * optimization
	 */
	public static final String ACTIVATE_TMQL4J_OPTIMIZATION = "activate-tmql4j-optimization";

	/**
	 * a string constant identifies the property to enable the language
	 * extension TMQL-UL
	 */
	public static final String ENABLE_TMQL_LANGUAGE_EXTENSION_TMQL_UL = "enable-tmql-language-extension-tmql-ul";

	/**
	 * internal system flag representing the creation mode of meta model topics
	 * in the queryied topic map
	 */
	public static final String MATERIALIZE_META_MODEL = "materialize-meta-model";

	/**
	 * internal system flag indicates the internal proceeding flag to execute
	 * expressions by multiple threads
	 */
	public static final String USE_MULTIPLE_THREADS = "use-multiple-threads";

	/**
	 * internal store of all known properties
	 */
	private static final List<String> knownProperties = new LinkedList<String>();
	
	
	private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

	/**
	 * insert known properties into internal store
	 */
	static {
		knownProperties.add(TUPLE_SEQUENCE_CLASS);
		knownProperties.add(CANONIZER_IMPLEMENTATION_CLASS);
		knownProperties.add(SCREENER_IMPLEMENTATION_CLASS);
		knownProperties.add(WHITESPACER_IMPLEMENTATION_CLASS);
		knownProperties.add(LEXER_IMPLEMENTATION_CLASS);
		knownProperties.add(PARSER_IMPLEMENTATION_CLASS);
		knownProperties.add(RESULTPROCESSOR_IMPLEMENTATION_CLASS);
		knownProperties.add(RESULT_SET_IMPLEMENTATION_CLASS);
		knownProperties.add(RESULT_TUPLE_IMPLEMENTATION_CLASS);
		knownProperties.add(TMDM_TYPE_TRANSITIVITY);
		knownProperties.add(ACTIVATE_TMQL4J_OPTIMIZATION);
		knownProperties.add(ENABLE_TMQL_LANGUAGE_EXTENSION_TMQL_UL);
		knownProperties.add(MATERIALIZE_META_MODEL);
		knownProperties.add(USE_MULTIPLE_THREADS);
	}

	/**
	 * the TMQL runtime instance
	 */
	private final ITMQLRuntime runtime;

	/**
	 * constructor
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @throws TMQLRuntimeException
	 *             thrown if the runtime properties can not be initialized.
	 */
	public TMQLRuntimeProperties(ITMQLRuntime runtime)
			throws TMQLRuntimeException {

		this.runtime = runtime;
		/*
		 * try to load engine properties from file
		 */
		try {
			load(getClass().getResourceAsStream("tmql-engine.properties"));
		} catch (Exception ex) {
			throw new TMQLInitializationException(
					"Cannot load tmql-engine.properties");
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Object put(Object key, Object value) {
		/*
		 * check if property value is valid for given key
		 */
		if (!isAllowedPropertyValue(key, value)) {
			return null;
		}
		return super.put(key, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void putAll(Map<? extends Object, ? extends Object> t) {
		for (Map.Entry<? extends Object, ? extends Object> entry : t.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Method checks if the property key is supported by the TMQL runtime of
	 * TMQL4J.
	 * 
	 * @param key
	 *            the property key
	 * @return <code>true</code> if property is supported by the TMQL runtime,
	 *         <code>false</code> otherwise
	 */
	private boolean isKnownProperty(final Object key) {
		return knownProperties.contains(key);
	}

	/**
	 * Method checks if the property value is supported by the property key in
	 * context of the TMQL runtime of TMQL4J.
	 * 
	 * @param key
	 *            the property key
	 * @param value
	 *            the property value
	 * @return <code>true</code> if value is supported by the key,
	 *         <code>false</code> if the key is unknown or the value is
	 *         unsupported
	 */
	private boolean isAllowedPropertyValue(final Object key, final Object value) {
		if (!isKnownProperty(key)) {
			return false;
		}
		return true;
	}

	/**
	 * Method creates a new instance of a tuple sequence dependent form the
	 * property {@link TMQLRuntimeProperties#RESULT_SET_IMPLEMENTATION_CLASS}.
	 * If tuple sequence of the defined type cannot instantiate a
	 * {@link UniqueTupleSequence} will be creates.
	 * 
	 * @param <T>
	 *            the type of contained items
	 * @return a new instance of a tuple sequence but never <code>null</code>
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> ITupleSequence<T> newSequence() {
		try {
			Class<? extends ITupleSequence<?>> clazz = (Class<? extends ITupleSequence<?>>) Class
					.forName(get(TUPLE_SEQUENCE_CLASS).toString());
			return (ITupleSequence<T>) clazz.getConstructor().newInstance();
		} catch (Exception e) {
			logger.warn("Cannot initialize new sequence!\r\n reason: "
					+ e.getLocalizedMessage());
		}
		return new UniqueTupleSequence<T>();
	}

	/**
	 * Method creates a new instance of a screener dependent form the property
	 * {@link TMQLRuntimeProperties#SCREENER_IMPLEMENTATION_CLASS}. If a
	 * screener of the defined type cannot instantiate a {@link TMQLScreener}
	 * will be creates.
	 * 
	 * @param query
	 *            the query used as parameter for the {@link IScreener}
	 *            constructor
	 * @return a new instance of {@link IScreener} but never <code>null</code>
	 */
	@SuppressWarnings("unchecked")
	public IScreener getScreenerImplementation(IQuery query) {
		try {
			Class<? extends IScreener> clazz = (Class<? extends IScreener>) Class
					.forName(get(SCREENER_IMPLEMENTATION_CLASS).toString());
			return (IScreener) clazz.getConstructor(IQuery.class).newInstance(
					query);
		} catch (Exception e) {
			logger.warn(
					"Cannot initialize new sequence!\r\n reason: "
							+ e.getLocalizedMessage());
		}
		return new TMQLScreener(query);
	}

	/**
	 * Method creates a new instance of a canonizer dependent form the property
	 * {@link TMQLRuntimeProperties#CANONIZER_IMPLEMENTATION_CLASS}. If a
	 * canonizer of the defined type cannot instantiate a {@link TMQLCanonizer}
	 * will be creates.
	 * 
	 * @param query
	 *            the query used as parameter for the {@link ICanonizer}
	 *            constructor
	 * @return a new instance of {@link ICanonizer} but never <code>null</code>
	 */
	@SuppressWarnings("unchecked")
	public ICanonizer getCanonizerImplementation(IQuery query) {
		try {
			Class<? extends ICanonizer> clazz = (Class<? extends ICanonizer>) Class
					.forName(get(CANONIZER_IMPLEMENTATION_CLASS).toString());
			return (ICanonizer) clazz.getConstructor(IQuery.class).newInstance(
					query);
		} catch (Exception e) {
			logger.warn(
					"Cannot initialize new sequence!\r\n reason: "
							+ e.getLocalizedMessage());
		}
		return new TMQLCanonizer(query);
	}

	/**
	 * Method creates a new instance of a whitespacer dependent form the
	 * property {@link TMQLRuntimeProperties#WHITESPACER_IMPLEMENTATION_CLASS}.
	 * If a whitespacer of the defined type cannot instantiate a
	 * {@link TMQLWhiteSpacer} will be creates.
	 * 
	 * @param query
	 *            the query used as parameter for the {@link IWhiteSpacer}
	 *            constructor
	 * @return a new instance of {@link IWhiteSpacer} but never
	 *         <code>null</code>
	 */
	@SuppressWarnings("unchecked")
	public IWhiteSpacer getWhiteSpacerImplementation(IQuery query) {
		try {
			Class<? extends IWhiteSpacer> clazz = (Class<? extends IWhiteSpacer>) Class
					.forName(get(WHITESPACER_IMPLEMENTATION_CLASS).toString());
			return (IWhiteSpacer) clazz.getConstructor(ITMQLRuntime.class,
					IQuery.class).newInstance(runtime, query);
		} catch (Exception e) {
			logger.warn(
					"Cannot initialize new sequence!\r\n reason: "
							+ e.getLocalizedMessage());
		}
		return new TMQLWhiteSpacer(runtime, query);
	}

	/**
	 * Method creates a new instance of a lexer dependent form the property
	 * {@link TMQLRuntimeProperties#LEXER_IMPLEMENTATION_CLASS}. If a lexer of
	 * the defined type cannot instantiate a {@link TMQLLexer} will be creates.
	 * 
	 * @param query
	 *            the query used as parameter for the {@link ILexer} constructor
	 * @return a new instance of {@link ILexer} but never <code>null</code>
	 */
	@SuppressWarnings("unchecked")
	public ILexer getLexerImplementation(IQuery query) {
		try {
			Class<? extends ILexer> clazz = (Class<? extends ILexer>) Class
					.forName(get(LEXER_IMPLEMENTATION_CLASS).toString());
			return (ILexer) clazz.getConstructor(ITMQLRuntime.class,
					IQuery.class).newInstance(runtime, query);
		} catch (Exception e) {
			logger.warn(
					"Cannot initialize new sequence!\r\n reason: "
							+ e.getLocalizedMessage());
		}
		return new TMQLLexer(runtime, query);
	}

	/**
	 * Method creates a new instance of a parser dependent form the property
	 * {@link TMQLRuntimeProperties#PARSER_IMPLEMENTATION_CLASS}. If a parser of
	 * the defined type cannot instantiate a {@link TMQLParser} will be creates.
	 * 
	 * @param lexer
	 *            the lexer used as parameter for the {@link IParser}
	 *            constructor
	 * @return a new instance of {@link IParser} but never <code>null</code>
	 */
	@SuppressWarnings("unchecked")
	public IParser getParserImplementation(ILexer lexer) {
		try {
			Class<? extends IParser> clazz = (Class<? extends IParser>) Class
					.forName(get(PARSER_IMPLEMENTATION_CLASS).toString());
			return (IParser) clazz.getConstructor(ILexer.class).newInstance(
					lexer);
		} catch (Exception e) {
			logger.warn(
					e.getLocalizedMessage());
		}
		return new TMQLParser(lexer);
	}

	/**
	 * Method creates a new instance of a result processor dependent form the
	 * property
	 * {@link TMQLRuntimeProperties#RESULTPROCESSOR_IMPLEMENTATION_CLASS}. If a
	 * result processor of the defined type cannot instantiate a
	 * {@link ResultProcessorImpl} will be creates.
	 * 
	 * @return a new instance of {@link IResultProcessor} but never
	 *         <code>null</code>
	 */
	@SuppressWarnings("unchecked")
	public IResultProcessor getResultProcessorImplementation() {
		final Class<? extends IResultSet> resultSetClass = getResultSetClass();
		try {
			Class<? extends IResultProcessor> clazz = (Class<? extends IResultProcessor>) Class
					.forName(get(RESULTPROCESSOR_IMPLEMENTATION_CLASS)
							.toString());
			return (IResultProcessor) clazz.getConstructor(ITMQLRuntime.class,
					Class.class).newInstance(runtime, resultSetClass);
		} catch (Exception e) {
			logger.warn(
					"Cannot initialize new sequence!\r\n reason: "
							+ e.getLocalizedMessage());
		}
		return new ResultProcessorImpl(runtime, SimpleResultSet.class);
	}

	/**
	 * Method return the internal value of the property
	 * {@link TMQLRuntimeProperties#RESULT_SET_IMPLEMENTATION_CLASS} represents
	 * the class of the result set. If the initialization of the defined class
	 * failed the {@link SimpleResultSet} will be returned.
	 * 
	 * @return the class of result set
	 */
	@SuppressWarnings("unchecked")
	public Class<? extends IResultSet<?>> getResultSetClass() {
		try {
			return (Class<? extends IResultSet<?>>) (Class.forName(get(
					RESULT_SET_IMPLEMENTATION_CLASS).toString()));
		} catch (ClassNotFoundException e) {
			logger.warn(
					"Cannot found class instance for result-set!\r\n reason: "
							+ e.getLocalizedMessage());
		}
		return SimpleResultSet.class;
	}

	/**
	 * Method return the internal value of the property
	 * {@link TMQLRuntimeProperties#RESULT_TUPLE_IMPLEMENTATION_CLASS}
	 * represents the class of the result.If the initialization of the defined
	 * class failed the {@link SimpleTupleResult} will be returned.
	 * 
	 * @return the class of result
	 */
	@SuppressWarnings("unchecked")
	public Class<? extends IResult> getResultClass() {
		try {
			return (Class<? extends IResult>) (Class.forName(get(
					RESULT_TUPLE_IMPLEMENTATION_CLASS).toString()));
		} catch (ClassNotFoundException e) {
			logger.warn(
					"Cannot found class instance for result!\r\n reason: "
							+ e.getLocalizedMessage());
		}
		return SimpleTupleResult.class;
	}

	/**
	 * Method check if the property of type transitivity is set. The property is
	 * identify by {@link TMQLRuntimeProperties#TMDM_TYPE_TRANSITIVITY}
	 * 
	 * @return <code>true</code> if types are transitive, <code>false</code>
	 *         otherwise
	 */
	public boolean isTmdmTypeTransitiv() {
		return Boolean.parseBoolean(get(TMDM_TYPE_TRANSITIVITY).toString()
				.trim());
	}

	/**
	 * Method returns the value of the internal flag
	 * {@link TMQLRuntimeProperties#ACTIVATE_TMQL4J_OPTIMIZATION}.
	 * 
	 * @return <code>true</code> if optimization is activated,
	 *         <code>false</code> otherwise.
	 */
	public boolean useOptimization() {
		Object obj = get(ACTIVATE_TMQL4J_OPTIMIZATION);
		if (obj != null) {
			return Boolean.parseBoolean(obj.toString());
		}
		return false;
	}

	/**
	 * Method return the expected result type of the query execution. Please
	 * note that the result can be changed automatically during the
	 * interpretation process if result has to be XML or CTM.
	 * 
	 * @return the string representation of the result type
	 * @throws TMQLRuntimeException
	 *             thrown if result set cannot be instantiate
	 */
	public String getResultType() throws TMQLRuntimeException {
		try {
			/*
			 * create new instance if result set
			 */
			IResultSet<?> set = getResultSetClass().newInstance();
			return set.getResultType();
		} catch (InstantiationException e) {
			throw new TMQLRuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new TMQLRuntimeException(e);
		}
	}

	/**
	 * Method check if the modification language extension is enabled.
	 * Representing property is
	 * {@link TMQLRuntimeProperties#ENABLE_TMQL_LANGUAGE_EXTENSION_TMQL_UL}.
	 * 
	 * @return <code>true</code> if language extension is enabled,
	 *         <code>false</code> otherwise.
	 */
	public boolean isLanguageExtensionTmqlUlEnabled() {
		Object obj = get(ENABLE_TMQL_LANGUAGE_EXTENSION_TMQL_UL);
		if (obj != null) {
			return Boolean.parseBoolean(obj.toString());
		}
		return false;
	}

	/**
	 * Method changes the internal flag which represents the support of the
	 * language extension TMQL-UL.
	 * 
	 * @param enable
	 *            <code>true</code> if the language extension shall be
	 *            supported, <code>false</code> otherwise
	 */
	public void enableLanguageExtensionTmqlUl(boolean enable) {
		setProperty(ENABLE_TMQL_LANGUAGE_EXTENSION_TMQL_UL, Boolean
				.toString(enable));
	}

	/**
	 * Method changes the internal flag which represents the creation mode of
	 * meta model topics in the queried topic map
	 * 
	 * @param enable
	 *            <code>true</code> if the meta model topics should be insert
	 *            into the queried map, <code>false</code> otherwise
	 */
	public void enableMaterializeMetaModel(boolean enable) {
		setProperty(MATERIALIZE_META_MODEL, Boolean.toString(enable));
	}

	/**
	 * Method check if the internal flag which represents the creation mode of
	 * meta model topics in the queried topic map is set. Representing property
	 * is {@link TMQLRuntimeProperties#MATERIALIZE_META_MODEL}.
	 * 
	 * @return <code>true</code> if the meta model topics should be insert into
	 *         the queried map, <code>false</code> otherwise.
	 */
	public boolean isMaterializeMetaModel() {
		Object obj = getProperty(MATERIALIZE_META_MODEL);
		if (obj != null) {
			return Boolean.parseBoolean(obj.toString());
		}
		return false;
	}

	/**
	 * Method changes the internal flag indicates if the processor can use
	 * multiple thread to run faster.
	 * 
	 * @param enable
	 *            <code>true</code> if the runtime can use multiple threads to
	 *            run expressions faster, <code>false</code> otherwise
	 */
	public void useMultipleThreads(boolean enable) {
		setProperty(USE_MULTIPLE_THREADS, Boolean.toString(enable));
	}

	/**
	 * Method check if the internal flag which indicates if the processor can
	 * use multiple thread to run faster is set. Representing property is
	 * {@link TMQLRuntimeProperties#USE_MULTIPLE_THREADS}.
	 * 
	 * @return <code>true</code> if the runtime can use multiple threads to run
	 *         expressions faster, <code>false</code> otherwise.
	 */
	public boolean useMultipleThreads() {
		Object obj = getProperty(USE_MULTIPLE_THREADS);
		if (obj != null) {
			return Boolean.parseBoolean(obj.toString());
		}
		return false;
	}

}
