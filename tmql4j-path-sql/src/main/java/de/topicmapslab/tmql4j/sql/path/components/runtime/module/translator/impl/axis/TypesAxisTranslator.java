/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.axis;

import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.IState;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.IState.State;

/**
 * @author Sven Krosse
 * 
 */
public class TypesAxisTranslator extends AxisTranslatorImpl {

	static final String FORWARD_TOPIC = "SELECT id_type FROM rel_instance_of WHERE id_instance IN ( {0} )";
	static final String FORWARD_TYPEABLES = "SELECT id_type FROM typeables WHERE id IN ( {0} )";
	static final String BACKWARD = "SELECT id_instance FROM rel_instance_of WHERE id_type IN ( {0} )";

	/**
	 * {@inheritDoc}
	 */
	protected String getForward(IState state) {
		return (state.getState() == IState.State.TOPIC) ? FORWARD_TOPIC : FORWARD_TYPEABLES;
	}

	/**
	 * {@inheritDoc}
	 */
	protected String getBackward(IState state) {
		return BACKWARD;
	}

	/**
	 * {@inheritDoc}
	 */
	protected State getBackwardState() {
		return IState.State.TOPIC;
	}

	/**
	 * {@inheritDoc}
	 */
	protected State getForwardState() {
		return IState.State.TOPIC;
	}

}
