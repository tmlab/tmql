package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model;

import org.tmapi.core.Topic;

public abstract class ReifieableStub extends TypedStub {

	protected Topic reifier;
	
	protected ReifieableStub() {
		this.reifier = null;
	}
	
	protected void _setReifier(Topic reifier){
		this.reifier = reifier;
	}
	
}
