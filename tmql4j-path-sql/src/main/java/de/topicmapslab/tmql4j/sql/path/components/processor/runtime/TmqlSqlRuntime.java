/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor.runtime;

import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.tmql4j.components.processor.ITmqlProcessor;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.components.processor.runtime.TmqlRuntime2007;
import de.topicmapslab.tmql4j.sql.path.components.processor.TmqlSqlProcessor;

/**
 * @author Sven Krosse
 *
 */
public class TmqlSqlRuntime extends TmqlRuntime2007 {

	public static final String TMQL_SQL = "tmql-sql-path";
	
	/**
	 * constructor
	 */
	public TmqlSqlRuntime() {
	}

	/**
	 * constructor
	 * @param topicMapSystem the topic map system
	 * @throws TMQLRuntimeException
	 */
	public TmqlSqlRuntime(TopicMapSystem topicMapSystem) throws TMQLRuntimeException {
		super(topicMapSystem);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ITmqlProcessor getTmqlProcessor() {
		return new TmqlSqlProcessor(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getLanguageName() {
		return TMQL_SQL;
	}

}
