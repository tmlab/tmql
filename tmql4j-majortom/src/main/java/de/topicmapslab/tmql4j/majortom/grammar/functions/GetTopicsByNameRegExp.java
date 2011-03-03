/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.majortom.grammar.functions;

import de.topicmapslab.majortom.model.core.IName;

/**
 * @author Sven Krosse
 * 
 */
public class GetTopicsByNameRegExp extends GetTopicsByCharacteristicRegExp {

	public static final String GetTopicsByNameRegExp = "fn:get-topics-by-name-regular-expression";

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return GetTopicsByNameRegExp;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?> getFilterType() {
		return IName.class;
	}

}
