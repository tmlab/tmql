/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.components.interpreter;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.PredicateInvocationRolePlayerExpression;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.TmdmSubjectIdentifier;

/**
 * 
 * Special interpreter class to interpret role-player-constraints.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * predicate-invocation-role-player ::= anchor : value-expression | ...
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PredicateInvocationRolePlayerExpressionInterpreter extends ExpressionInterpreterImpl<PredicateInvocationRolePlayerExpression> {

	/**
	 * the Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public PredicateInvocationRolePlayerExpressionInterpreter(PredicateInvocationRolePlayerExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Restriction interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * Return if expression represents ellipses shortcut
		 */
		if (getGrammarTypeOfExpression() == PredicateInvocationRolePlayerExpression.TYPE_ROLE_PLAYER_COMBINATION) {

			final Object roleType;
			List<IExpressionInterpreter<?>> interpreters = getInterpreters(runtime);
			IExpressionInterpreter<?> interpreter = interpreters.get(0);
			if (interpreter instanceof VariableInterpreter) {
				String variable = interpreter.getVariables().get(0);
				if (context.getCurrentTuple() != null && context.getCurrentTuple().containsKey(variable)) {
					roleType = context.getCurrentTuple().get(variable);
				} else {
					roleType = variable;
				}
			} else {
				/*
				 * execute value-expression for role-type
				 */
				QueryMatches matches = interpreter.interpret(runtime, context, optionalArguments);
				if (matches.isEmpty()) {
					logger.warn("Value-expression to fetch role-type return empty result set!");
					return null;
				} else {
					Construct construct = (Construct) matches.get(0).get(QueryMatches.getNonScopedVariable());
					if (construct == null) {
						logger.warn("Value-expression to fetch role-type return empty result set!");
						return null;
					} else {
						roleType = construct;
					}
				}
			}
			final Object player;
			interpreter = interpreters.get(1);
			if (interpreter instanceof VariableInterpreter) {
				String variable = interpreter.getVariables().get(0);
				if (context.getCurrentTuple() != null && context.getCurrentTuple().containsKey(variable)) {
					player = context.getCurrentTuple().get(variable);
				} else {
					player = variable;
				}
			} else {
				QueryMatches matches = interpreter.interpret(runtime, context, optionalArguments);
				if (matches.isEmpty()) {
					logger.warn("Value-expression to fetch player return empty result set!");
					return null;
				} else {
					Construct construct = (Construct) matches.get(0).get(QueryMatches.getNonScopedVariable());
					if (construct == null) {
						logger.warn("Value-expression to fetch player return empty result set!");
						return null;
					} else {
						player = construct;
					}
				}
			}
			Restriction restriction = new Restriction();
			restriction.ex = this.getExpression();
			restriction.roleType = roleType;
			restriction.player = player;
			return restriction;
		}
		logger.warn("Unsupported state of role-player-constraint!");
		return null;
	}

	public class Restriction {
		Object player;
		Object roleType;
		IExpression ex;

		boolean satisfy(Association association) {
			if (roleType instanceof Topic && !TmdmSubjectIdentifier.isTmdmSubject(roleType)) {

				Set<Role> roles = HashUtil.getHashSet();
				roles = association.getRoles((Topic) roleType);

				if (roles.isEmpty()) {
					return false;
				}
				if (player instanceof Topic) {
					if (!TmdmSubjectIdentifier.isTmdmSubject(player)) {
						boolean satisfy = false;
						for (Role r : roles) {
							if (r.getPlayer().equals(player)) {
								satisfy = true;
								break;
							}
						}
						if (!satisfy) {
							return false;
						}
					}
				}

			} else {
				Set<Role> roles = association.getRoles();
				if (roles.isEmpty()) {
					return false;
				}
				if (player instanceof Topic) {
					if (!TmdmSubjectIdentifier.isTmdmSubject(player)) {
						boolean satisfy = false;
						for (Role r : roles) {
							if (r.getPlayer().equals(player)) {
								satisfy = true;
								break;
							}
						}
						if (!satisfy) {
							return false;
						}
					}
				}
			}
			return true;
		}

	}

}
