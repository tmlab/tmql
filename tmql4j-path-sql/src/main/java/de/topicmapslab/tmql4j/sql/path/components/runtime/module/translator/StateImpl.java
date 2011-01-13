/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator;

/**
 * @author Sven Krosse
 * 
 */
public class StateImpl implements IState {

	private final State state;
	private final String context;

	/**
	 * constructor
	 * 
	 * @param state
	 *            the state
	 * @param context
	 *            the context
	 */
	public StateImpl(final State state, final String context) {
		this.state = state;
		this.context = context;
	}

	/**
	 * {@inheritDoc}
	 */
	public State getState() {
		return state;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getInnerContext() {
		return context;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return context +  "( " + state.name() + " )";
	}

}
