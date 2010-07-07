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
package de.topicmapslab.tmql4j.extension.tmml.interpreter;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.index.TypeInstanceIndex;

import de.topicmapslab.java.tmdm.TmdmSubjectIdentifier;
import de.topicmapslab.tmql4j.api.exceptions.DataBridgeException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.UpdateClause;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.core.base.context.VariableSetImpl;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.PredicateInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.context.IVariableSet;
import de.topicmapslab.tmql4j.lexer.token.Ellipsis;
import de.topicmapslab.tmql4j.parser.core.expressions.FilterPostfix;
import de.topicmapslab.tmql4j.parser.core.expressions.PredicateInvocation;
import de.topicmapslab.tmql4j.parser.core.expressions.PredicateInvocationRolePlayerExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.WhereClause;

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
public class ExtendedPredicateInvocationInterpreter extends
		PredicateInvocationInterpreter {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression to interpret
	 */
	public ExtendedPredicateInvocationInterpreter(PredicateInvocation ex) {
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
		if (getExpression().getParent() instanceof UpdateClause) {
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
		/*
		 * get associations filter by type
		 */
		final String reference = getTokens().get(0);
		Set<Association> associations = HashUtil.getHashSet();
		if (reference.equalsIgnoreCase(TmdmSubjectIdentifier.TMDM_SUBJECT)) {
			associations.addAll(runtime.getTopicMap().getAssociations());
		} else {
			Topic associationType;
			try {
				Construct c = runtime.getDataBridge().getConstructByIdentifier(
						runtime, reference);
				if (c instanceof Topic) {
					associationType = (Topic) c;
					TypeInstanceIndex index = runtime.getTopicMap().getIndex(
							TypeInstanceIndex.class);
					if (!index.isOpen()) {
						index.open();
					}
					associations.addAll(index.getAssociations(associationType));
				} else {
					throw new TMQLRuntimeException(
							"Construct used as association type is not a topic!");
				}
			} catch (DataBridgeException e) {
				// NOTHING TO DO HERE
			}
		}

		/*
		 * check if predicate is strict
		 */
		boolean strict = !getTmqlTokens().contains(Ellipsis.class);
		if (strict) {
			/*
			 * number of expected roles
			 */
			long expectedRoleCount = getExpression().getExpressions().size();
			/*
			 * remove associations with invalid role count
			 */
			for (Association association : HashUtil.getHashSet(associations)) {
				if (association.getRoles().size() != expectedRoleCount) {
					associations.remove(association);
				}
			}
		}

		/*
		 * get role-player conditions
		 */
		QueryMatches rolePlayerConditions[] = extractArguments(runtime,
				PredicateInvocationRolePlayerExpression.class);
		/*
		 * filter all associations does not matching the given
		 * role-player-conditions
		 */
		for (QueryMatches rolePlayerCondition : rolePlayerConditions) {
			if (rolePlayerCondition.isEmpty()) {
				if (!strict) {
					continue;
				} else {
					throw new TMQLRuntimeException(
							"Invalid interpretation of association-definition-part");
				}
			}
			Object roleType = rolePlayerCondition.get(0).get("$0");
			Object player = rolePlayerCondition.get(0).get("$1");
			/*
			 * if role-type or player is null -> topic does not exists
			 */
			if (roleType == null || player == null) {
				/*
				 * empty result, because at least one topic does not exits
				 */
				runtime.getRuntimeContext().peek().setValue(
						VariableNames.QUERYMATCHES, new QueryMatches(runtime));
				return;
			}

			/*
			 * filter by role-type
			 */
			if (roleType instanceof Topic) {
				typeFilter: for (Association association : HashUtil
						.getHashSet(associations)) {
					/*
					 * get roles of the specified type
					 */
					Set<Role> roles = association.getRoles((Topic) roleType);
					/*
					 * check if at least one role exists
					 */
					if (roles.isEmpty()) {
						associations.remove(association);
						continue;
					}
					/*
					 * filter by player
					 */
					if (player instanceof Topic) {
						for (Role role : roles) {
							if (role.getPlayer().equals(player)) {
								continue typeFilter;
							}
						}
						associations.remove(association);
					}
				}
			}
			/*
			 * filter by player
			 */
			else if (player instanceof Topic) {
				typeFilter: for (Association association : HashUtil
						.getHashSet(associations)) {
					for (Role role : association.getRoles()) {
						if (role.getPlayer().equals(player)) {
							continue typeFilter;
						}
					}
					associations.remove(association);
				}
			}
		}

		/*
		 * store of results
		 */
		QueryMatches results = new QueryMatches(runtime);

		/*
		 * convert to tuple sequence
		 */
		for (Association association : associations) {
			Map<String, Object> tuple = HashUtil.getHashMap();
			tuple.put(QueryMatches.getNonScopedVariable(), association);
			results.add(tuple);
		}

		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);
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

		/*
		 * store of update-results
		 */
		QueryMatches results = new QueryMatches(runtime);
		long count = 0;
		IVariableSet set = runtime.getRuntimeContext().peek();
		/*
		 * extract variable bindings
		 */
		final QueryMatches matches;
		if (set.contains(VariableNames.ITERATED_BINDINGS)) {
			matches = (QueryMatches) set
					.getValue(VariableNames.ITERATED_BINDINGS);
		} else {
			matches = new QueryMatches(runtime);
		}

		/*
		 * create the association type
		 */
		final String reference = getTokens().get(0);
		Topic associationType;
		try {
			Construct c = runtime.getDataBridge().getConstructByIdentifier(
					runtime, reference);
			if (c instanceof Topic) {
				associationType = (Topic) c;
			} else {
				throw new TMQLRuntimeException(
						"Construct used as association type is not a topic!");
			}
		} catch (DataBridgeException e) {
			count++;
			associationType = runtime.getTopicMap()
					.createTopicBySubjectIdentifier(
							runtime.getTopicMap().createLocator(
									runtime.getLanguageContext()
											.getPrefixHandler().toAbsoluteIRI(
													reference)));
		}

		/*
		 * no iteration bindings
		 */
		if (matches.isEmpty()) {
			/*
			 * create association
			 */
			Association association = runtime.getTopicMap().createAssociation(
					associationType, new Topic[0]);
			count++;
			/*
			 * get role-player definitions
			 */
			QueryMatches rolePlayerDefinitions[] = extractArguments(runtime,
					PredicateInvocationRolePlayerExpression.class);
			for (QueryMatches rolePlayerDefinition : rolePlayerDefinitions) {
				Object roleType = rolePlayerDefinition.get(0).get("$0");
				if (roleType instanceof String) {
					throw new TMQLRuntimeException("The identifier '"
							+ roleType.toString()
							+ "' is not allowed as role-type");
				}
				Object player = rolePlayerDefinition.get(0).get("$1");
				if (player instanceof String) {
					throw new TMQLRuntimeException("The identifier '"
							+ roleType.toString()
							+ "' is not allowed as player");
				}
				count += (Long) rolePlayerDefinition.get(0).get(
						QueryMatches.getNonScopedVariable());
				/*
				 * create role
				 */
				association.createRole((Topic) roleType, (Topic) player);
			}
		}
		/*
		 * variable binding defined
		 */
		else {
			/*
			 * iterate over all possible bindings
			 */
			for (Map<String, Object> tuple : matches) {
				/*
				 * create association
				 */
				Association association = runtime.getTopicMap()
						.createAssociation(associationType, new Topic[0]);
				count++;
				/*
				 * create variable layer
				 */
				IVariableSet vars = new VariableSetImpl();
				for (Entry<String, Object> entry : tuple.entrySet()) {
					vars.setValue(entry.getKey(), entry.getValue());
				}
				runtime.getRuntimeContext().push(vars);
				/*
				 * get role-player definitions
				 */
				QueryMatches rolePlayerDefinitions[] = extractArguments(
						runtime, PredicateInvocationRolePlayerExpression.class);
				for (QueryMatches rolePlayerDefinition : rolePlayerDefinitions) {
					Topic roleType = (Topic) rolePlayerDefinition.get(0).get(
							"$0");
					if (roleType == null) {
						throw new TMQLRuntimeException(
								"The identifier tm:subject is not allowed as role-type");
					}
					Topic player = (Topic) rolePlayerDefinition.get(0)
							.get("$1");
					if (player == null) {
						throw new TMQLRuntimeException(
								"The identifier tm:subject is not allowed as player");
					}
					count += (Long) rolePlayerDefinition.get(0).get(
							QueryMatches.getNonScopedVariable());
					/*
					 * create role
					 */
					association.createRole(roleType, player);
				}
				runtime.getRuntimeContext().pop();
			}
		}

		/*
		 * create result of update-expression as count of performed changes
		 */
		Map<String, Object> result = HashUtil.getHashMap();
		result.put(QueryMatches.getNonScopedVariable(), count);
		results.add(result);
		/*
		 * set to stack
		 */
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);
	}

}
