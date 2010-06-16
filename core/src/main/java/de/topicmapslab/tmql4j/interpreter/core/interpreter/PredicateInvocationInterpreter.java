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
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Locator;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.index.TypeInstanceIndex;

import de.topicmapslab.java.tmdm.TmdmSubjectIdentifier;
import de.topicmapslab.tmql4j.api.exceptions.DataBridgeException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.model.context.IVariableSet;
import de.topicmapslab.tmql4j.parser.core.expressions.PredicateInvocation;
import de.topicmapslab.tmql4j.parser.core.expressions.PredicateInvocationRolePlayerExpression;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * 
 * Special interpreter class to interpret predicate-invocations.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * predicate-invocation ::= anchor ( < anchor : value-expression > [ , ... ] )
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PredicateInvocationInterpreter extends
		ExpressionInterpreterImpl<PredicateInvocation> {

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
	public PredicateInvocationInterpreter(PredicateInvocation ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		final QueryMatches matches = new QueryMatches(runtime);

		TopicMap topicMap = runtime.getTopicMap();

		/*
		 * extract all associations of the specified type
		 */
		Set<Association> associations = HashUtil.getHashSet();
		if (getTokens().get(0).equals(TmdmSubjectIdentifier.TMDM_SUBJECT)) {
			associations.addAll(topicMap.getAssociations());
		} else {
			Construct c = null;
			final String anchor = getTokens().get(0);
			try {
				c = runtime.getDataBridge().getConstructByIdentifier(runtime,
						anchor);
			} catch (DataBridgeException e) {
				logger.warn("Cannot read topic type of assocaition '" + anchor);
			}
			if (c == null || !(c instanceof Topic)) {
				/*
				 * set to variable stack
				 */
				runtime.getRuntimeContext().peek().setValue(
						VariableNames.QUERYMATCHES, matches);
				return;
			}
			TypeInstanceIndex index = topicMap
					.getIndex(TypeInstanceIndex.class);
			if (!index.isOpen()) {
				index.open();
			}
			associations.addAll(index.getAssociations((Topic) c));
		}

		/*
		 * extract all restrictions
		 */
		boolean strict_ = true;
		final Set<Restriction> restrictions = HashUtil.getHashSet();
		for (IExpressionInterpreter<PredicateInvocationRolePlayerExpression> interpreter : getInterpretersFilteredByEypressionType(
				runtime, PredicateInvocationRolePlayerExpression.class)) {

			if (interpreter.getGrammarTypeOfExpression() == PredicateInvocationRolePlayerExpression.TYPE_ELLIPSIS) {
				strict_ = false;
			} else {
				runtime.getRuntimeContext().push();

				interpreter.interpret(runtime);

				IVariableSet set = runtime.getRuntimeContext().peek();
				QueryMatches result = (QueryMatches) set
						.getValue(VariableNames.QUERYMATCHES);
				/*
				 * role identifier or player unknown
				 */
				if (result.isEmpty()) {
					/*
					 * set to variable stack
					 */
					runtime.getRuntimeContext().peek().setValue(
							VariableNames.QUERYMATCHES, matches);
					return;
				}
				Map<String, Object> tuple = result.get(0);

				Restriction restriction = new Restriction();
				restriction.ex = interpreter.getExpression();
				restriction.roleType = tuple.get("$0");
				restriction.player = tuple.get("$1");
				restrictions.add(restriction);
			}
		}

		/*
		 * extract all satisfying associations
		 */
		Set<Thread> threads = HashUtil.getHashSet();
		final boolean strict = strict_;
		for (final Association association : associations) {
			Thread thread = new Thread() {

				public void run() {
					/*
					 * check if predicate is strict
					 */
					if (strict
							&& association.getRoles().size() != restrictions
									.size()) {
						return;
					}

					/*
					 * check if association satisfies all restrictions
					 */
					for (Restriction restriction : restrictions) {
						if (!restriction.satisfy(association)) {
							return;
						}
					}

					/*
					 * extract bindings
					 */
					Map<Restriction, Set<Role>> solutions = HashUtil
							.getHashMap();
					for (Restriction restriction : restrictions) {
						for (Role r : association.getRoles()) {
							boolean playerIsSubject = isTmdmSubject(restriction.player);
							boolean roleIsSubject = isTmdmSubject(restriction.roleType);
							/*
							 * check if player is restricted
							 */
							if (restriction.player instanceof Topic
									&& !playerIsSubject
									&& !r.getPlayer()
											.equals(restriction.player)) {
								continue;
							}

							/*
							 * check if role type is restricted
							 */
							if (restriction.roleType instanceof Topic
									&& !roleIsSubject
									&& !r.getType()
											.equals(restriction.roleType)) {
								continue;
							}

							Set<Role> set = solutions.get(restriction);
							if (set == null) {
								set = HashUtil.getHashSet();
							}
							set.add(r);
							solutions.put(restriction, set);
						}
					}

					if (!solutions.isEmpty()) {
						Map<String, Object> tuple = HashUtil.getHashMap();
						Set<Role> blockedPlayers = HashUtil.getHashSet(), blockedTypes = HashUtil
								.getHashSet();
						Set<Map<String, Object>> tuples = toTuples(solutions,
								tuple, blockedPlayers, blockedTypes);
						synchronized (matches) {
							matches.add(tuples);
						}
					}
				}

				private Set<Map<String, Object>> toTuples(
						Map<Restriction, Set<Role>> values,
						Map<String, Object> tuple, Set<Role> blockedPlayers,
						Set<Role> blockedTypes) {
					Set<Map<String, Object>> tuples = HashUtil.getHashSet();
					Map<Restriction, Set<Role>> values_ = HashUtil
							.getHashMap(values);
					Restriction key = values.keySet().iterator().next();
					values_.remove(key);
					for (Role role : values.get(key)) {
						boolean playerIsSubject = isTmdmSubject(key.player);
						boolean roleIsSubject = isTmdmSubject(key.roleType);
						Map<String, Object> tuple_ = HashUtil.getHashMap(tuple);
						Set<Role> blockedPlayers_ = HashUtil
								.getHashSet(blockedPlayers);
						Set<Role> blockedTypes_ = HashUtil
								.getHashSet(blockedTypes);
						if (key.roleType instanceof Topic && !roleIsSubject) {
							if (blockedTypes_.contains(role)
									|| !role.getType().equals(key.roleType)) {
								continue;
							} else {
								blockedTypes_.add(role);
							}
						} else if (key.roleType instanceof String) {
							if (blockedTypes_.contains(role)) {
								continue;
							}
							tuple_.put((String) key.roleType, role.getType());
							blockedTypes_.add(role);
						}
						if (key.player instanceof Topic && !playerIsSubject) {
							if (blockedPlayers_.contains(role)
									|| !role.getPlayer().equals(key.player)) {
								continue;
							} else {
								blockedPlayers_.add(role);
							}
						} else if (key.player instanceof String) {
							if (blockedPlayers_.contains(role)) {
								continue;
							}
							tuple_.put((String) key.player, role.getPlayer());
							blockedPlayers_.add(role);
						}
						if (values_.isEmpty()) {
							tuples.add(tuple_);
						} else {
							tuples.addAll(toTuples(values_, tuple_,
									blockedPlayers_, blockedTypes_));
						}
					}
					return tuples;
				}
			};

			threads.add(thread);
			thread.run();
		}

		/*
		 * wait until all threads finished
		 */
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (Thread t : threads) {
				if (t.isAlive()) {
					continue;
				}
			}
			break;
		}

		/*
		 * set to variable stack
		 */
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				matches);
	}

	private static boolean isTmdmSubject(Object obj) {
		if (obj instanceof Topic) {
			for (Locator loc : ((Topic) obj).getSubjectIdentifiers()) {
				if (loc.getReference().equalsIgnoreCase(
						TmdmSubjectIdentifier.TMDM_TOPIC_TYPE)) {
					return true;
				}
			}
		}
		return false;
	}

	class Restriction {
		Object player;
		Object roleType;
		IExpression ex;

		boolean satisfy(Association association) {
			if (roleType instanceof Topic && !isTmdmSubject(roleType)) {

				Set<Role> roles = HashUtil.getHashSet();
				roles = association.getRoles((Topic) roleType);

				if (roles.isEmpty()) {
					return false;
				}
				if (player instanceof Topic) {
					if (!isTmdmSubject(player)) {
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
					if (!isTmdmSubject(player)) {
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
