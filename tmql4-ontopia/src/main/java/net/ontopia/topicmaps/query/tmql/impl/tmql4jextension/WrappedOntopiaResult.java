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
import java.util.NoSuchElementException;

import net.ontopia.topicmaps.core.OccurrenceIF;
import net.ontopia.topicmaps.impl.tmapi2.OntopiaToTMAPIWrapper;

import org.tmapi.core.Construct;
import org.tmapi.core.Locator;

import de.topicmapslab.tmql4j.components.processor.results.IResult;

public class WrappedOntopiaResult implements IResult {

	private final List<Object> results;
	private Iterator<Object> iterator = null;
	private String topicMapId;

	/**
	 * constructor
	 */
	public WrappedOntopiaResult() {
		this.results = new LinkedList<Object>();
	}

	/**
	 * constructor
	 * 
	 * @param results
	 *            the results
	 */
	public WrappedOntopiaResult(Collection<Object> results) {
		this.results = new LinkedList<Object>();
		add(results);
	}

	/**
	 * constructor
	 * 
	 * @param results
	 *            the results
	 */
	public WrappedOntopiaResult(Object... results) {
		this.results = new LinkedList<Object>();
		add(results);
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
	public Object first() {
		return results.iterator().next();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public Object last() {
		return results.get(results.size() - 1);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public Iterator<Object> iterator() {
		return results.iterator();
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
	 * 
	 * {@inheritDoc}
	 */
	public int size() {
		return this.results.size();
	}

	/**
	 * Returns all values
	 * 
	 * @return all values
	 */
	public Object[] getValues() {
		return results.toArray();
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
				values.add(results.get(index));
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
			return results.get(index);
		}
		return null;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public List<Object> getResults() {
		return results;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void add(Object o) {
		if (o instanceof List<?>) {
			this.results.add(OntopiaToTMAPIWrapper.toTMObjectIF((List<?>) o, topicMapId));
		} else if (o instanceof Construct) {
			Object obj = OntopiaToTMAPIWrapper.toTMObjectIF(o, topicMapId);
			if (obj instanceof OccurrenceIF) {
				this.results.add(OntopiaToTMAPIWrapper.toTMObjectIF((OccurrenceIF) obj, topicMapId));
			} else {
				this.results.add(obj);
			}

		} else if (o instanceof Locator) {
			this.results.add(((Locator) o).toExternalForm());
		} else {
			this.results.add(o);
		}
		iterator = null;
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
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Object next() throws NoSuchElementException {
		if (iterator == null) {
			iterator = results.iterator();
		}
		return iterator.next();
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
	 * @param topicMapId the topicMapId to set
	 */
	public void setTopicMapId(String topicMapId) {
		this.topicMapId = topicMapId;
	}
}
