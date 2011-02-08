/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.axis;

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
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
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
	public ISqlDefinition toSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		Class<? extends IToken> token = expression.getTmqlTokens().get(0);
		String optionalType = null;
		if (expression.getTmqlTokens().size() > 2) {
			optionalType = expression.getTokens().get(2);
		}
		/*
		 * navigation is forward
		 */
		if (MoveForward.class.equals(token)) {
			return forward(runtime, context, optionalType, definition);
		}
		/*
		 * navigation is backward
		 */
		return backward(runtime, context, optionalType, definition);
	}

	/**
	 * Calling the implementations to transform the given forward navigation of
	 * this axis with the optional type to an SQL definition
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param optionalType
	 *            the optional type or <code>null</code>
	 * @param definition
	 *            the incoming SQL definition
	 * @return the outgoing SQL definition
	 * @throws TMQLRuntimeException
	 *             thrown if anything fails
	 */
	protected abstract ISqlDefinition forward(ITMQLRuntime runtime, IContext context, final String optionalType, ISqlDefinition definition) throws TMQLRuntimeException;

	/**
	 * Calling the implementations to transform the given backward navigation of
	 * this axis with the optional type to an SQL definition
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param optionalType
	 *            the optional type or <code>null</code>
	 * @param definition
	 *            the incoming SQL definition
	 * @return the outgoing SQL definition
	 * @throws TMQLRuntimeException
	 *             thrown if anything fails
	 */
	protected abstract ISqlDefinition backward(ITMQLRuntime runtime, IContext context, final String optionalType, ISqlDefinition definition) throws TMQLRuntimeException;

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
	public static AxisTranslatorImpl getTranslator(final ITMQLRuntime runtime, Step expression) {
		Class<? extends IToken> token = expression.getTmqlTokens().get(1);
		return translators.get(token);
	}
}
