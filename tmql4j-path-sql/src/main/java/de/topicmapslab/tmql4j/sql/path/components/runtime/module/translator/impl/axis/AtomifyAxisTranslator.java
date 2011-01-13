/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.axis;

import java.text.MessageFormat;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.path.grammar.lexical.MoveForward;
import de.topicmapslab.tmql4j.path.grammar.productions.Step;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.ITranslatorContext;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.ITranslatorContext.State;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TranslaterContext;

/**
 * @author Sven Krosse
 * 
 */
public class AtomifyAxisTranslator extends AxisTranslatorImpl {
	
	static final String SELECTION_FORWARD = "value";
	static final String SELECTION_BACKWARD = "id";
	static final String BACKWARD = "SELECT value FROM names WHERE id IN ( {0} ) UNION SELECT value FROM occurrences WHERE id IN ( {1} )";
	static final String FORWARD = "SELECT id FROM names WHERE value IN ( {0} ) UNION SELECT id FROM occurrences WHERE value IN ( {1} )";

	/**
	 * {@inheritDoc}
	 */
	public ITranslatorContext transform(ITMQLRuntime runtime, Step expression, ITranslatorContext state) throws TMQLRuntimeException {
		Class<? extends IToken> token = expression.getTmqlTokens().get(0);

		final String result;
		final ITranslatorContext.State newState;
		final String selection;
		/*
		 * navigation is forward
		 */
		if (MoveForward.class.equals(token)) {
			result = MessageFormat.format(getForward(state), state.getContextOfCurrentNode(), state.getContextOfCurrentNode());
			newState = getForwardState();
			selection = getForwardSelection(state);
		}
		/*
		 * navigation is backward
		 */
		else {
			result = MessageFormat.format(getBackward(state), state.getContextOfCurrentNode(), state.getContextOfCurrentNode());
			newState = getBackwardState();
			selection = getBackwardSelection(state);
		}
		ITranslatorContext translatorContext = new TranslaterContext(newState, selection);
		translatorContext.setContextOfCurrentNode(result);
		return translatorContext;
	}

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
		return State.CHARACTERISTICS;
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
