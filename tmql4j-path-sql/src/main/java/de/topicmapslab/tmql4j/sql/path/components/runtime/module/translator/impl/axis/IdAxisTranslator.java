/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.axis;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.productions.Step;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.ITranslatorContext;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.ITranslatorContext.State;

/**
 * @author Sven Krosse
 * 
 */
public class IdAxisTranslator extends AxisTranslatorImpl {
	
	/**
	 * {@inheritDoc}
	 */
	public ITranslatorContext transform(ITMQLRuntime runtime, Step expression, ITranslatorContext state) throws TMQLRuntimeException {
		return state;
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected String getForward(ITranslatorContext state) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	protected String getBackward(ITranslatorContext state) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	protected State getBackwardState() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	protected State getForwardState() {
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected String getBackwardSelection(ITranslatorContext state) {
		return state.getTopLevelSelections().get(0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected String getForwardSelection(ITranslatorContext state) {
		return state.getTopLevelSelections().get(0);
	}

}
