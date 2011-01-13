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
public class SubtypesAxisTranslator extends AxisTranslatorImpl {

	/**
	 * {@inheritDoc}
	 */
	protected String getBackward(IState state) {
		return SupertypesAxisTranslator.FORWARD;
	}

	/**
	 * {@inheritDoc}
	 */
	protected State getBackwardState() {
		return State.TOPIC;
	}

	/**
	 * {@inheritDoc}
	 */
	protected String getForward(IState state) {
		return SupertypesAxisTranslator.BACKWARD;
	}

	/**
	 * {@inheritDoc}
	 */
	protected State getForwardState() {
		return State.TOPIC;
	}

}
