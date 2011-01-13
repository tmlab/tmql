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
public class TypedAxisTranslator extends AxisTranslatorImpl {
	
	static final String FORWARD = "SELECT id FROM typeables WHERE id_type IN ( {0} )";

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
		return TypesAxisTranslator.FORWARD_TYPEABLES;
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
		return IState.State.ANY;
	}

}
