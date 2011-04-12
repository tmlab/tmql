/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor;

import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

import de.topicmapslab.tmql4j.components.lexer.ILexer;
import de.topicmapslab.tmql4j.components.parser.IParser;
import de.topicmapslab.tmql4j.components.parser.IParserTree;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.prepared.IPreparedStatement;
import de.topicmapslab.tmql4j.components.processor.results.TmqlResultProcessor;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultProcessor;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.model.ResultSet;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * @author Sven Krosse
 * 
 */
public abstract class TmqlProcessorImpl implements ITmqlProcessor {

	/**
	 * exception message
	 */
	private static final String ERROR_CANNOT_CREATE_NET_INSTANCE_OF_PREPARED_STATEMENT = "Cannot create net instance of prepared statement";
	private final ITMQLRuntime runtime;
	private IResultProcessor tmqlResultProcessor;

	/**
	 * constructor
	 * 
	 * @param runtime
	 *            the runtime
	 */
	public TmqlProcessorImpl(ITMQLRuntime runtime) {
		this.runtime = runtime;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IResultSet<?> query(IQuery query, OutputStream stream) {
		IParserTree tree = parse(query);
		if (tree != null) {
			IContext context = new Context(this, query, stream);

			QueryMatches results = tree.root().interpret(runtime, context);

			IResultProcessor resultProcessor = getResultProcessor();
			resultProcessor.proceed(context, results);

			return resultProcessor.getResultSet();
		}
		return ResultSet.emptyResultSet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IResultSet<?> query(IQuery query) {
		IParserTree tree = parse(query);
		if (tree != null) {
			IContext context = new Context(this, query);

			QueryMatches results = tree.root().interpret(runtime, context);

			IResultProcessor resultProcessor = getResultProcessor();
			resultProcessor.proceed(context, results);

			return resultProcessor.getResultSet();
		}
		return ResultSet.emptyResultSet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IResultSet<?> query(IPreparedStatement statement) {
		/*
		 * create context
		 */
		IContext context = new Context(this, statement);
		/*
		 * execute query
		 */
		QueryMatches results = statement.getParserTree().root().interpret(runtime, context);
		/*
		 * proceed results
		 */
		IResultProcessor resultProcessor = getResultProcessor();
		resultProcessor.proceed(context, results);
		/*
		 * set results to statement and return it
		 */
		IResultSet<?> resultSet = resultProcessor.getResultSet();
		statement.setResults(resultSet);
		return resultSet;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IParserTree parse(IQuery query) {
		ILexer lexer = getTmqlLexer(query);
		lexer.execute();
		if (!lexer.getTokens().isEmpty()) {
			IParser parser = getTmqlParser(lexer);
			parser.parse(runtime);
			return parser.getParserTree();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPreparedStatement prepared(IQuery query) {
		IParserTree tree = parse(query);
		if (tree != null) {
			try {
				return getPreparedStatementClass().getConstructor(ITMQLRuntime.class, IQuery.class, IParserTree.class).newInstance(runtime, query, tree);
			} catch (IllegalArgumentException e) {
				throw new TMQLRuntimeException(ERROR_CANNOT_CREATE_NET_INSTANCE_OF_PREPARED_STATEMENT, e);
			} catch (SecurityException e) {
				throw new TMQLRuntimeException(ERROR_CANNOT_CREATE_NET_INSTANCE_OF_PREPARED_STATEMENT, e);
			} catch (InstantiationException e) {
				throw new TMQLRuntimeException(ERROR_CANNOT_CREATE_NET_INSTANCE_OF_PREPARED_STATEMENT, e);
			} catch (IllegalAccessException e) {
				throw new TMQLRuntimeException(ERROR_CANNOT_CREATE_NET_INSTANCE_OF_PREPARED_STATEMENT, e);
			} catch (InvocationTargetException e) {
				throw new TMQLRuntimeException(ERROR_CANNOT_CREATE_NET_INSTANCE_OF_PREPARED_STATEMENT, e);
			} catch (NoSuchMethodException e) {
				throw new TMQLRuntimeException(ERROR_CANNOT_CREATE_NET_INSTANCE_OF_PREPARED_STATEMENT, e);
			}
		}
		return null;
	}

	/**
	 * Internal method to get the implementation class of the prepared statement
	 * 
	 * @return the class of prepared statement
	 * @since 3.1.0
	 */
	protected abstract Class<? extends IPreparedStatement> getPreparedStatementClass();

	/**
	 * @return the runtime
	 */
	protected ITMQLRuntime getRuntime() {
		return runtime;
	}

	/**
	 * Creates a new result processor
	 * 
	 * @return the new result processor
	 */
	protected IResultProcessor createResultProcessor() {
		return new TmqlResultProcessor(runtime);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IResultProcessor getResultProcessor() {
		if (tmqlResultProcessor == null) {
			tmqlResultProcessor = createResultProcessor();
		}
		return tmqlResultProcessor;
	}
}
