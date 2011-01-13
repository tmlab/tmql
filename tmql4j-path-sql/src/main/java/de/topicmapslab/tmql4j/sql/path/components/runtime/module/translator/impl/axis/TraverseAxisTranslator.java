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
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TranslaterContext;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.ITranslatorContext.State;

/**
 * @author Sven Krosse
 * 
 */
public class TraverseAxisTranslator extends AxisTranslatorImpl {
	static final String SELECTION_FORWARD = "id_player";
	static final String SELECTION_BACKWARD = "id_parent";
	static final String FORWARD = "SELECT id_player FROM roles WHERE id_player NOT IN ( {0} ) AND id_parent IN ( SELECT id_parent FROM roles WHERE id_player IN ( {1} ) )";
	static final String BACKWARD = "SELECT id_parent FROM roles WHERE id_parent NOT IN ( {0} ) AND id_player IN ( SELECT id_player FROM roles WHERE id_parent IN ( {1} ) )";

	/**
	 * {@inheritDoc}
	 */
	public ITranslatorContext transform(ITMQLRuntime runtime, Step expression, ITranslatorContext state) throws TMQLRuntimeException {
		Class<? extends IToken> token = expression.getTmqlTokens().get(0);

		final String result;
		final String selection;
		final ITranslatorContext.State newState;
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
		return State.ASSOCIATION;
	}

	/**
	 * {@inheritDoc}
	 */
	protected State getForwardState() {
		return State.TOPIC;
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
