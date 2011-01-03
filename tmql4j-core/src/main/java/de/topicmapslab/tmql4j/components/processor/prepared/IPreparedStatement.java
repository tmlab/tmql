/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.prepared;

import java.util.Calendar;

import org.tmapi.core.Construct;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.parser.IParserTree;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * A TMQL prepared statement
 * 
 * @author Sven Krosse
 * 
 */
public interface IPreparedStatement extends IQuery {

	/**
	 * Setting the long value at the current index. If the index is invalid an
	 * exception will be thrown. The indexes are zero-based.
	 * 
	 * @param index
	 *            the index
	 * @param value
	 *            the value
	 */
	public void setLong(int index, long value);

	/**
	 * Setting the long value at the current index. If the index is invalid an
	 * exception will be thrown. The indexes are zero-based.
	 * 
	 * @param index
	 *            the index
	 * @param value
	 *            the value
	 */
	public void setDouble(int index, double value);

	/**
	 * Setting the double value at the current index. If the index is invalid an
	 * exception will be thrown. The indexes are zero-based.
	 * 
	 * @param index
	 *            the index
	 * @param value
	 *            the value
	 */
	public void setDate(int index, Calendar value);

	/**
	 * Setting the integer value at the current index. If the index is invalid
	 * an exception will be thrown. The indexes are zero-based.
	 * 
	 * @param index
	 *            the index
	 * @param value
	 *            the value
	 */
	public void setString(int index, String value);

	/**
	 * Setting the topic at the current index. If the index is invalid an
	 * exception will be thrown. The indexes are zero-based.
	 * 
	 * @param index
	 *            the index
	 * @param topic
	 *            the topic
	 */
	public void setTopic(int index, Topic topic);

	/**
	 * Setting the object at the current index. If the index is invalid an
	 * exception will be thrown. The indexes are zero-based.
	 * 
	 * @param index
	 *            the index
	 * @param object
	 *            the object
	 */
	public void set(int index, Object object);

	/**
	 * Setting the construct at the current index. If the index is invalid an
	 * exception will be thrown. The indexes are zero-based.
	 * 
	 * @param index
	 *            the index
	 * @param construct
	 *            the construct
	 */
	public void setConstruct(int index, Construct construct);

	/**
	 * Executes the prepared statement
	 * 
	 * @param parameters
	 *            the optional parameters for used wildcards
	 */
	public void run(Object... parameters);

	/**
	 * Get the value setting to the given index
	 * 
	 * @param index
	 *            the index
	 * @return the object value
	 */
	public Object get(int index);

	/**
	 * Getting the value setting at the index represented by the given
	 * expression.
	 * 
	 * @param expression
	 *            the expression
	 * @return the object value
	 */
	public Object get(IExpression expression);

	/**
	 * Method transforms the internal query to string and replace every place
	 * holder by the set value.
	 * 
	 * @return the string
	 * 
	 * @throws TMQLRuntimeException
	 *             thrown if at least one argument is missing
	 */
	public String getNonParametrizedQueryString() throws TMQLRuntimeException;

	/**
	 * Returns the parser tree
	 * 
	 * @return the parser tree
	 */
	public IParserTree getParserTree();

}
