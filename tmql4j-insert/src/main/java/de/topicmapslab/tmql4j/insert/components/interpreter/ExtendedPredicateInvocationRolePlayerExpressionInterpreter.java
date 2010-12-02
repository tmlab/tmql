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
package de.topicmapslab.tmql4j.insert.components.interpreter;

import java.util.Map;

import org.tmapi.core.Construct;
import org.tmapi.core.Topic;

import de.topicmapslab.identifier.TmdmSubjectIdentifier;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.exception.DataBridgeException;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.insert.grammar.productions.UpdateClause;
import de.topicmapslab.tmql4j.insert.grammar.productions.WhereClause;
import de.topicmapslab.tmql4j.path.components.interpreter.PredicateInvocationRolePlayerExpressionInterpreter;
import de.topicmapslab.tmql4j.path.grammar.lexical.Ellipsis;
import de.topicmapslab.tmql4j.path.grammar.productions.FilterPostfix;
import de.topicmapslab.tmql4j.path.grammar.productions.PredicateInvocationRolePlayerExpression;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * 
 * Special interpreter class to interpret association-definition part.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * association-definition-part ::=  ( anchor | variable ) : ( anchor | variable )
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ExtendedPredicateInvocationRolePlayerExpressionInterpreter extends
		PredicateInvocationRolePlayerExpressionInterpreter {

	private long count = 0;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public ExtendedPredicateInvocationRolePlayerExpressionInterpreter(
			PredicateInvocationRolePlayerExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		if (getExpression().isChildOf(WhereClause.class)
				&& !getExpression().getVariables().isEmpty()) {
			super.interpret(runtime);
		} else if ( getExpression().isChildOf(FilterPostfix.class)){
			super.interpret(runtime);
		}else {
			extendedInterpret(runtime);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void extendedInterpret(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		if (getExpression().getParent().getParent() instanceof UpdateClause) {
			interpretAsUpdateStream(runtime);
		} else {
			interpretAsCondition(runtime);
		}
	}

	/**
	 * Method is called if the expression is a condition.
	 * 
	 * @param runtime
	 *            the runtime
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation failed
	 */
	private void interpretAsCondition(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		if (getTmqlTokens().get(0).equals(Ellipsis.class)) {
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.QUERYMATCHES, new QueryMatches(runtime));
		} else {
			getDefinitionPart(runtime, false);
		}
	}

	/**
	 * Method is called if the expression is child of a update-clause.
	 * 
	 * @param runtime
	 *            the runtime
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation failed
	 */
	private void interpretAsUpdateStream(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		if (getTmqlTokens().get(0).equals(Ellipsis.class)) {
			throw new TMQLRuntimeException(
					"Ellipsis '...' not allowed as update-definition.");
		}
		getDefinitionPart(runtime, true);
	}

	/**
	 * Internal method to create the role-player-constraint.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param createNotExistings
	 *            flag indicates, if non existing topics should be created
	 */
	private void getDefinitionPart(TMQLRuntime runtime,
			boolean createNotExistings) throws TMQLRuntimeException {
		/*
		 * create result tuple
		 */
		QueryMatches results = new QueryMatches(runtime);
		Map<String, Object> tuple = HashUtil.getHashMap();
		tuple.put("$0", getOrCreateTopic(runtime, getTmqlTokens().get(0),
				getTokens().get(0), createNotExistings));
		tuple.put("$1", getOrCreateTopic(runtime, getTmqlTokens().get(2),
				getTokens().get(2), createNotExistings));
		tuple.put(QueryMatches.getNonScopedVariable(), count);
		results.add(tuple);

		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);
	}

	/**
	 * Method try to find the topic represented by the reference or variable. If
	 * there isn't a topic with this identifier, a new topic will be created
	 * with the reference used as subject-identifier. If the construct isn't a
	 * topic an exception will be thrown. If the reference represent a system
	 * topic, the string will be returned. If the topic does not exits but
	 * should not created, <code>null</code> will be returned.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param reference
	 *            the reference
	 * @param createNotExistings
	 *            flag indicates, if non existing topics should be created
	 * @return the object
	 * @throws TMQLRuntimeException
	 *             thrown if found construct isn't a topic or the variable is
	 *             not set
	 */
	private final Object getOrCreateTopic(TMQLRuntime runtime,
			Class<? extends IToken> token, String reference,
			boolean createNotExistings) throws TMQLRuntimeException {

		/*
		 * do not create a system topic
		 */
		if (reference.equalsIgnoreCase(TmdmSubjectIdentifier.TMDM_SUBJECT)) {
			return reference;
		}

		IVariableSet set = runtime.getRuntimeContext().peek();

		if (token.equals(Variable.class)) {
			if (!set.contains(reference)
					|| !(set.getValue(reference) instanceof Topic)) {
				throw new TMQLRuntimeException("Variable '" + reference
						+ "' is not set or is not a topic!");
			}
			return (Topic) set.getValue(reference);
		}

		try {
			Construct c = runtime.getDataBridge().getConstructByIdentifier(
					runtime, reference);
			if (c instanceof Topic) {
				return (Topic) c;
			}
			throw new TMQLRuntimeException("Construct is not a topic!");
		} catch (DataBridgeException e) {
			count++;
			if (createNotExistings) {
				return runtime.getTopicMap().createTopicBySubjectIdentifier(
						runtime.getTopicMap().createLocator(
								runtime.getLanguageContext().getPrefixHandler()
										.toAbsoluteIRI(reference)));
			}
		}
		return null;
	}

}
