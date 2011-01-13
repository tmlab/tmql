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
import java.util.Map;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisAtomify;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisCharacteristics;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisId;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisIndicators;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisInstances;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisItem;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisLocators;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisPlayers;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisReifier;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisRoles;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisScope;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisSubtypes;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisSupertypes;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisTraverse;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisTyped;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisTypes;
import de.topicmapslab.tmql4j.path.grammar.lexical.MoveForward;
import de.topicmapslab.tmql4j.path.grammar.productions.Step;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.IState;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.StateImpl;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TmqlSqlTranslatorImpl;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public abstract class AxisTranslatorImpl extends TmqlSqlTranslatorImpl<Step> {

	/**
	 * {@inheritDoc}
	 */
	public IState transform(ITMQLRuntime runtime, IContext context, IExpression expression, IState state) throws TMQLRuntimeException {
		Class<? extends IToken> token = expression.getTmqlTokens().get(0);

		final String result;
		final IState.State newState;
		/*
		 * navigation is forward
		 */
		if (MoveForward.class.equals(token)) {
			result = MessageFormat.format(getForward(state), state.getInnerContext());
			newState = getForwardState();
		}
		/*
		 * navigation is backward
		 */
		else {
			result = MessageFormat.format(getBackward(state), state.getInnerContext());
			newState = getBackwardState();
		}
		return new StateImpl(newState, result);
	}

	/**
	 * Returns the message format of forward navigation
	 * 
	 * @param state
	 *            the current state
	 * @return the message
	 */
	protected abstract String getForward(IState state);

	/**
	 * Returns the message format of backward navigation
	 * 
	 * @param state
	 *            the current state
	 * @return the message
	 */
	protected abstract String getBackward(IState state);

	/**
	 * Returns the result state after forward navigation
	 * 
	 * @return the state
	 */
	protected abstract IState.State getForwardState();

	/**
	 * Returns the result state after backward navigation
	 * 
	 * @return the state
	 */
	protected abstract IState.State getBackwardState();

	private static Map<Class<? extends IToken>, AxisTranslatorImpl> translators = HashUtil.getHashMap();
	static {
		translators.put(AxisAtomify.class, new AtomifyAxisTranslator());
		translators.put(AxisCharacteristics.class, new CharacteristicsAxisTranslator());
		translators.put(AxisId.class, new IdAxisTranslator());
		translators.put(AxisIndicators.class, new IndicatorsAxisTranslator());
		translators.put(AxisInstances.class, new InstancesAxisTranslator());
		translators.put(AxisItem.class, new ItemAxisTranslator());
		translators.put(AxisLocators.class, new LocatorsAxisTranslator());
		translators.put(AxisPlayers.class, new PlayersAxisTranslator());
		translators.put(AxisReifier.class, new ReifierAxisTranslator());
		translators.put(AxisRoles.class, new RolesAxisTranslator());
		translators.put(AxisScope.class, new ScopeAxisTranslator());
		translators.put(AxisSubtypes.class, new SubtypesAxisTranslator());
		translators.put(AxisSupertypes.class, new SupertypesAxisTranslator());
		translators.put(AxisTraverse.class, new TraverseAxisTranslator());
		translators.put(AxisTyped.class, new TypedAxisTranslator());
		translators.put(AxisTypes.class, new TypesAxisTranslator());
	}

	/**
	 * Returns the matching translator for the given step navigation axis
	 * content
	 * 
	 * @param runtime
	 *            the runtime
	 * @param expression
	 *            the expression
	 * @return the translator
	 */
	public static AxisTranslatorImpl getTranslator(final ITMQLRuntime runtime, Step expression ){
		Class<? extends IToken> token = expression.getTmqlTokens().get(1);
		return translators.get(token);
	}
}
