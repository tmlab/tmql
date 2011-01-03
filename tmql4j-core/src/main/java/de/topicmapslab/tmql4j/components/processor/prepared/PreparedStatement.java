/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.prepared;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.tmapi.core.Construct;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.components.parser.IParserTree;
import de.topicmapslab.tmql4j.components.processor.results.IResultSet;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.grammar.productions.PreparedExpression;
import de.topicmapslab.tmql4j.query.IQuery;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class PreparedStatement implements IPreparedStatement {

	/**
	 * 
	 */
	private static final String MISSING_INDEX_FOR_EXPRESSION = "Missing index for expression!";
	/**
	 * 
	 */
	private static final String THE_TOPIC_MAP_IS_NOT_SET = "The Topic Map is not set!";
	/**
	 * 
	 */
	private static final String THE_QUERY_CONTAINS_FORBIDDEN_EXPRESSION = "The query contains forbidden expression!";
	/**
	 * exception message
	 */
	private static final String MISSING_VALUE_FOR_INDEX = "Missing value for index {0}.";
	/**
	 * exception message
	 */
	private static final String THE_GIVEN_INDEX_IS_OUT_OF_RANGE = "The given index {0} is out of range.";
	private Map<Integer, Object> values;
	private final Map<IExpression, Integer> indexes;
	private final IParserTree tree;
	private final ITMQLRuntime runtime;
	private final IQuery query;
	private String nonParametrizedQueryString = null;

	/**
	 * 
	 */
	public PreparedStatement(ITMQLRuntime runtime, IQuery query, IParserTree tree) {
		this.runtime = runtime;
		this.tree = tree;
		this.query = query;
		this.indexes = HashUtil.getHashMap();
		int index = 0;
		for (List<IExpression> path : tree.paths(PreparedExpression.class)) {
			indexes.put(path.get(path.size() - 1), index);
			index++;
		}
	}

	/**
	 * Utility method to check if the given index represent a valid place-holder
	 * position
	 * 
	 * @param index
	 *            the index
	 * @throws TMQLRuntimeException
	 */
	private void checkIndex(int index) throws TMQLRuntimeException {
		if (index >= indexes.size()) {
			throw new TMQLRuntimeException(MessageFormat.format(THE_GIVEN_INDEX_IS_OUT_OF_RANGE, index));
		}
	}

	/**
	 * Utility method to set the value for the given position
	 * 
	 * @param index
	 *            the index
	 * @param value
	 *            the value
	 */
	private void internalSet(int index, Object value) {
		if (values == null) {
			values = HashUtil.getHashMap();
		}
		values.put(index, value);
		nonParametrizedQueryString = null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void set(int index, Object object) {
		checkIndex(index);
		internalSet(index, object);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLong(int index, long value) {
		checkIndex(index);
		internalSet(index, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDouble(int index, double value) {
		checkIndex(index);
		internalSet(index, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDate(int index, Calendar value) {
		checkIndex(index);
		internalSet(index, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setString(int index, String value) {
		checkIndex(index);
		internalSet(index, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTopic(int index, Topic topic) {
		checkIndex(index);
		internalSet(index, topic);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setConstruct(int index, Construct construct) {
		checkIndex(index);
		internalSet(index, construct);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object get(IExpression expression) {
		if (indexes.containsKey(expression)) {
			return get(indexes.get(expression));
		}
		throw new TMQLRuntimeException(MISSING_INDEX_FOR_EXPRESSION);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object get(int index) {
		return getValues().get(index);
	}

	/**
	 * @return the tree
	 */
	protected IParserTree getTree() {
		return tree;
	}

	/**
	 * @return the values
	 */
	protected Map<Integer, Object> getValues() {
		if (values == null) {
			return Collections.emptyMap();
		}
		return values;
	}

	/**
	 * Validation method, to check first if all place holders are set
	 * 
	 * @throws TMQLRuntimeException
	 */
	protected void validate() throws TMQLRuntimeException {
		/*
		 * first check if all indexes are set
		 */
		for (Integer index : indexes.values()) {
			if (!getValues().containsKey(index) || getValues().get(index) == null) {
				throw new TMQLRuntimeException(MessageFormat.format(MISSING_VALUE_FOR_INDEX, index));
			}
		}
		/*
		 * check if the tree does not contains forbidden expression
		 */
		if (!tree.isValid(runtime, query)) {
			throw new TMQLRuntimeException(THE_QUERY_CONTAINS_FORBIDDEN_EXPRESSION);
		}
		/*
		 * check if topic map is set
		 */
		if (getTopicMap() == null) {
			throw new TMQLRuntimeException(THE_TOPIC_MAP_IS_NOT_SET);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public TopicMap getTopicMap() {
		return query.getTopicMap();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getQueryString() {
		return query.getQueryString();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setResults(IResultSet<?> results) {
		query.setResults(results);
	}

	/**
	 * {@inheritDoc}
	 */
	public IResultSet<?> getResults() throws TMQLRuntimeException {
		return query.getResults();
	}

	/**
	 * {@inheritDoc}
	 */
	public void allowExpression(Class<? extends IExpression> allowedExpressionType) {
		query.allowExpression(allowedExpressionType);
	}

	/**
	 * {@inheritDoc}
	 */
	public void allowModificationQueries() {
		query.allowModificationQueries();
	}

	/**
	 * {@inheritDoc}
	 */
	public void forbidExpression(Class<? extends IExpression> forbiddenExpressionType) {
		query.forbidExpression(forbiddenExpressionType);
	}

	/**
	 * {@inheritDoc}
	 */
	public void forbidModificationQueries() {
		query.forbidModificationQueries();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isForbidden(Class<? extends IExpression> expressionType) {
		return query.isForbidden(expressionType);
	}

	/**
	 * {@inheritDoc}
	 */
	public void afterQuery(ITMQLRuntime runtime) {
		// NOTHING TO DO
	}

	/**
	 * {@inheritDoc}
	 */
	public void beforeQuery(ITMQLRuntime runtime) {
		// NOTHING TO DO
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTopicMap(TopicMap topicMap) {
		query.setTopicMap(topicMap);
	}

	/**
	 * @return the runtime
	 */
	protected ITMQLRuntime getRuntime() {
		return runtime;
	}

	/**
	 * {@inheritDoc}
	 */
	public void run(Object... parameters) {
		for ( int index = 0 ; index < parameters.length ; index++ ){
			set(index, parameters[index]);
		}
		validate();
		runtime.getTmqlProcessor().query(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getNonParametrizedQueryString() {
		/*
		 * lazy load
		 */
		if (nonParametrizedQueryString == null) {
			/*
			 * get query
			 */
			nonParametrizedQueryString = getQueryString();
			/*
			 * iterate over
			 */
			for (int index = 0; index < getValues().size(); index++) {
				Object value = getValues().get(index);
				if (value == null) {
					throw new TMQLRuntimeException(MessageFormat.format(MISSING_VALUE_FOR_INDEX, index));
				}
				String replacement;
				if (value instanceof Construct) {
					replacement = "\"" + ((Construct) value).getId() + "\" << id";
				} else {
					replacement = value.toString();
				}
				nonParametrizedQueryString = nonParametrizedQueryString.replaceFirst("\\?", replacement);
			}
		}
		return nonParametrizedQueryString;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IParserTree getParserTree() {
		return tree;
	}
}
