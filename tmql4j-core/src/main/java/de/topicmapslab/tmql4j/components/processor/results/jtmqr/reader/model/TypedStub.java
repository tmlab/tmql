package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model;

import org.tmapi.core.Topic;

/**
 * abstract typed stub
 * @author Christian Haß
 *
 */
public abstract class TypedStub extends ConstructStub {

	/**
	 * the type
	 */
	protected Topic type;
	
	/**
	 * constructor
	 */
	protected TypedStub() {
		this.type = null;
	}
	
	/**
	 * sets the type
	 * @param type - the type
	 */
	protected void _setType(Topic type){
		this.type = type;
	}
	
}
