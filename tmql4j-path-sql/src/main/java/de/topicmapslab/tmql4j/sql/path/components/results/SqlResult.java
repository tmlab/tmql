/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.results;

import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;

/**
 * @author Sven Krosse
 * 
 */
public class SqlResult extends SimpleResult {

	/**
	 * constructor
	 * 
	 * @param parent
	 *            the parent result set
	 */
	public SqlResult(SqlResultSet parent) {
		super(parent);
	}

	/**
	 * Modify the value at the given position.
	 * 
	 * @param index
	 *            the index
	 * @param object
	 *            the new value
	 */
	public void set(int index, Object object) {
		getResults().set(index, object);
	}

}
