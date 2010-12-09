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
package de.topicmapslab.tmql4j.components.processor.results;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import org.tmapi.core.Association;
import org.tmapi.core.Topic;

/**
 * Interface definition of a tuple of the generated result set. The result
 * represents a row in the result set and contains a number of atomic or complex
 * values like {@link Topic}s, {@link Association}s, CTM- or XTM-strings.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public interface IResult extends Iterable<Object> {

	/**
	 * Add a new values to the result.
	 * 
	 * @param value
	 *            the value to add
	 */
	public void add(Object value);

	/**
	 * Add a number of values to the result.
	 * 
	 * @param values
	 *            a comma-separated list of values
	 */
	public void add(Object... values);

	/**
	 * Add a number of values to the result.
	 * 
	 * @param values
	 *            a collection containing a number of values
	 */
	public void add(Collection<Object> values);

	/**
	 * Method returns the first element of the result, if the result contains at
	 * least one element. The Method also reset the internal iterator.
	 * 
	 * @see IResult#next()
	 * @return the first element of the result
	 * @throws NoSuchElementException
	 *             thrown if the result is empty
	 */
	Object first() throws NoSuchElementException;

	/**
	 * Method returns the last element of the result, if the result contains at
	 * least one element.
	 * 
	 * @return the last element of the result
	 * @throws NoSuchElementException
	 *             thrown if the result is empty
	 */
	Object last() throws NoSuchElementException;

	/**
	 * Method return the next element identified by the internal iterator
	 * instance. If no element is available after current position, an exception
	 * will be thrown.
	 * 
	 * @return the next element after current position
	 * @throws NoSuchElementException
	 *             thrown if no element is available
	 */
	Object next() throws NoSuchElementException;

	/**
	 * Method returns the whole list of contained values.
	 * 
	 * @return a list of all values.
	 */
	public List<Object> getResults();

	/**
	 * Returns the item at the given position
	 * 
	 * @param <T>
	 *            the type of item at this position
	 * @param index
	 *            the index
	 * @return the construct
	 * @throws IndexOutOfBoundsException
	 *             thrown if index is out of bounds
	 * @throws ClassCastException
	 *             thrown if construct at position has other type
	 */
	public <T extends Object> T get(int index);

	/**
	 * Checks if the value at the given position is <code>null</code>
	 * 
	 * @param index
	 *            the index
	 * @return <code>true</code> if the value at the given position is
	 *         <code>null</code>, <code>false</code> otherwise.
	 */
	public boolean isNullValue(int index);

	/**
	 * Method returns the number of contained values.
	 * 
	 * @return the number of contained values
	 */
	public int size();
}
