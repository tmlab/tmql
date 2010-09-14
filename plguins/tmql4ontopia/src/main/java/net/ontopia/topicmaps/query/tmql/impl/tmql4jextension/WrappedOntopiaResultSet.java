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

import de.topicmapslab.tmql4j.resultprocessing.model.IResult;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;
import de.topicmapslab.tmql4j.resultprocessing.model.ResultType;

public class WrappedOntopiaResultSet implements
		IResultSet<WrappedOntopiaResult> {

	private final List<WrappedOntopiaResult> results;
	private Iterator<WrappedOntopiaResult> iterator;

	public WrappedOntopiaResultSet() {
		results = new LinkedList<WrappedOntopiaResult>();
	}

	public WrappedOntopiaResultSet(Collection<WrappedOntopiaResult> results) {
		this.results = new LinkedList<WrappedOntopiaResult>();
		this.results.addAll(results);
	}

	public WrappedOntopiaResultSet(WrappedOntopiaResult... results) {
		this.results = new LinkedList<WrappedOntopiaResult>();
		this.results.addAll(Arrays.asList(results));
	}

	public void addResult(WrappedOntopiaResult result) {
		this.results.add(result);
		iterator = null;
	}

	public WrappedOntopiaResult first() {
		return this.results.iterator().next();
	}

	public WrappedOntopiaResult last() {
		return this.results.get(this.results.size() - 1);
	}

	public Iterator<WrappedOntopiaResult> iterator() {
		return this.results.iterator();
	}

	public int size() {
		return this.results.size();
	}

	public Class<WrappedOntopiaResult> getResultClass() {
		return WrappedOntopiaResult.class;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\r\n");
		Iterator<WrappedOntopiaResult> iterator = iterator();
		while (iterator.hasNext()) {
			builder.append(iterator.next().toString()
					+ (iterator.hasNext() ? "," : "") + "\r\n");
		}
		builder.append("}");
		return builder.toString();
	}

	public WrappedOntopiaResult[] getValues() {
		return results.toArray(new WrappedOntopiaResult[0]);
	}

	public WrappedOntopiaResult[] getValues(Integer... indizes) {
		List<Object> values = new LinkedList<Object>();
		for (Integer index : indizes) {
			if (index < size()) {
				values.add(results.get(index));
			}
		}
		return values.toArray(new WrappedOntopiaResult[0]);
	}

	public WrappedOntopiaResult getValue(Integer index) {
		if (index < size() && index != -1) {
			return (WrappedOntopiaResult) results.get(index);
		}
		return null;
	}

	public void addResults(Collection<WrappedOntopiaResult> results) {
		for (IResult result : results) {
			addResult(result);
		}
	}

	public void addResults(WrappedOntopiaResult... results) {
		for (IResult result : results) {
			addResult(result);
		}
	}

	public boolean canReduceTo2Dimensions() {
		return false;
	}

	public void reduceTo2Dimensions() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addResult(IResult result) {
		if (result instanceof WrappedOntopiaResult) {
			addResult((WrappedOntopiaResult) result);
		}
	}

	@Override
	public void clear() {
		iterator = null;
		results.clear();
	}

	@Override
	public String getResultType() {
		return ResultType.TMAPI.name();
	}

	@Override
	public List<WrappedOntopiaResult> getResults() {
		return results;
	}

	@Override
	public WrappedOntopiaResult next() throws NoSuchElementException {
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
	public WrappedOntopiaResult get(int index) {
		if ( getResults().size() <= index ){
			throw new IndexOutOfBoundsException("Result does not contains an element at position '"+ index + "'.");
		}
		return getResults().get(index);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <R extends Object> R get(int rowIndex, int colIndex) {
		return (R)get(rowIndex).get(colIndex);
	}

}
