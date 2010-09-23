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
package de.topicmapslab.tmql4j.resultprocessing.base;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.tmapi.core.Association;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.resultprocessing.model.IResult;

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
	 * base constructor to create a new instance
	 */
	public Result() {
		/*
		 * create result list
		 */
		this.results = new LinkedList<Object>();

		/*
		 * create iterator instance
		 */
		this.iterator = results.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<Collection<Object>> reduceTo2Dimensions()
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean canReduceTo2Dimensions() {
		return false;
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
		if ( value != null ){
			this.results.add(value);
		}
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
		Iterator<Object> iterator = iterator();
		while (iterator.hasNext()) {
			Object obj = iterator.next();
			builder.append((obj==null?"":obj.toString())
					+ (iterator.hasNext() ? ", " : ""));
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
		if ( getResults().size() <= index ){
			throw new IndexOutOfBoundsException("Result does not contains an element at position '"+ index + "'.");
		}
		return (T) getResults().get(index);
	}

}
