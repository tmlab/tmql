package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.Result;
import de.topicmapslab.tmql4j.components.processor.results.model.ResultSet;

public class SimpleJtmqrResult implements IResult {

	private final List<Object> results;
	private Iterator<Object> iterator;
	private final SimpleJtmqrResultSet parent;

	
	public SimpleJtmqrResult(SimpleJtmqrResultSet parent) {
		this.parent = parent;
		this.results = new ArrayList<Object>();
		this.iterator = this.results.iterator();
	}
	
	@Override
	public Iterator<Object> iterator() {
		return this.results.iterator();
	}

	@Override
	public void add(Object value) {
		this.results.add(value);
	}

	@Override
	public void add(Object... values) {
		for (Object value : values) {
			add(value);
		}
	}

	@Override
	public void add(Collection<Object> values) {
		for (Object value : values) {
			add(value);
		}
	}

	@Override
	public Object first() throws NoSuchElementException {
		this.iterator = this.results.iterator();
		return this.iterator.next();
	}

	@Override
	public Object last() throws NoSuchElementException {
		try {
			return this.results.get(this.results.size() - 1);
		} catch (IndexOutOfBoundsException e) {
			throw new NoSuchElementException(e.getLocalizedMessage());
		}
	}

	@Override
	public Object next() throws NoSuchElementException {
		return this.iterator.next();
	}

	@Override
	public List<Object> getResults() {
		return this.results;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(int index) {
		if (getResults().size() <= index) {
			throw new IndexOutOfBoundsException("Result does not contains an element at position '" + index + "'.");
		}
		return (T) getResults().get(index);
	}

	@Override
	public boolean isNullValue(int index) {
		Object value = get(index);
		return value == null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String alias) {
		Integer index = (Integer) this.parent.getAlias().get(alias);
		if (index == null) {
			throw new IllegalArgumentException("Given alias is unknown for the result set.");
		}
		return (T)get(index);
	}

	@Override
	public boolean isNullValue(String alias) {
		Integer index = (Integer) this.parent.getAlias().get(alias);
		if (index == null) {
			throw new IllegalArgumentException("Given alias is unknown for the result set.");
		}
		return isNullValue(index);
	}

	@Override
	public int size() {
		return this.results.size();
	}
	
	

	

}
