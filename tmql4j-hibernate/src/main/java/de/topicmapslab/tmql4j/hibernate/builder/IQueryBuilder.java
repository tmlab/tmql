/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.builder;

import de.topicmapslab.tmql4j.components.processor.prepared.IPreparedStatement;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * @author Sven Krosse
 * 
 */
public interface IQueryBuilder extends  Cloneable {

	/**
	 * Returns the internal query as query
	 * 
	 * @return the query
	 */
	public IQuery toQuery();

	/**
	 * Returns the internal query as string
	 * 
	 * @return the query string
	 */
	public String toQueryString();

	/**
	 * Returns the internal query as prepared statement
	 * 
	 * @param runtime
	 *            the runtime
	 * @return the statement
	 */
	public IPreparedStatement toPreparedStatement(ITMQLRuntime runtime);
	
	/**
	  * {@inheritDoc}
	  */
	public IQueryBuilder clone() throws CloneNotSupportedException;
}
