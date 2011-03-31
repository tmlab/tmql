/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.results.jtmqr;

import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.Result;
import de.topicmapslab.tmql4j.components.processor.results.model.ResultSet;
import de.topicmapslab.tmql4j.components.processor.results.model.ResultType;

/**
 * @author Sven Krosse
 * 
 */
public class JTMQRResult extends ResultSet<JTMQRValue> {

	/**
	 * constructor create an empty result set
	 * 
	 * @param topicMapSystem
	 *            the topic map system
	 * @param topicMap
	 *            the topic map
	 */
	public JTMQRResult(final TopicMapSystem topicMapSystem, final TopicMap topicMap) {
		super(topicMapSystem, topicMap);
	}

	/**
	 * {@inheritDoc}
	 */
	public IResult createResult() {
		return new JTMQRValue(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getResultType() {
		return ResultType.JTMQR.name();
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		if (isEmpty()) {
			return "";
		}
		return first().first().toString();
	}

}

class JTMQRValue extends Result {
	/**
	 * @param parent
	 */
	public JTMQRValue(ResultSet<?> parent) {
		super(parent);
	}
}