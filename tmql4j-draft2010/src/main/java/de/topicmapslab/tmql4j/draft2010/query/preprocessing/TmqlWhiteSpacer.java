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
package de.topicmapslab.tmql4j.draft2010.query.preprocessing;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * Base implementation of a {@link IWhiteSpacer}. The white-spacer removes at
 * first all unnecessary white-spaces. At the second step it tries to detect
 * missing white-spaces to split each language-specific token by a single space.
 * The cleaned query can be handle in a easier way than the origin one.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TmqlWhiteSpacer {

	/**
	 * {@inheritDoc}
	 */
	public static String execute(ITMQLRuntime runtime, String query) throws TMQLRuntimeException {
		/*
		 * call query transformer
		 */
		final String string = TmqlQueryTransformer.transform(runtime, query.toString());
		return PathWhitespacer.whitespace(string);
	}

}
