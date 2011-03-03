package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model;

import org.tmapi.core.Topic;

/**
 * abstract reifier stub
 * @author Christian Haß
 *
 */
public abstract class ReifieableStub extends TypedStub {

	/**
	 * the reifier
	 */
	protected Topic reifier;
	
	/**
	 * constructor
	 */
	protected ReifieableStub() {
		this.reifier = null;
	}
	
	/**
	 * sets the reifier
	 * @param reifier - the reifier
	 */
	protected void _setReifier(Topic reifier){
		this.reifier = reifier;
	}
	
}
