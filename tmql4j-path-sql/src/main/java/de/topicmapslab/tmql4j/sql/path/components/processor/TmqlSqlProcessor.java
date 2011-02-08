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

import org.tmapi.core.TopicMap;

import de.topicmapslab.majortom.database.jdbc.model.ISession;
import de.topicmapslab.majortom.database.store.JdbcTopicMapStore;
import de.topicmapslab.majortom.model.core.ITopicMap;
import de.topicmapslab.tmql4j.components.parser.IParserTree;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.results.IResultProcessor;
import de.topicmapslab.tmql4j.components.processor.results.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.ResultSet;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.components.processor.TmqlProcessor2007;
import de.topicmapslab.tmql4j.query.IQuery;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.SqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.results.SqlResultProcessor;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TranslatorRegistry;

/**
 * @author Sven Krosse
 * 
 */
public class TmqlSqlProcessor extends TmqlProcessor2007 {

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
	public IResultSet<?> query(IQuery query, OutputStream os) {
		IParserTree tree = parse(query);
		if (tree != null) {
			IContext context = new Context(this, query);
			ISqlDefinition definition = new SqlDefinition();
			definition = TranslatorRegistry.getTranslator(tree.root().getClass()).toSql(getRuntime(), context, tree.root(), definition);
			System.out.println(definition);
			return executeSql(query, definition);			 
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
			SqlResultProcessor processor = (SqlResultProcessor)getResultProcessor();
			processor.proceed(query, session, rs);
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
	protected IResultProcessor createResultProcessor() {
		return new SqlResultProcessor(getRuntime());
	}
	
}
