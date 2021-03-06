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

import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.JTMQRFormat;
import de.topicmapslab.tmql4j.util.HashUtil;

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
	 * the topic map
	 */
	private final TopicMap topicMap;

	/**
	 * the topic map system set to runtime
	 */
	private final TopicMapSystem topicMapSystem;

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
	 * the alias map
	 */
	private Map<String, Integer> alias;
	/**
	 * the alias by index
	 */
	private Map<Integer, String> indexes;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param topicMapSystem
	 *            the topic map system
	 * @param topicMap
	 *            the topic map
	 */
	@SuppressWarnings("unchecked")
	public ResultSet(final TopicMapSystem topicMapSystem, final TopicMap topicMap) {
		this.topicMap = topicMap;
		this.topicMapSystem = topicMapSystem;
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
		this.clazz = (Class<T>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];

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
	 * Internal method to set the alias
	 * 
	 * @param alias
	 *            the alias to set
	 */
	public void setAlias(Map<String, Integer> alias) {
		this.alias = alias;
	}

	/**
	 * Internal method to set the indexes
	 * 
	 * @param indexes
	 *            the indexes to set
	 */
	public void setIndexes(Map<Integer, String> indexes) {
		this.indexes = indexes;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addResults(Collection<T> results) {
		for (T result : results) {
			addResult(result);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addResults(T... results) {
		for (T result : results) {
			addResult(result);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<? extends T> getResultClass() {
		return clazz;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return results.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T first() throws NoSuchElementException {
		iterator = results.iterator();
		return iterator.next();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T next() throws NoSuchElementException {
		return iterator.next();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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
	@Override
	@SuppressWarnings("unchecked")
	public void addResult(IResult result) {
		this.results.add((T) result);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return results.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Method return a string representation of the result set. The string
	 * representation is a comma-separated list of the string-representations of
	 * all contained results. The returned pattern is { &lt; results &gt; }.
	 * 
	 * @return the string representation of the result set
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\r\n");
		Iterator<T> iterator = iterator();
		while (iterator.hasNext()) {
			builder.append(iterator.next().toString() + (iterator.hasNext() ? "," : "") + "\r\n");
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
	@Override
	public synchronized void clear() {
		this.results.clear();
		this.iterator = this.results.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> getResults() {
		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T get(int index) {
		if (getResults().size() <= index) {
			throw new IndexOutOfBoundsException("Result set does not contains an element at position '" + index + "'.");
		}
		return getResults().get(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <R extends Object> R get(int rowIndex, int colIndex) {
		T result = get(rowIndex);
		return (R) result.get(colIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullValue(int rowIndex, int colIndex) {
		Object obj = get(rowIndex, colIndex);
		return obj == null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <R extends Object> R get(int rowIndex, String alias) {
		IResult result = get(rowIndex);
		return (R) result.get(alias);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullValue(int rowIndex, String alias) {
		Object obj = get(rowIndex, alias);
		return obj == null;
	}

	private static ResultSet<?> emptyResultSet;

	/**
	 * Returns an empty unmodifiable result set.
	 * 
	 * @return the result set
	 */
	public static ResultSet<?> emptyResultSet() {
		if (emptyResultSet == null) {
			emptyResultSet = new ResultSet<IResult>(null, null) {
				@Override
				public String getResultType() {
					return ResultType.TMAPI.name();
				}

				/**
				 * {@inheritDoc}
				 */
				@Override
				public void addResult(IResult result) {
					throw new UnsupportedOperationException("Unmodifiable result set does not supports method add.");
				}

				/**
				 * {@inheritDoc}
				 */
				@Override
				public void addResults(Collection<IResult> results) {
					throw new UnsupportedOperationException("Unmodifiable result set does not supports method add.");
				}

				/**
				 * {@inheritDoc}
				 */
				@Override
				public void addResults(IResult... results) {
					throw new UnsupportedOperationException("Unmodifiable result set does not supports method add.");
				}

				/**
				 * {@inheritDoc}
				 */
				@Override
				public Class<? extends IResult> getResultClass() {
					return IResult.class;
				}

				/**
				 * {@inheritDoc}
				 */
				@Override
				public void unify() {
					// NOTHING TO DO
				}

				/**
				 * {@inheritDoc}
				 */
				@Override
				public boolean isNullValue(int rowIndex, int colIndex) {
					return false;
				}

				/**
				 * {@inheritDoc}
				 */
				@Override
				public IResult createResult() {
					throw new UnsupportedOperationException("Unmodifiable result set does not supports creation of new results.");
				}

				/**
				 * {@inheritDoc}
				 */
				@Override
				public java.util.Set<String> getAliases() {
					return Collections.emptySet();
				}
			};
		}
		return emptyResultSet;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unify() {
		List<T> results = HashUtil.getList();
		for (T result : getResults()) {
			if (!results.contains(result)) {
				results.add(result);
			}
		}
		this.results.clear();
		this.results.addAll(results);
	}

	/**
	 * @return the alias
	 */
	Map<String, Integer> getAlias() {
		if (alias == null) {
			return Collections.emptyMap();
		}
		return alias;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toCTM() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Current result set cannot be transformed to CTM.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void toCTM(OutputStream os) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Current result set cannot be transformed to CTM.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toJTMQR() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Current result set cannot be transformed to JTMQR.");
	}

	@Override
	public String toJTMQR(JTMQRFormat format) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Current result set cannot be transformed to JTMQR.");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void toJTMQR(OutputStream os) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Current result set cannot be transformed to JTMQR.");
	}
	
	@Override
	public void toJTMQR(OutputStream os, JTMQRFormat format) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Current result set cannot be transformed to JTMQR.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toXTM() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Current result set cannot be transformed to XTM.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void toXTM(OutputStream os) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Current result set cannot be transformed to XTM.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TopicMap toTopicMap() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Current result set cannot be transformed to a topic map.");
	}

	/**
	 * Internal method to get the topic map which was queried
	 * 
	 * @return the topicMap
	 */
	protected TopicMap getTopicMap() {
		return topicMap;
	}

	/**
	 * Internal method to get the topic map system set to runtime
	 * 
	 * @return the topicMapSystem
	 */
	protected TopicMapSystem getTopicMapSystem() {
		return topicMapSystem;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAlias(int index) {
		if (indexes == null) {
			return null;
		}
		return indexes.get(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getIndex(String alias) {
		if (alias == null || !this.alias.containsKey(alias)) {
			return -1;
		}
		return this.alias.get(alias);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasAlias() {
		return indexes != null && alias != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getAliases() {
		if (hasAlias()) {
			return alias.keySet();
		}
		return Collections.emptySet();
	}
}
