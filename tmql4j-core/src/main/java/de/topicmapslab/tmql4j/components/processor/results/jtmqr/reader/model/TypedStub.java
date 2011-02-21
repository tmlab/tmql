package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model;

import org.tmapi.core.Topic;

public abstract class TypedStub {

	protected Topic type;
	
	protected TypedStub() {
		this.type = null;
	}
	
	protected void _setType(Topic type){
		this.type = type;
	}
	
}
