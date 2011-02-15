/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2009 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.update.components.interpreter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.PreparedExpression;
import de.topicmapslab.tmql4j.path.grammar.lexical.Dot;
import de.topicmapslab.tmql4j.path.util.Restriction;
import de.topicmapslab.tmql4j.update.components.results.IUpdateAlias;
import de.topicmapslab.tmql4j.update.grammar.productions.PredicateInvocation;
import de.topicmapslab.tmql4j.update.grammar.productions.PredicateInvocationRolePlayerExpression;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Special implementation of {@link ExpressionInterpreterImpl} representing an
 * interpreter of a association-definition.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * association-definition ::= anchor ( < association-definition-part > )
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PredicateInvocationInterpreter extends ExpressionInterpreterImpl<PredicateInvocation> {

	/**
	 * the Logger
	 */
	private static Logger logger = LoggerFactory.getLogger(PredicateInvocationInterpreter.class.getSimpleName());

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression to interpret
	 */
	public PredicateInvocationInterpreter(PredicateInvocation ex) {
		super(ex);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		QueryMatches results = new QueryMatches(runtime);
		/*
		 * context independent
		 */
		if (context.getContextBindings() == null) {
			interpretAsUpdateStream(runtime, context, results, optionalArguments);
		}
		/*
		 * context dependent
		 */
		else {
			for (Object currentNode : context.getContextBindings().getPossibleValuesForVariable()) {
				Context newContext = new Context(context);
				newContext.setCurrentNode(currentNode);
				newContext.setContextBindings(null);
				interpretAsUpdateStream(runtime, newContext, results, optionalArguments);
			}
		}

		if (results.isEmpty()) {
			return QueryMatches.emptyMatches();
		}
		return results;
	}

	/**
	 * Method is called if the expression is child of a update-clause.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the current querying context
	 * @param results
	 *            the results
	 * @param optionalArguments
	 *            any optional arguments
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation failed
	 */
	@SuppressWarnings("unchecked")
	private void interpretAsUpdateStream(ITMQLRuntime runtime, IContext context, QueryMatches results, Object... optionalArguments) throws TMQLRuntimeException {
		Set<String> topicsIds = HashUtil.getHashSet();
		/*
		 * create the association type
		 */
		Topic associationType;
		TopicMap topicMap = context.getQuery().getTopicMap();
		/*
		 * is wildcard used
		 */
		if (containsExpressionsType(PreparedExpression.class)) {
			QueryMatches matches = extractArguments(runtime, PreparedExpression.class, 0, context, optionalArguments);
			if (matches.isEmpty()) {
				throw new TMQLRuntimeException("Prepared statement has to be bound to a value!");
			}
			Object obj = matches.getFirstValue();
			if (obj instanceof Topic) {
				associationType = (Topic) obj;
			} else if (obj instanceof String) {
				associationType = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, (String) obj);
				if (associationType == null) {
					associationType = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(runtime.getConstructResolver().toAbsoluteIRI(context, (String) obj)));
					topicsIds.add(associationType.getId());
				}
			} else {
				throw new TMQLRuntimeException("Invalid result of prepared statement, expects a topic");
			}
		}
		/*
		 * is string or element
		 */
		else {
			final String reference = getTokens().get(0);
			final Class<? extends IToken> token = getTmqlTokens().get(0);
			if (token.equals(Dot.class)) {
				Object currentNode = context.getCurrentNode();
				if (currentNode instanceof Topic) {
					associationType = (Topic) currentNode;
				} else {
					throw new TMQLRuntimeException("Construct used as association type is not a topic!");
				}
			} else {
				Construct c = runtime.getConstructResolver().getConstructByIdentifier(context, reference);
				if (c instanceof Topic) {
					associationType = (Topic) c;
				} else if (c == null) {
					associationType = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(runtime.getConstructResolver().toAbsoluteIRI(context, reference)));
					topicsIds.add(associationType.getId());
				} else {
					throw new TMQLRuntimeException("Construct used as association type is not a topic!");
				}
			}
		}

		/*
		 * no iteration bindings
		 */
		if (context.getContextBindings() == null) {
			createAssociation(runtime, context, results, topicMap, associationType);
			if (!topicsIds.isEmpty()) {
				Object val = results.getMatches().get(0).get(IUpdateAlias.TOPICS);
				if ( val != null ){
					((Collection<String>)val).addAll(topicsIds);
				}else{
					val = topicsIds;
				}
				results.getMatches().get(0).put(IUpdateAlias.TOPICS, val);
			}
		}
		/*
		 * variable binding defined
		 */
		else {
			/*
			 * iterate over all possible bindings
			 */
			for (Map<String, Object> tuple : context.getContextBindings()) {
				Context newContext = new Context(context);
				newContext.setContextBindings(null);
				newContext.setCurrentTuple(tuple);
				createAssociation(runtime, newContext, results, topicMap, associationType);
				if (!topicsIds.isEmpty()) {
					Object val = results.getMatches().get(results.getMatches().size() - 1).get(IUpdateAlias.TOPICS);
					if ( val != null ){
						((Collection<String>)val).addAll(topicsIds);
					}else{
						val = topicsIds;
					}
					results.getMatches().get(results.getMatches().size() - 1).put(IUpdateAlias.TOPICS, val);
				}
			}
		}

		context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.ASSOCIATIONS);
		context.getTmqlProcessor().getResultProcessor().setColumnAlias(1, IUpdateAlias.ROLES);
		context.getTmqlProcessor().getResultProcessor().setColumnAlias(2, IUpdateAlias.TOPICS);
		context.getTmqlProcessor().getResultProcessor().setAutoReduction(false);
	}

	/**
	 * Utility method to create a new association
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param topicMap
	 *            the topic map
	 * @param associationType
	 *            the association type
	 */
	private void createAssociation(ITMQLRuntime runtime, IContext context, QueryMatches matches, TopicMap topicMap, Topic associationType) {
		List<String> roles = HashUtil.getList();
		List<String> topics = HashUtil.getList();
		/*
		 * create association
		 */
		Association association = topicMap.createAssociation(associationType);	
		/*
		 * get role-player definitions
		 */
		for (IExpressionInterpreter<?> interpreter : getInterpretersFilteredByEypressionType(runtime, PredicateInvocationRolePlayerExpression.class)) {
			Restriction restriction = interpreter.interpret(runtime, context);
			if (restriction == null) {
				logger.warn("Role-Player-Constraint does not fits!");
			}
			Object roleType = restriction.getRoleType();
			if (roleType instanceof String) {
				throw new TMQLRuntimeException("The identifier '" + roleType.toString() + "' is not allowed as role-type");
			}
			if ( restriction.isNewRoleType()){
				topics.add(((Topic)roleType).getId());
			}
			Object player = restriction.getPlayer();
			if (player instanceof String) {
				throw new TMQLRuntimeException("The identifier '" + roleType.toString() + "' is not allowed as player");
			}
			if ( restriction.isNewPlayer()){
				topics.add(((Topic)player).getId());
			}
			/*
			 * create role
			 */
			Role role = association.createRole((Topic) roleType, (Topic) player);
			roles.add(role.getId());
			
		}
		Map<String, Object> tuple = HashUtil.getHashMap();
		tuple.put(IUpdateAlias.ASSOCIATIONS, association.getId());
		tuple.put(IUpdateAlias.ROLES, roles);
		if ( !topics.isEmpty()){
			tuple.put(IUpdateAlias.TOPICS, topics);
		}
		matches.add(tuple);
	}
}
