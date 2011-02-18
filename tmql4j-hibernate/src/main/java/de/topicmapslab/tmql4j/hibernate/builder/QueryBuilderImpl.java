/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.builder;

import de.topicmapslab.tmql4j.components.processor.prepared.IPreparedStatement;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.path.query.TMQLQuery;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * @author Sven Krosse
 *
 */
public abstract class QueryBuilderImpl implements IQueryBuilder {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IQuery toQuery() {
		return new TMQLQuery(null, toQueryString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPreparedStatement toPreparedStatement(ITMQLRuntime runtime) {
		return runtime.preparedStatement(toQueryString());
	}

}
