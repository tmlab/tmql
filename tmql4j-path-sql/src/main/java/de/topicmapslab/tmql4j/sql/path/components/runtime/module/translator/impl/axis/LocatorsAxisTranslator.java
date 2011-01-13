/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.axis;

import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.ITranslatorContext;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.ITranslatorContext.State;

/**
 * @author Sven Krosse
 * 
 */
public class LocatorsAxisTranslator extends AxisTranslatorImpl {

	static final String SELECTION_FORWARD = "reference";
	static final String SELECTION_BACKWARD = "id_topic";
	static final String BACKWARD = "SELECT id_topic FROM rel_subject_locators, locators WHERE id = id_locator AND reference IN ( {0} )";
	static final String FORWARD = "SELECT reference FROM rel_subject_locators, locators  WHERE id = id_locator AND id_topic IN ( {0} )";

	/**
	 * {@inheritDoc}
	 */
	protected String getForward(ITranslatorContext state) {
		return FORWARD;
	}

	/**
	 * {@inheritDoc}
	 */
	protected String getBackward(ITranslatorContext state) {
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
	
	/**
	 * {@inheritDoc}
	 */
	protected String getBackwardSelection(ITranslatorContext state) {
		return SELECTION_BACKWARD;
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected String getForwardSelection(ITranslatorContext state) {
		return SELECTION_FORWARD;
	}

}
