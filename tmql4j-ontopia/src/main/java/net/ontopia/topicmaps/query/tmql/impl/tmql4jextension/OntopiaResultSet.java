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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import de.topicmapslab.tmql4j.components.processor.results.IResult;
import de.topicmapslab.tmql4j.components.processor.results.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.ResultType;

public class OntopiaResultSet implements IResultSet<OntopiaResult> {

	private final List<OntopiaResult> results;
	private Iterator<OntopiaResult> iterator;

	/**
	 * constructor
	 */
	public OntopiaResultSet() {
		results = new LinkedList<OntopiaResult>();
	}
	/**
	 * constructor
	 * @param results the results
	 */
	public OntopiaResultSet(Collection<OntopiaResult> results) {
		this.results = new LinkedList<OntopiaResult>();
		this.results.addAll(results);
	}

	/**
	 * constructor
	 * @param results the results
	 */
	public OntopiaResultSet(OntopiaResult... results) {
		this.results = Arrays.asList(results);
	}

	/**
	 * {@inheritDoc}
	 */
	public void addResult(OntopiaResult result) {
		this.results.add(result);
		iterator = null;
	}

	/**
	 * {@inheritDoc}
	 */
	public OntopiaResult first() {
		return this.results.iterator().next();
	}

	/**
	 * {@inheritDoc}
	 */
	public OntopiaResult last() {
		return this.results.get(this.results.size() - 1);
	}

	/**
	 * {@inheritDoc}
	 */
	public Iterator<OntopiaResult> iterator() {
		return this.results.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	public int size() {
		return this.results.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<OntopiaResult> getResultClass() {
		return OntopiaResult.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\r\n");
		Iterator<OntopiaResult> iterator = iterator();
		while (iterator.hasNext()) {
			builder.append(iterator.next().toString() + (iterator.hasNext() ? "," : "") + "\r\n");
		}
		builder.append("}");
		return builder.toString();
	}

	/**
	 * Returns all values
	 * 
	 * @return all values
	 */
	public OntopiaResult[] getValues() {
		return results.toArray(new OntopiaResult[0]);
	}

	/**
	 * Returns the values at the specified positions
	 * 
	 * @param indizes
	 *            the indexes
	 * @return the values
	 */
	public OntopiaResult[] getValues(Integer... indizes) {
		List<Object> values = new LinkedList<Object>();
		for (Integer index : indizes) {
			if (index < size()) {
				values.add(results.get(index));
			}
		}
		return values.toArray(new OntopiaResult[0]);
	}

	/**
	 * Returns the value at the specific index
	 * 
	 * @param index
	 *            the index
	 * @return the value or <code>null</code>
	 */
	public OntopiaResult getValue(Integer index) {
		if (index < size()) {
			return (OntopiaResult) results.get(index);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void addResults(Collection<OntopiaResult> results) {
		for (IResult result : results) {
			addResult(result);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void addResults(OntopiaResult... results) {
		for (IResult result : results) {
			addResult(result);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addResult(IResult result) {
		if (result instanceof OntopiaResult) {
			addResult((OntopiaResult) result);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		iterator = null;
		results.clear();
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
	@Override
	public List<OntopiaResult> getResults() {
		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OntopiaResult next() throws NoSuchElementException {
		if (iterator == null) {
			iterator = results.iterator();
		}
		return iterator.next();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isEmpty() {
		return results.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	public OntopiaResult get(int index) {
		if (getResults().size() <= index) {
			throw new IndexOutOfBoundsException("Result does not contains an element at position '" + index + "'.");
		}
		return getResults().get(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <R extends Object> R get(int rowIndex, int colIndex) {
		return (R) get(rowIndex).get(colIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void unify() {
		// NOTHING TO DO HERE		
	}
	
}
