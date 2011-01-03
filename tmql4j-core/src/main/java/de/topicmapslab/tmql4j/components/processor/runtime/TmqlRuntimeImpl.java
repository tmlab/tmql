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

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.FactoryConfigurationException;
import org.tmapi.core.TMAPIException;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;

import de.topicmapslab.tmql4j.components.parser.IParserTree;
import de.topicmapslab.tmql4j.components.processor.ITmqlProcessor;
import de.topicmapslab.tmql4j.components.processor.prepared.IPreparedStatement;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.query.IQuery;
import de.topicmapslab.tmql4j.query.QueryFactory;
import de.topicmapslab.tmql4j.util.HashUtil;

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
	 * 
	 */
	private static final String GIVEN_QUERY_IS_NOT_A_TMQL_QUERY_OR_CANNOT_TRANSFORM_TO_TMQL = "Given query is not a TMQL query or cannot transform to TMQL.";

	/**
	 * the logger
	 */
	private static Logger logger = LoggerFactory.getLogger(TmqlRuntimeImpl.class);

	/**
	 * a set holding all forbidden expression types
	 */
	private Set<Class<? extends IExpression>> forbiddenExpressionTypes;

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
	public IParserTree parse(IQuery query) throws TMQLRuntimeException {
		return parse(query.getQueryString());
	}

	/**
	 * {@inheritDoc}
	 */
	public void run(IQuery query, Object... parameters) throws TMQLRuntimeException {
		/*
		 * is prepared statement
		 */
		if ( parameters.length > 0 ){
			if ( query instanceof IPreparedStatement ){
				IPreparedStatement stmt = (IPreparedStatement) query;
				for ( int i = 0 ; i < parameters.length ; i++ ){
					stmt.set(i, parameters[i]);
				}
				stmt.run();
				return;
			}
			/*
			 * no prepared statement
			 */
			throw new TMQLRuntimeException("Parameters only allowed for prepared statements");			
		}
		/*
		 * add restrictions
		 */
		addRestrictions(query);
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
	public IQuery run(TopicMap topicMap, String query, Object... parameters ) throws TMQLRuntimeException {		
		IQuery q = null;
		/*
		 * is prepared statement
		 */
		if ( parameters.length > 0){
			q = preparedStatement(query);
			q.setTopicMap(topicMap);
		}
		/*
		 * is simple query without wildcards
		 */
		else{
			q = QueryFactory.getFactory().getTmqlQuery(topicMap, query);
		}
		if (q == null) {
			throw new TMQLRuntimeException(GIVEN_QUERY_IS_NOT_A_TMQL_QUERY_OR_CANNOT_TRANSFORM_TO_TMQL);
		}
		run(q, parameters);
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
	public ITmqlProcessor getTmqlProcessor() {
		return processor;
	}

	/**
	 * Method to create a new TMQl processor
	 * 
	 * @return the processor
	 */
	protected abstract ITmqlProcessor createTmqlProcessor();

	/**
	 * {@inheritDoc}
	 */
	public IPreparedStatement preparedStatement(IQuery query) throws TMQLRuntimeException {
		/*
		 * add restrictions
		 */
		addRestrictions(query);
		/*
		 * before-execution call to query
		 */
		query.beforeQuery(this);
		/*
		 * create prepared statement
		 */
		return getTmqlProcessor().prepared(query);
	}

	/**
	 * {@inheritDoc}
	 */
	public IPreparedStatement preparedStatement(String query) throws TMQLRuntimeException {
		IQuery q = QueryFactory.getFactory().getTmqlQuery(null, query);
		if (q == null) {
			throw new TMQLRuntimeException(GIVEN_QUERY_IS_NOT_A_TMQL_QUERY_OR_CANNOT_TRANSFORM_TO_TMQL);
		}
		return preparedStatement(q);
	}

	/**
	 * {@inheritDoc}
	 */
	public void forbidExpression(Class<? extends IExpression> forbiddenExpressionType) {
		if (forbiddenExpressionTypes == null) {
			forbiddenExpressionTypes = HashUtil.getHashSet();
		}
		forbiddenExpressionTypes.add(forbiddenExpressionType);
	}

	/**
	 * {@inheritDoc}
	 */
	public void forbidModificationQueries() {
		for (Class<? extends IExpression> forbiddenExpressionType : getModificationExpressionTypes()) {
			forbidExpression(forbiddenExpressionType);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void allowExpression(Class<? extends IExpression> allowedExpressionType) {
		if (forbiddenExpressionTypes != null) {
			forbiddenExpressionTypes.remove(allowedExpressionType);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void allowModificationQueries() {
		for (Class<? extends IExpression> allowedExpressionType : getModificationExpressionTypes()) {
			allowExpression(allowedExpressionType);
		}
	}

	/**
	 * Utility method to load all expression of a specific language which occurs
	 * a modification of the topic map
	 * 
	 * @return a set of classes
	 */
	@SuppressWarnings("unchecked")
	private Set<Class<? extends IExpression>> getModificationExpressionTypes() {
		Set<Class<? extends IExpression>> classes = HashUtil.getHashSet();
		for (String name : getModificationExpressionTypeNames()) {
			try {
				Class<? extends IExpression> clazz = (Class<? extends IExpression>) Class.forName(name);
				classes.add(clazz);
			} catch (Exception e) {
				logger.warn("Expression type '" + name + "'not present in classpath");
			}
		}
		return classes;
	}

	/**
	 * Returns an array of the full qualified expression types of the query
	 * language which occurs modification expression.
	 * 
	 * @return a string array
	 */
	protected abstract String[] getModificationExpressionTypeNames();

	/**
	 * Method adds the restrictions of this runtime to the query instance
	 * 
	 * @param query
	 *            the query instance
	 */
	protected void addRestrictions(IQuery query) {
		if (forbiddenExpressionTypes != null) {
			for (Class<? extends IExpression> forbiddenExpressionType : forbiddenExpressionTypes) {
				query.forbidExpression(forbiddenExpressionType);
			}
		}
	}

}
