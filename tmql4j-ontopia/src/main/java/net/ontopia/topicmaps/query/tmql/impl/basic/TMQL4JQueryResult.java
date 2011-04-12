/**
 * TMQL4J Plugin for Ontopia
 * 
 * Copyright: Copyright 2009 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * Author: Sven Krosse
 * 
 */
package net.ontopia.topicmaps.query.tmql.impl.basic;

import java.util.LinkedList;
import java.util.List;

import net.ontopia.topicmaps.query.core.QueryResultIF;
import net.ontopia.topicmaps.query.tmql.impl.tmql4jextension.WrappedOntopiaResult;
import net.ontopia.topicmaps.query.tmql.impl.tmql4jextension.WrappedOntopiaResultSet;
import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;

public class TMQL4JQueryResult implements QueryResultIF {

	private final WrappedOntopiaResultSet resultSet;
	private final List<String> columnNames;
	private int currentRow;

	/**
	 * constructor
	 * 
	 * @param tmid
	 *            the topic map id
	 * @param resultSet
	 *            the result set
	 * @param columnNames
	 *            the column names
	 */
	public TMQL4JQueryResult(final String tmid, final IResultSet<?> resultSet, final List<String> columnNames) {
		if (columnNames != null) {
			this.columnNames = columnNames;
		} else {
			this.columnNames = new LinkedList<String>();
		}

		this.resultSet = new WrappedOntopiaResultSet(null,null);

		/*
		 * convert to ontopia result
		 */
		if (resultSet != null) {
			for (IResult result : resultSet) {
				WrappedOntopiaResult result_ = new WrappedOntopiaResult(this.resultSet);
				result_.add(result.getResults());
				result_.setTopicMapId(tmid);
				this.resultSet.addResult(result_);
			}
		}

		long size = 0;
		if (this.resultSet.size() > 0) {
			size = ((WrappedOntopiaResult) this.resultSet.first()).size();
		}

		for (int index = this.columnNames.size(); index < size; index++) {
			this.columnNames.add("Column " + index);
		}

		this.currentRow = -1;
	}

	/**
	 * constructor
	 * 
	 * @param tmid
	 *            the topic map id
	 * @param resultSet
	 *            the result set
	 */
	public TMQL4JQueryResult(final String tmid, final IResultSet<?> resultSet) {
		this.columnNames = new LinkedList<String>();
		this.resultSet = new WrappedOntopiaResultSet(null,null);

		/*
		 * convert to ontopia result
		 */
		if (resultSet != null) {
			for (IResult result : resultSet) {
				WrappedOntopiaResult result_ = new WrappedOntopiaResult(this.resultSet);
				result_.add(result.getResults());
				result_.setTopicMapId(tmid);
				this.resultSet.addResult(result_);
			}
			long size = 0;
			if (this.resultSet.size() > 0) {
				size = ((WrappedOntopiaResult) this.resultSet.first()).size();
			}
			for (int index = 0; index < size; index++) {
				this.columnNames.add("Column " + index);
			}
		}

		this.currentRow = -1;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public void close() {

	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public String getColumnName(int index) {
		if (index < columnNames.size()) {
			return columnNames.get(index);
		}
		return null;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public String[] getColumnNames() {
		return columnNames.toArray(new String[0]);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public Object getValue(int index) {
		WrappedOntopiaResult result = resultSet.getValue(currentRow);
		if (result != null) {
			return result.getValue(index);
		}
		return null;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public Object[] getValues() {
		WrappedOntopiaResult result = resultSet.getValue(currentRow);
		if (result != null) {
			return result.getValues();
		}
		return new Object[0];
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public Object[] getValues(Object[] values) {
		Object[] row = getValues();
		System.arraycopy(row, 0, values, 0, row.length);
		return values;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public int getWidth() {
		if (resultSet.size() > 0) {
			return ((WrappedOntopiaResult) resultSet.iterator().next()).size();
		}
		return 0;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public boolean next() {
		currentRow++;
		return currentRow < resultSet.size();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public int getIndex(String columnName) {
		if (columnNames.contains(columnName)) {
			return columnNames.indexOf(columnName);
		}
		return -1;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public Object getValue(String columnName) {
		int index = getIndex(columnName);
		if (index != -1) {
			return getValue(index);
		}
		return null;
	}

}
