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
package de.topicmapslab.tmql4j.preprocessing.model;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.model.query.IQuery;

/**
 * Interface definition of a white-spacer.The white-spacer removes at first all
 * unnecessary white-spaces. At the second step it tries to detect missing
 * white-spaces to split each language-specific token by a single space. The
 * cleaned query can be handle in a easier way than the origin one.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IWhiteSpacer {

	/**
	 * Start the white-spacer. The white-spacer removes at first all unnecessary
	 * white-spaces. At the second step it tries to detect missing white-spaces
	 * to split each language-specific token by a single space.
	 * 
	 * @throws TMQLRuntimeException
	 *             thrown if the execution of the white-spacer fails
	 */
	void execute() throws TMQLRuntimeException;

	/**
	 * Method return the new query instance.
	 * 
	 * @return the query instance as result of white-spacing
	 */
	IQuery getTransformedQuery();

}
