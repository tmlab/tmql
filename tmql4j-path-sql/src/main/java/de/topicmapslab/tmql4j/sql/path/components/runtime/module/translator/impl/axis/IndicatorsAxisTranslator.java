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
public class IndicatorsAxisTranslator extends AxisTranslatorImpl {

	public static final String FORWARD = "SELECT reference FROM rel_subject_identifiers, locators  WHERE id = id_locator AND id_topic IN ( {0} )";
	public static final String BACKWARD = "SELECT id_topic FROM rel_subject_identifiers, locators WHERE id = id_locator AND reference IN ( {0} )";

	/**
	 * {@inheritDoc}
	 */
	protected String getForward(IState state) {
		return FORWARD;
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
		return State.TOPIC;
	}

	/**
	 * {@inheritDoc}
	 */
	protected State getForwardState() {
		return State.STRING;
	}

}
