package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.result;

import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.model.ResultSet;
import de.topicmapslab.tmql4j.components.processor.results.model.ResultType;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;
import de.topicmapslab.tmql4j.util.HashUtil;

public class SimpleJtmqrResultSet<T extends IResult> implements IResultSet<T>{

	private final List<T> results;
	private Iterator<T> iterator;
	private final Class<T> clazz;
	private Map<String, Integer> alias;
	private Map<Integer, String> indexes;
	
	@SuppressWarnings("unchecked")
	public SimpleJtmqrResultSet() {
	
		Class<?> clazz = getClass();

		while (!(clazz.getGenericSuperclass() instanceof ParameterizedType)) {
			clazz = clazz.getSuperclass();
		}

		this.clazz = (Class<T>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
		this.results = new LinkedList<T>();
		this.iterator = this.results.iterator();
		this.alias = new HashMap<String, Integer>();
	}
	
	@Override
	public Iterator<T> iterator() {
		return this.iterator;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addResult(IResult result) {
		this.results.add((T)result);
	}

	@Override
	public void addResults(Collection<T> results) {
		for (T result : results) {
			addResult(result);
		}
	}

	@Override
	public void addResults(T... results) {
		for (T result : results) {
			addResult(result);
		}
	}

	@Override
	public T first() throws NoSuchElementException {
		this.iterator = this.results.iterator();
		return this.iterator.next();
	}

	@Override
	public T last() throws NoSuchElementException {
		try {
			return this.results.get(this.results.size() - 1);
		} catch (IndexOutOfBoundsException e) {
			throw new NoSuchElementException(e.getLocalizedMessage());
		}
	}

	@Override
	public T next() throws NoSuchElementException {
		return this.iterator.next();
	}

	@Override
	public Class<? extends T> getResultClass() {
		return this.clazz;
	}

	@Override
	public String getResultType() {
		return ResultType.TMAPI.name();
	}

	@Override
	public int size() {
		return this.results.size();
	}

	@Override
	public T get(int index) {
		if (getResults().size() <= index) {
			throw new IndexOutOfBoundsException("Result set does not contains an element at position '" + index + "'.");
		}
		return getResults().get(index);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> R get(int rowIndex, int colIndex) {
		T result = get(rowIndex);
		return (R) result.get(colIndex);
	}

	@Override
	public boolean isNullValue(int rowIndex, int colIndex) {
		Object obj = get(rowIndex, colIndex);
		return obj == null;
	}

	@Override
	public <R> R get(int rowIndex, String alias) {
		IResult result = get(rowIndex);
		return (R) result.get(alias);
	}

	@Override
	public boolean isNullValue(int rowIndex, String alias) {
		Object obj = get(rowIndex, alias);
		return obj == null;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public void clear() {
		this.results.clear();
		this.iterator = this.results.iterator();
	}

	@Override
	public List<T> getResults() {
		return this.results;
	}

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

	@Override
	public IResult createResult() {
		return new SimpleJtmqrResult(this);
	}

	@Override
	public String toXTM() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void toXTM(OutputStream os) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toCTM() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void toCTM(OutputStream os) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toJTMQR() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void toJTMQR(OutputStream os) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public TopicMap toTopicMap() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getAlias(int index) {
		if ( indexes == null ){
			return null;
		}
		return getAlias(index);
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

	@Override
	public boolean hasAlias() {
		if(this.alias.isEmpty())
			return false;
		return true;
	}
	
	
	
}
