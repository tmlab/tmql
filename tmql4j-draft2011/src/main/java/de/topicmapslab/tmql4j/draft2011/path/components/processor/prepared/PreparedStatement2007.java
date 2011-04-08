/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2011.path.components.processor.prepared;

import de.topicmapslab.tmql4j.components.parser.IParserTree;
import de.topicmapslab.tmql4j.components.processor.prepared.PreparedStatement;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * Prepared Statement of the 2007 draft
 * 
 * @author Sven Krosse
 * 
 * @since 3.1.0
 */
public class PreparedStatement2007 extends PreparedStatement {

	/**
	 * constructor
	 * 
	 * @param runtime
	 *            the runtime
	 * @param query
	 *            the query
	 * @param tree
	 *            the tree
	 */
	public PreparedStatement2007(ITMQLRuntime runtime, IQuery query, IParserTree tree) {
		super(runtime, query, tree);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String doGetNonParameterizedQueryString() {
		return PreparedUtils.toNonParameterized(this);
	}
}
