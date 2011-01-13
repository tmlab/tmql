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
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.IState.State;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.StateImpl;

/**
 * @author Sven Krosse
 * 
 */
public class AtomifyAxisTranslator extends AxisTranslatorImpl {

	static final String BACKWARD = "SELECT value FROM names WHERE id IN ( {0} ) UNION SELECT value FROM occurrences WHERE id IN ( {1} )";
	static final String FORWARD = "SELECT id FROM names WHERE value IN ( {0} ) UNION SELECT id FROM occurrences WHERE value IN ( {1} )";

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
		return State.CHARACTERISTICS;
	}

	/**
	 * {@inheritDoc}
	 */
	protected State getForwardState() {
		return State.STRING;
	}

}
