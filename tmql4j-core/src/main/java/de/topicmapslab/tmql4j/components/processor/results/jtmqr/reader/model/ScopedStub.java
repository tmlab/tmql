package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model;

import java.util.Collections;
import java.util.Set;

import org.tmapi.core.Topic;

/**
 * abstract scoped stub
 * @author Christian Haß
 */
public abstract class ScopedStub extends ReifieableStub {

	/**
	 * the scope
	 */
	protected Set<Topic> scope;
	
	/**
	 * constructor
	 */
	protected ScopedStub() {
		this.scope = Collections.emptySet();
	}
	
	/**
	 * sets the scope
	 * @param scope - the scope
	 */
	protected void _setScope(Set<Topic> scope){
		this.scope = scope;
	}
	
}
