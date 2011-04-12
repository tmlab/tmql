package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.result;

import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.ResultSet;
import de.topicmapslab.tmql4j.components.processor.results.model.ResultType;

/**
 * result set class for JTMQR extracted TMQL result sets
 * @author Christian Haß
 *
 */
public class SimpleJtmqrResultSet extends ResultSet<IResult> {

	/**
	 * constructor
	 */
	public SimpleJtmqrResultSet() {
		super(null, null);
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
	public IResult createResult() {
		return new SimpleJtmqrResult(this);
	}
	
	/**
	 * @throws UnsupportedOperationException
	 */
	@Override
	protected TopicMap getTopicMap() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @throws UnsupportedOperationException
	 */
	@Override
	protected TopicMapSystem getTopicMapSystem() {
		throw new UnsupportedOperationException();
	}
	
}
