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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.topicmapslab.tmql4j.components.processor.results.model.Result;
import de.topicmapslab.tmql4j.components.processor.results.model.ResultSet;

public class WrappedOntopiaResult extends Result {

	private String topicMapId;

	/**
	 * constructor
	 * 
	 * @param parent
	 *            the parent
	 */
	public WrappedOntopiaResult(ResultSet<?> parent) {
		super(parent);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public void add(Object... values) {
		for (Object o : values) {
			add(o);
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		Iterator<Object> iterator = iterator();
		while (iterator.hasNext()) {
			builder.append(iterator.next().toString() + (iterator.hasNext() ? ", " : ""));
		}
		builder.append("]");
		return builder.toString();
	}

	/**
	 * Returns all values
	 * 
	 * @return all values
	 */
	public Object[] getValues() {
		return getResults().toArray();
	}

	/**
	 * Returns the values at the specified positions
	 * 
	 * @param indizes
	 *            the indexes
	 * @return the values
	 */
	public Object[] getValues(Integer... indizes) {
		List<Object> values = new LinkedList<Object>();
		for (Integer index : indizes) {
			if (index < size()) {
				values.add(getResults().get(index));
			}
		}
		return values.toArray();
	}

	/**
	 * Returns the value at the specific index
	 * 
	 * @param index
	 *            the index
	 * @return the value or <code>null</code>
	 */
	public Object getValue(Integer index) {
		if (index < size()) {
			return getResults().get(index);
		}
		return null;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void add(Collection<Object> values) {
		add(values.toArray());
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> T get(int index) {
		if (getResults().size() <= index) {
			throw new IndexOutOfBoundsException("Result does not contains an element at position '" + index + "'.");
		}
		return (T) getResults().get(index);
	}

	/**
	 * Setting the topic map id
	 * 
	 * @param topicMapId
	 *            the topicMapId to set
	 */
	public void setTopicMapId(String topicMapId) {
		this.topicMapId = topicMapId;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isNullValue(int index) {
		Object obj = getValue(index);
		return obj == null;
	}
	
	/**
	 * @return the topicMapId
	 */
	public String getTopicMapId() {
		return topicMapId;
	}
}
