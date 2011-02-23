package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.result;

import de.topicmapslab.tmql4j.components.processor.results.model.Result;
import de.topicmapslab.tmql4j.components.processor.results.model.ResultSet;

/**
 * result class for JTMQR extracted TMQL results
 * @author Christian Haß
 *
 */
public class SimpleJtmqrResult extends Result {

	/**
	 * constructor
	 * @param parent - the parent result set
	 */
	public SimpleJtmqrResult(ResultSet<?> parent) {
		super(parent);
	}

}
