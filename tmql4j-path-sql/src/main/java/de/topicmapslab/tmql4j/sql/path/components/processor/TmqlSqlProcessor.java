/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor;

import java.io.OutputStream;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.TopicMap;

import de.topicmapslab.majortom.database.jdbc.model.ISession;
import de.topicmapslab.majortom.database.store.JdbcTopicMapStore;
import de.topicmapslab.majortom.model.core.ITopicMap;
import de.topicmapslab.tmql4j.components.parser.IParserTree;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultProcessor;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.model.ResultSet;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.components.processor.TmqlProcessor2007;
import de.topicmapslab.tmql4j.query.IQuery;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.SqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.TranslatorRegistry;
import de.topicmapslab.tmql4j.sql.path.components.results.SqlResultProcessor;

/**
 * @author Sven Krosse
 * 
 */
public class TmqlSqlProcessor extends TmqlProcessor2007 {
	/**
	 * the Logger
	 */
	private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

	/**
	 * constructor
	 * 
	 * @param runtime
	 *            the runtime
	 */
	public TmqlSqlProcessor(ITMQLRuntime runtime) {
		super(runtime);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IResultSet<?> query(IQuery query, OutputStream os) {
		IParserTree tree = parse(query);
		if (tree != null) {
			IContext context = new Context(this, query);
			try {
				ISqlDefinition definition = new SqlDefinition();
				definition = TranslatorRegistry.getTranslator(tree.root().getClass()).toSql(getRuntime(), context, tree.root(), definition);
				return executeSql(query, definition);
			} catch (TMQLRuntimeException e) {
				logger.warn("Cannot translate query to SQL, at least one expression not supported by translator!", e);
				/*
				 * try normal runtime over TMAPI
				 */
				QueryMatches results = tree.root().interpret(getRuntime(), context);
				IResultProcessor resultProcessor = getResultProcessor();
				resultProcessor.proceed(context, results);
				return resultProcessor.getResultSet();
			}
		}
		return ResultSet.emptyResultSet();
	}

	/**
	 * Sent the transformed query to topic map database and return SQL result
	 * 
	 * @param query
	 *            the TMQL query
	 * @param definition
	 *            the SQL query definition
	 * @return the JDBC result set
	 */
	private IResultSet<?> executeSql(IQuery query, ISqlDefinition definition) {
		TopicMap topicMap = query.getTopicMap();
		JdbcTopicMapStore store = ((JdbcTopicMapStore) ((ITopicMap) topicMap).getStore());
		ISession session = store.openSession();
		try {
			java.sql.ResultSet rs = session.getConnection().createStatement().executeQuery(definition.toString());
			SqlResultProcessor processor = (SqlResultProcessor) getResultProcessor();
			processor.proceed(definition, query, session, rs);
			return processor.getResultSet();
		} catch (SQLException e) {
			throw new TMQLRuntimeException("Database connection is broken!", e);
		} finally {
			try {
				session.close();
			} catch (SQLException e) {
				throw new TMQLRuntimeException("Database connection is broken!", e);
			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IResultProcessor createResultProcessor() {
		return new SqlResultProcessor(getRuntime());
	}

}
