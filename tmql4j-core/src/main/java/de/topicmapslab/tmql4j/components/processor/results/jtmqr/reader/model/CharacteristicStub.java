package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model;

/**
 * abstract class for characteristic stubs
 * @author Christian Haß
 *
 */
public abstract class CharacteristicStub extends ScopedStub {

	/**
	 * value of the characteristic
	 */
	protected String value;
	
	/**
	 * constructor
	 */
	protected CharacteristicStub() {
		this.value = null;
	}
	
	/**
	 * sets the value for the construct
	 * @param value - the value
	 */
	protected void _setValue(String value){
		this.value = value;
	}
	
}
