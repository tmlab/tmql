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
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.IState;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.StateImpl;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.IState.State;

/**
 * @author Sven Krosse
 * 
 */
public class TraverseAxisTranslator extends AxisTranslatorImpl {

	static final String FORWARD = "SELECT id_player FROM roles WHERE id_player NOT IN ( {0} ) AND id_parent IN ( SELECT id_parent FROM roles WHERE id_player IN ( {1} ) )";
	static final String BACKWARD = "SELECT id_parent FROM roles WHERE id_parent NOT IN ( {0} ) AND id_player IN ( SELECT id_player FROM roles WHERE id_parent IN ( {1} ) )";

	/**
	 * {@inheritDoc}
	 */
	public IState transform(ITMQLRuntime runtime, Step expression, IState state) throws TMQLRuntimeException {
		Class<? extends IToken> token = expression.getTmqlTokens().get(0);

		final String result;
		final IState.State newState;
		/*
		 * navigation is forward
		 */
		if (MoveForward.class.equals(token)) {
			result = MessageFormat.format(getForward(state), state.getInnerContext(), state.getInnerContext());
			newState = getForwardState();
		}
		/*
		 * navigation is backward
		 */
		else {
			result = MessageFormat.format(getBackward(state), state.getInnerContext(), state.getInnerContext());
			newState = getBackwardState();
		}
		return new StateImpl(newState, result);
	}
	
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
		return State.ASSOCIATION;
	}

	/**
	 * {@inheritDoc}
	 */
	protected State getForwardState() {
		return State.TOPIC;
	}

}
