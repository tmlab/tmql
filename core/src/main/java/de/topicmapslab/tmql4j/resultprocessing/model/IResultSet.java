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
package de.topicmapslab.tmql4j.resultprocessing.model;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;

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
	 * Reduce internal results to atomic results. If result set contains results
	 * containing complex values like tuple sequences or collections, they will
	 * be reduce to a set of values. If more than one complex type is contained,
	 * all possible combinations will be generated. For example the values [ { A
	 * , B } , { C , D } ] will be transformed to [ A , C ] , [ A , D ] , [ B ,
	 * C ] , [ B , D ].
	 * 
	 * @throws UnsupportedOperationException
	 *             thrown if operation is not supported by the implementation
	 * @see IResult#reduceTo2Dimensions()
	 */
	public void reduceTo2Dimensions() throws UnsupportedOperationException,
			TMQLRuntimeException;

	/**
	 * Method checks if the current implementation supports the reduction to a
	 * 2-dimensional result set.
	 * 
	 * @see IResultSet#reduceTo2Dimensions()
	 * @return <code>true</code> if operation is supported, <code>false</code>
	 *         otherwise
	 */
	public boolean canReduceTo2Dimensions();

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
}
