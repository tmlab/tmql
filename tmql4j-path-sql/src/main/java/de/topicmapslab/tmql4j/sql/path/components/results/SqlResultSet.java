/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.results;

import de.topicmapslab.tmql4j.components.processor.results.IResult;
import de.topicmapslab.tmql4j.components.results.SimpleResultSet;

/**
 * @author Sven Krosse
 * 
 */
public class SqlResultSet extends SimpleResultSet {

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
