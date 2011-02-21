package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model;

public abstract class CharacteristicStub extends ScopedStub {

	protected String value;
	
	protected CharacteristicStub() {
		this.value = null;
	}
	
	protected void _setValue(String value){
		this.value = value;
	}
	
}
