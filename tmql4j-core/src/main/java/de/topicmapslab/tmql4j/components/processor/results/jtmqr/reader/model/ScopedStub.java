package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model;

import java.util.Collections;
import java.util.Set;

import org.tmapi.core.Topic;

public abstract class ScopedStub extends ReifieableStub {

	protected Set<Topic> scope;
	
	protected ScopedStub() {
		this.scope = Collections.emptySet();
	}
	
	protected void _setScope(Set<Topic> scope){
		this.scope = scope;
	}
	
}
