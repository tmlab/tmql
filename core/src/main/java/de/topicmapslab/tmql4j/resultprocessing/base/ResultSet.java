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

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.resultprocessing.model.IResult;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;

/**
 * Base implementation of {@link IResultSet} to implement the method providing
 * base functionality. The result set represents a 2-dimension container
 * containing a number of {@link IResult}s. functionality.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 * @param <T>
 *            the type of contained results
 */
public abstract class ResultSet<T extends IResult> implements IResultSet<T> {

	/**
	 * list of contained results
	 */
	private final List<T> results;

	/**
	 * internal iterator instance
	 */
	private Iterator<T> iterator;
	/**
	 * the type of contained results
	 */
	private final Class<T> clazz;

	/**
	 * base constructor to create a new instance
	 */
	@SuppressWarnings("unchecked")
	public ResultSet() {
		/*
		 * get current class
		 */
		Class<?> clazz = getClass();
		/*
		 * walk through class hierarchy till the superclass is a parameterized
		 * type
		 */
		while (!(clazz.getGenericSuperclass() instanceof ParameterizedType)) {
			clazz = clazz.getSuperclass();
		}

		/*
		 * extract class information from type
		 */
		this.clazz = (Class<T>) ((ParameterizedType) clazz
				.getGenericSuperclass()).getActualTypeArguments()[0];

		/*
		 * create result list
		 */
		this.results = new LinkedList<T>();

		/*
		 * create iterator instance
		 */
		this.iterator = results.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	public void reduceTo2Dimensions() throws UnsupportedOperationException,
			TMQLRuntimeException {
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
	public void addResults(Collection<T> results) {
		for (T result : results) {
			addResult(result);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void addResults(T... results) {
		for (T result : results) {
			addResult(result);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<T> getResultClass() {
		return clazz;
	}

	/**
	 * {@inheritDoc}
	 */
	public Iterator<T> iterator() {
		return results.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	public T first() throws NoSuchElementException {
		iterator = results.iterator();
		return iterator.next();
	}

	/**
	 * {@inheritDoc}
	 */
	public T next() throws NoSuchElementException {
		return iterator.next();
	}

	/**
	 * {@inheritDoc}
	 */
	public T last() throws NoSuchElementException {
		try {
			return this.results.get(results.size() - 1);
		} catch (IndexOutOfBoundsException e) {
			throw new NoSuchElementException(e.getLocalizedMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void addResult(IResult result) {
		this.results.add((T) result);
	}

	/**
	 * {@inheritDoc}
	 */
	public int size() {
		return results.size();
	}

	/**
	 * Method return a string representation of the result set. The string
	 * representation is a comma-separated list of the string-representations of
	 * all contained results. The returned pattern is { &lt; results &gt; }.
	 * 
	 * @return the string representation of the result set
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\r\n");
		Iterator<T> iterator = iterator();
		while (iterator.hasNext()) {
			builder.append(iterator.next().toString()
					+ (iterator.hasNext() ? "," : "") + "\r\n");
		}
		builder.append("}");
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
		if (clazz.isInstance(obj)) {
			return clazz.cast(obj).getResults().equals(results);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void clear() {
		this.results.clear();
		this.iterator = this.results.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<T> getResults() {
		return results;
	}

}
