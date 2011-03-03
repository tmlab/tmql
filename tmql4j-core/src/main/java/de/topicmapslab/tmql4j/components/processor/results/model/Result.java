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
package de.topicmapslab.tmql4j.components.processor.results.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.tmapi.core.Association;
import org.tmapi.core.Topic;

/**
 * Base implementation of {@link IResult} to implement the method providing base
 * functionality. The result represents a row in the result set and contains a
 * number of atomic or complex values like {@link Topic}s, {@link Association}s,
 * CTM- or XTM-strings.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public abstract class Result implements IResult {

	/**
	 * list of contained results
	 */
	private final List<Object> results;
	/**
	 * internal iterator instance
	 */
	private Iterator<Object> iterator;
	/**
	 * the parent result set
	 */
	private final ResultSet<?> parent;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param parent
	 *            the parent result set
	 */
	public Result(ResultSet<?> parent) {
		/*
		 * create result list
		 */
		this.results = new ArrayList<Object>();

		/*
		 * create iterator instance
		 */
		this.iterator = results.iterator();

		this.parent = parent;
	}

	/**
	 * {@inheritDoc}
	 */
	public Iterator<Object> iterator() {
		return results.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object first() throws NoSuchElementException {
		iterator = results.iterator();
		return iterator.next();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object next() throws NoSuchElementException {
		return iterator.next();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object last() throws NoSuchElementException {
		try {
			return this.results.get(results.size() - 1);
		} catch (IndexOutOfBoundsException e) {
			throw new NoSuchElementException(e.getLocalizedMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void add(Object value) {
		this.results.add(value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void add(Collection<Object> values) {
		for (Object value : values) {
			add(value);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void add(Object... values) {
		for (Object value : values) {
			add(value);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Object> getResults() {
		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	public int size() {
		return results.size();
	}

	/**
	 * Method return a string representation of the result. The string
	 * representation is a comma-separated list of the string-representations of
	 * all contained values. The returned pattern is [ &lt; values &gt; ].
	 * 
	 * @return the string representation of the result
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		boolean first = true;
		for (Object obj : getResults()) {
			if (!first) {
				builder.append(", ");
			}
			builder.append((obj == null ? "null" : obj.toString()));
			first = false;
		}
		builder.append("]");
		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return results.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (getClass().isInstance(obj)) {
			return getClass().cast(obj).getResults().equals(results);
		}
		return false;
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
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> T get(String alias) {
		Integer index = parent.getAlias().get(alias);
		if (index == null) {
			throw new IllegalArgumentException("Given alias is unknown for the result set.");
		}
		return (T)get(index);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isNullValue(int index) {
		Object value = get(index);
		return value == null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isNullValue(String alias) {
		Integer index = parent.getAlias().get(alias);
		if (index == null) {
			throw new IllegalArgumentException("Given alias is unknown for the result set.");
		}
		return isNullValue(index);
	}

}
