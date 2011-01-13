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
public class TypedAxisTranslator extends AxisTranslatorImpl {
	
	static final String SELECTION_FORWARD = "id";
	
	static final String FORWARD = "SELECT id FROM typeables WHERE id_type IN ( {0} )";

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
		return TypesAxisTranslator.FORWARD_TYPEABLES;
	}

	/**
	 * {@inheritDoc}
	 */
	protected State getBackwardState() {
		return ITranslatorContext.State.TOPIC;
	}

	/**
	 * {@inheritDoc}
	 */
	protected State getForwardState() {
		return ITranslatorContext.State.ANY;
	}

	
	/**
	 * {@inheritDoc}
	 */
	protected String getBackwardSelection(ITranslatorContext state) {
		return TypesAxisTranslator.SELECTION_FORWARD;
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected String getForwardSelection(ITranslatorContext state) {
		return SELECTION_FORWARD;
	}
}
