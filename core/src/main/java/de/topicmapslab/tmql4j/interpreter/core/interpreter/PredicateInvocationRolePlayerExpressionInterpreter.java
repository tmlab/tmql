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
package de.topicmapslab.tmql4j.interpreter.core.interpreter;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.Construct;

import de.topicmapslab.tmql4j.api.exceptions.DataBridgeException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.lexer.token.Variable;
import de.topicmapslab.tmql4j.parser.core.expressions.PredicateInvocationRolePlayerExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.ValueExpression;

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
public class PredicateInvocationRolePlayerExpressionInterpreter extends
		ExpressionInterpreterImpl<PredicateInvocationRolePlayerExpression> {

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
	public PredicateInvocationRolePlayerExpressionInterpreter(
			PredicateInvocationRolePlayerExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		/*
		 * Return if expression represents ellipses shortcut
		 */
		if (getGrammarTypeOfExpression() == PredicateInvocationRolePlayerExpression.TYPE_ROLE_PLAYER_COMBINATION) {

			final Object roleType;
			if (containsExpressionsType(de.topicmapslab.tmql4j.parser.core.expressions.Variable.class)) {
				if (runtime.getRuntimeContext().peek().contains(
						getVariables().get(0))) {
					roleType = runtime.getRuntimeContext().peek().getValue(
							getVariables().get(0));
				} else {
					roleType = getVariables().get(0);
				}
			} else {
				/*
				 * Look up role type representing by given anchor
				 */
				Construct construct = null;
				final String anchor = getTokens().get(0);
				try {
					construct = runtime.getDataBridge()
							.getConstructByIdentifier(runtime, anchor);
				} catch (DataBridgeException e) {
					logger.warn("Cannot read topic type of assocaition '"
							+ anchor);
				}
				if (construct == null) {
					/*
					 * set to stack
					 */
					runtime.getRuntimeContext().peek().setValue(
							VariableNames.QUERYMATCHES, HashUtil.getHashMap());
					return;
				}
				roleType = construct;
			}
			final Object player;
			IExpressionInterpreter<ValueExpression> interpreter = getInterpretersFilteredByEypressionType(
					runtime, ValueExpression.class).get(0);
			if (interpreter.getTmqlTokens().get(0).equals(Variable.class)) {
				String var = interpreter.getTokens().get(0);
				if (runtime.getRuntimeContext().peek().contains(var)) {
					player = runtime.getRuntimeContext().peek().getValue(var);
				} else {
					player = var;
				}
			} else {
				QueryMatches players = extractArguments(runtime,
						ValueExpression.class, 0);
				/*
				 * Transform result to mapping between role type an possible
				 * players
				 */
				player = players.getPossibleValuesForVariable().get(0);
			}

			QueryMatches matches = new QueryMatches(runtime);
			Map<String, Object> tuple = HashUtil.getHashMap();
			tuple.put("$0",roleType);
			tuple.put("$1", player);
			matches.add(tuple);

			/*
			 * set to stack
			 */
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.QUERYMATCHES, matches);
		}
	}

}
