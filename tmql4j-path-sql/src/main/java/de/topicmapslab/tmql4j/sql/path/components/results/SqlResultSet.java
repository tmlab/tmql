/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.results;

import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

/**
 * @author Sven Krosse
 * 
 */
public class SqlResultSet extends SimpleResultSet {

	/**
	 * constructor
	 * @param topicMapSystem the topic map system
	 * @param topicMap the topic map
	 */
	public SqlResultSet(TopicMapSystem topicMapSystem, TopicMap topicMap) {
		super(topicMapSystem, topicMap);
	}
	

	/**
	 * Modify the value at the given position.
	 * 
	 * @param rowIndex
	 *            the rowIndex
	 * @param colIndex
	 *            the colIndex
	 * @param object
	 *            the new value
	 */
	public void set(int rowIndex, int colIndex, Object object) {
		((SqlResult) getResults().get(rowIndex)).set(colIndex, object);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IResult createResult() {
		return new SqlResult(this);
	}

}
