/**
 * TMQL4J Plugin for Ontopia
 * 
 * Copyright: Copyright 2009 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * Author: Sven Krosse
 * 
 */
package net.ontopia.topicmaps.query.tmql.impl.tmql4jextension;

import java.util.LinkedList;
import java.util.List;

import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.ResultSet;
import de.topicmapslab.tmql4j.components.processor.results.model.ResultType;

public class WrappedOntopiaResultSet extends ResultSet<WrappedOntopiaResult> {

	/**
	 * constructor create an empty result set
	 * 
	 * @param topicMapSystem
	 *            the topic map system
	 * @param topicMap
	 *            the topic map
	 */
	public WrappedOntopiaResultSet(final TopicMapSystem topicMapSystem, final TopicMap topicMap) {
		super(topicMapSystem, topicMap);
	}

	/**
	 * Returns all values
	 * 
	 * @return all values
	 */
	public WrappedOntopiaResult[] getValues() {
		return getResults().toArray(new WrappedOntopiaResult[0]);
	}

	/**
	 * Returns the values at the specified positions
	 * 
	 * @param indizes
	 *            the indexes
	 * @return the values
	 */
	public WrappedOntopiaResult[] getValues(Integer... indizes) {
		List<Object> values = new LinkedList<Object>();
		for (Integer index : indizes) {
			if (index < size()) {
				values.add(getResults().get(index));
			}
		}
		return values.toArray(new WrappedOntopiaResult[0]);
	}

	/**
	 * Returns the value at the specific index
	 * 
	 * @param index
	 *            the index
	 * @return the value or <code>null</code>
	 */
	public WrappedOntopiaResult getValue(Integer index) {
		if (index < size() && index != -1) {
			return (WrappedOntopiaResult) getResults().get(index);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getResultType() {
		return ResultType.TMAPI.name();
	}

	/**
	 * {@inheritDoc}
	 */
	public void unify() {
		// NOTHING TO DO HERE
	}

	/**
	 * {@inheritDoc}
	 */
	public IResult createResult() {
		return new WrappedOntopiaResult(this);
	}

}
