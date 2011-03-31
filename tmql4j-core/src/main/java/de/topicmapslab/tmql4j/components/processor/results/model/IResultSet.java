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
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.JTMQRFormat;

/**
 * Interface definition of a sequence of tuples as a result of querying process.
 * The result set represents a 2-dimension container containing a number of
 * {@link IResult}s.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IResultSet<T extends IResult> extends Iterable<T> {

	/**
	 * Add one result to the result set.
	 * 
	 * @param result
	 *            the result to add
	 */
	public void addResult(IResult result);

	/**
	 * Add a number of results to the result set.
	 * 
	 * @param results
	 *            a collection contains a number of results
	 */
	public void addResults(Collection<T> results);

	/**
	 * Add a number of results to the result set.
	 * 
	 * @param results
	 *            a comma-separated list of results
	 */
	public void addResults(T... results);

	/**
	 * Method returns the first element of the result set, if the result set
	 * contains at least one element. The Method also reset the internal
	 * iterator.
	 * 
	 * @see IResultSet#next()
	 * @return the first element of the result set
	 * @throws NoSuchElementException
	 *             thrown if the result set is empty
	 */
	public T first() throws NoSuchElementException;

	/**
	 * Method returns the last element of the result set, if the result set
	 * contains at least one element.
	 * 
	 * @return the last element of the result set
	 * @throws NoSuchElementException
	 *             thrown if the result set is empty
	 */
	public T last() throws NoSuchElementException;

	/**
	 * Method return the next element identified by the internal iterator
	 * instance. If no element is available after current position, an exception
	 * will be thrown.
	 * 
	 * @return the next element after current position
	 * @throws NoSuchElementException
	 *             thrown if no element is available
	 */
	public T next() throws NoSuchElementException;

	/**
	 * Method returns the type of contained results specified by the type
	 * parameter T.
	 * 
	 * @return a class representing the type parameter T
	 */
	public Class<? extends T> getResultClass();

	/**
	 * Method returns a string representation of current result type according
	 * to the TMQL draft. Possible values can be XML, CTM, TMAPI and other
	 * realized by TMQL4J-plug-ins.
	 * 
	 * @return a string representation of the result type
	 */
	public String getResultType();

	/**
	 * Method returns the number of contained results.
	 * 
	 * @return the number of contained results
	 */
	public int size();

	/**
	 * Returns the result at the given position
	 * 
	 * @param <T>
	 *            the result type
	 * @param index
	 *            the index
	 * @return the construct
	 * @throws IndexOutOfBoundsException
	 *             thrown if index is out of bounds
	 * @throws ClassCastException
	 *             thrown if construct at position has other type
	 */
	public T get(int index);

	/**
	 * Returns the item at the given cell position
	 * 
	 * @param <R>
	 *            the type of item at this position
	 * @param rowIndex
	 *            the rowIndex
	 * @param colIndex
	 *            the colIndex
	 * @return the construct
	 * @throws IndexOutOfBoundsException
	 *             thrown if index is out of bounds
	 * @throws ClassCastException
	 *             thrown if construct at position has other type
	 */
	public <R extends Object> R get(int rowIndex, int colIndex);

	/**
	 * Checks if the value at the given cell position is <code>null</code>
	 * 
	 * @param rowIndex
	 *            the row index
	 * @param colIndex
	 *            the column index
	 * @return <code>true</code> if the value is <code>null</code>,
	 *         <code>false</code> otherwise
	 * @throws IndexOutOfBoundsException
	 *             thrown if index is out of bounds
	 */
	public boolean isNullValue(int rowIndex, int colIndex);

	/**
	 * Returns the item at the given cell position
	 * 
	 * @param <R>
	 *            the type of item at this position
	 * @param rowIndex
	 *            the rowIndex
	 * @param alias
	 *            the alias of colIndex
	 * @return the construct
	 * @throws ClassCastException
	 *             thrown if construct at position has other type
	 * @throws IllegalArgumentException
	 *             thrown if the given alias is unknown
	 */
	public <R extends Object> R get(int rowIndex, String alias);

	/**
	 * Checks if the value at the given cell position is <code>null</code>
	 * 
	 * @param rowIndex
	 *            the row index
	 * @param alias
	 *            the alias of column index
	 * @return <code>true</code> if the value is <code>null</code>,
	 *         <code>false</code> otherwise
	 */
	public boolean isNullValue(int rowIndex, String alias);

	/**
	 * Method returns if the number of results is 0
	 * 
	 * @return <code>true</code> if the result set is empty, <code>false</code>
	 *         else
	 */
	public boolean isEmpty();

	/**
	 * Method removes all contained results from result set and reset the
	 * internal iterator.
	 */
	public void clear();

	/**
	 * Method returns the whole list of contained results.
	 * 
	 * @return a list of all results.
	 */
	public List<T> getResults();

	/**
	 * Removes duplicates from the internal result set
	 */
	public void unify();

	/**
	 * Creates a new result for the result set
	 * 
	 * @return the new result set
	 */
	public IResult createResult();

	/**
	 * Transform the query result to an XTM string.
	 * 
	 * @return the XTM string
	 * @throws UnsupportedOperationException
	 *             thrown if the current results implementation does not support
	 *             transformation to other formats.
	 */
	public String toXTM() throws UnsupportedOperationException;

	/**
	 * Transform the query result to an XTM stream.
	 * 
	 * @param os
	 *            the output stream the XTM should write to
	 * @throws UnsupportedOperationException
	 *             thrown if the current results implementation does not support
	 *             transformation to other formats.
	 */
	public void toXTM(OutputStream os) throws UnsupportedOperationException;

	/**
	 * Transform the query result to an CTM string.
	 * 
	 * @return the CTM string
	 * @throws UnsupportedOperationException
	 *             thrown if the current results implementation does not support
	 *             transformation to other formats.
	 */
	public String toCTM() throws UnsupportedOperationException;

	/**
	 * Transform the query result to an CTM stream.
	 * 
	 * @param os
	 *            the output stream the CTM should write to
	 * @throws UnsupportedOperationException
	 *             thrown if the current results implementation does not support
	 *             transformation to other formats.
	 */
	public void toCTM(OutputStream os) throws UnsupportedOperationException;

	/**
	 * Transform the query result to an JTMQR string.
	 * 
	 * @return the JTMQR string
	 * @throws UnsupportedOperationException
	 *             thrown if the current results implementation does not support
	 *             transformation to other formats.
	 */
	public String toJTMQR() throws UnsupportedOperationException;

	/**
	 * Transform the query result to an JTMQR stream.
	 * 
	 * @param os
	 *            the output stream the JTMQR should write to
	 * @throws UnsupportedOperationException
	 *             thrown if the current results implementation does not support
	 *             transformation to other formats.
	 */
	public void toJTMQR(OutputStream os) throws UnsupportedOperationException;
	
	/**
	 * Transform the query result to an JTMQR string.
	 * 
	 * @return the JTMQR string
	 * @throws UnsupportedOperationException
	 *             thrown if the current results implementation does not support
	 *             transformation to other formats.
	 */
	public String toJTMQR(JTMQRFormat format) throws UnsupportedOperationException;

	/**
	 * Transform the query result to an JTMQR stream.
	 * 
	 * @param os
	 *            the output stream the JTMQR should write to
	 * @throws UnsupportedOperationException
	 *             thrown if the current results implementation does not support
	 *             transformation to other formats.
	 */
	public void toJTMQR(OutputStream os, JTMQRFormat format) throws UnsupportedOperationException;

	/**
	 * Transform the query result to a topic map instance containing all topics
	 * and associations of the result set
	 * 
	 * @return the topic map
	 * @throws UnsupportedOperationException
	 *             thrown if the current results implementation does not support
	 *             transformation to topic map.
	 */
	public TopicMap toTopicMap() throws UnsupportedOperationException;

	/**
	 * Returns all aliases or an empty set
	 * 
	 * @return the aliases
	 * @since 3.1.0
	 */
	public Set<String> getAliases();

	/**
	 * Returns the index for the given alias or <code>null</code> if no index is
	 * set
	 * 
	 * @param alias
	 *            the alias
	 * @return the index
	 * @since 3.1.0
	 */
	public int getIndex(String alias);

	/**
	 * Returns the alias for the given index or <code>null</code> if no alias is
	 * set
	 * 
	 * @param index
	 *            the index
	 * @return the alias
	 * @since 3.1.0
	 */
	public String getAlias(int index);

	/**
	 * Method checks if any alias is set
	 * 
	 * @return <code>true</code> if at least one alias is set,
	 *         <code>false</code> otherwise
	 * @since 3.1.0
	 */
	public boolean hasAlias();
}
