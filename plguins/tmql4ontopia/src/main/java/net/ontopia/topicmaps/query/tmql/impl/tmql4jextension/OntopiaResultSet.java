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

public class OntopiaResultSet implements IResultSet<OntopiaResult> {

	private final List<OntopiaResult> results;
	private Iterator<OntopiaResult> iterator;

	public OntopiaResultSet() {
		results = new LinkedList<OntopiaResult>();
	}

	public OntopiaResultSet(Collection<OntopiaResult> results) {
		this.results = new LinkedList<OntopiaResult>();
		this.results.addAll(results);
	}

	public OntopiaResultSet(OntopiaResult... results) {
		this.results = Arrays.asList(results);
	}

	public void addResult(OntopiaResult result) {
		this.results.add(result);
		iterator = null;
	}

	public OntopiaResult first() {
		return this.results.iterator().next();
	}

	public OntopiaResult last() {
		return this.results.get(this.results.size() - 1);
	}

	public Iterator<OntopiaResult> iterator() {
		return this.results.iterator();
	}

	public int size() {
		return this.results.size();
	}

	public Class<OntopiaResult> getResultClass() {
		return OntopiaResult.class;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\r\n");
		Iterator<OntopiaResult> iterator = iterator();
		while (iterator.hasNext()) {
			builder.append(iterator.next().toString()
					+ (iterator.hasNext() ? "," : "") + "\r\n");
		}
		builder.append("}");
		return builder.toString();
	}

	public OntopiaResult[] getValues() {
		return results.toArray(new OntopiaResult[0]);
	}

	public OntopiaResult[] getValues(Integer... indizes) {
		List<Object> values = new LinkedList<Object>();
		for (Integer index : indizes) {
			if (index < size()) {
				values.add(results.get(index));
			}
		}
		return values.toArray(new OntopiaResult[0]);
	}

	public OntopiaResult getValue(Integer index) {
		if (index < size()) {
			return (OntopiaResult) results.get(index);
		}
		return null;
	}

	public void addResults(Collection<OntopiaResult> results) {
		for ( IResult result : results){
			addResult(result);
		}
	}

	public void addResults(OntopiaResult... results) {
		for ( IResult result : results){
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
		if ( result instanceof OntopiaResult ){
			addResult((OntopiaResult)result);
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
	public List<OntopiaResult> getResults() {
		return results;
	}

	@Override
	public OntopiaResult next() throws NoSuchElementException {
		if ( iterator == null ){
			iterator = results.iterator();
		}
		return iterator.next();
	}

}
