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
package de.topicmapslab.tmql4j.draft2011.path.components.interpreter;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.index.TypeInstanceIndex;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.FilterPostfix;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.PredicateInvocation;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.PredicateInvocationRolePlayerExpression;
import de.topicmapslab.tmql4j.draft2011.path.util.Restriction;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.PreparedExpression;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.TmdmSubjectIdentifier;

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
public class PredicateInvocationInterpreter extends ExpressionInterpreterImpl<PredicateInvocation> {

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
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		TopicMap topicMap = context.getQuery().getTopicMap();
		Set<Association> associations = HashUtil.getHashSet();
		/*
		 * is wildcard as type
		 */
		if (containsExpressionsType(PreparedExpression.class)) {
			QueryMatches matches = extractArguments(runtime, PreparedExpression.class, 0, context, optionalArguments);
			if (matches.isEmpty()) {
				throw new TMQLRuntimeException("Prepared statement has to be bound to a value!");
			}
			Object obj = matches.getFirstValue();
			if (obj instanceof Topic) {
				TypeInstanceIndex index = topicMap.getIndex(TypeInstanceIndex.class);
				if (!index.isOpen()) {
					index.open();
				}
				associations.addAll(index.getAssociations((Topic) obj));
			} else {
				throw new TMQLRuntimeException("Invalid result of prepared statement, expects a topic");
			}
		}
		/*
		 * extract all associations of the specified type
		 */
		else {
			final String anchor = getTokens().get(0);
			if (anchor.equals(TmdmSubjectIdentifier.TM_SUBJECT)) {
				associations.addAll(topicMap.getAssociations());
			} else {
				Construct c = runtime.getConstructResolver().getConstructByIdentifier(context, anchor);
				/*
				 * association type is unknown
				 */
				if (c == null) {
					logger.warn("Cannot read topic type of assocaition '" + anchor);
					return QueryMatches.emptyMatches();
				}
				TypeInstanceIndex index = topicMap.getIndex(TypeInstanceIndex.class);
				if (!index.isOpen()) {
					index.open();
				}
				associations.addAll(index.getAssociations((Topic) c));
			}
		}

		/*
		 * extract all restrictions
		 */
		boolean strict_ = true;
		final Set<Restriction> restrictions = HashUtil.getHashSet();
		for (IExpressionInterpreter<PredicateInvocationRolePlayerExpression> interpreter : getInterpretersFilteredByEypressionType(runtime, PredicateInvocationRolePlayerExpression.class)) {

			/*
			 * is ellipsis token
			 */
			if (interpreter.getGrammarTypeOfExpression() == PredicateInvocationRolePlayerExpression.TYPE_ELLIPSIS) {
				strict_ = false;
			}
			/*
			 * is any restriction
			 */
			else {
				Restriction restriction = interpreter.interpret(runtime, context, optionalArguments);
				if (restriction == null) {
					return QueryMatches.emptyMatches();
				}
				restrictions.add(restriction);
			}
		}

		/*
		 * extract all satisfying associations
		 */
		ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(4 * Runtime.getRuntime().availableProcessors());
		final boolean strict = strict_;
		final QueryMatches matches = new QueryMatches(runtime);
		for (final Association association : associations) {
			Thread thread = new Thread() {

				public void run() {
					/*
					 * check if predicate is strict
					 */
					if (strict && association.getRoles().size() != restrictions.size()) {
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
					Map<Restriction, Set<Role>> solutions = HashUtil.getHashMap();
					for (Restriction restriction : restrictions) {
						for (Role r : association.getRoles()) {
							boolean playerIsSubject = TmdmSubjectIdentifier.isTmdmSubject(restriction.getPlayer());
							boolean roleIsSubject = TmdmSubjectIdentifier.isTmdmSubject(restriction.getRoleType());
							/*
							 * check if player is restricted
							 */
							if (restriction.getPlayer() instanceof Topic && !playerIsSubject && !r.getPlayer().equals(restriction.getPlayer())) {
								continue;
							}

							/*
							 * check if role type is restricted
							 */
							if (restriction.getRoleType() instanceof Topic && !roleIsSubject && !r.getType().equals(restriction.getRoleType())) {
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
						Set<Role> blockedPlayers = HashUtil.getHashSet(), blockedTypes = HashUtil.getHashSet();
						Set<Map<String, Object>> tuples = toTuples(association, solutions, tuple, blockedPlayers, blockedTypes);
						synchronized (matches) {
							matches.add(tuples);
						}
					}
				}

				private Set<Map<String, Object>> toTuples(Association association, Map<Restriction, Set<Role>> values, Map<String, Object> tuple, Set<Role> blockedPlayers, Set<Role> blockedTypes) {
					Set<Map<String, Object>> tuples = HashUtil.getHashSet();
					Map<Restriction, Set<Role>> values_ = HashUtil.getHashMap(values);
					Restriction key = values.keySet().iterator().next();
					values_.remove(key);
					for (Role role : values.get(key)) {
						boolean playerIsSubject = TmdmSubjectIdentifier.isTmdmSubject(key.getPlayer());
						boolean roleIsSubject = TmdmSubjectIdentifier.isTmdmSubject(key.getRoleType());
						Map<String, Object> tuple_ = HashUtil.getHashMap(tuple);
						Set<Role> blockedPlayers_ = HashUtil.getHashSet(blockedPlayers);
						Set<Role> blockedTypes_ = HashUtil.getHashSet(blockedTypes);
						if (key.getRoleType() instanceof Topic && !roleIsSubject) {
							if (blockedTypes_.contains(role) || !role.getType().equals(key.getRoleType())) {
								continue;
							} else {
								blockedTypes_.add(role);
							}
						} else if (key.getRoleType() instanceof String && !roleIsSubject) {
							if (blockedTypes_.contains(role)) {
								continue;
							}
							tuple_.put((String) key.getRoleType(), role.getType());
							blockedTypes_.add(role);
						}
						if (key.getPlayer() instanceof Topic && !playerIsSubject) {
							if (blockedPlayers_.contains(role) || !role.getPlayer().equals(key.getPlayer())) {
								continue;
							} else {
								blockedPlayers_.add(role);
							}
						} else if (key.getPlayer() instanceof String && !playerIsSubject) {
							if (blockedPlayers_.contains(role)) {
								continue;
							}
							tuple_.put((String) key.getPlayer(), role.getPlayer());
							blockedPlayers_.add(role);
						}
						if (values_.isEmpty()) {
							/*
							 * add the association also if the parent is not a
							 * filter
							 */
							if (!getExpression().isChildOf(FilterPostfix.class) && getVariables().isEmpty()) {
								tuple_.put(QueryMatches.getNonScopedVariable(), association);
							}
							tuples.add(tuple_);
						} else {
							tuples.addAll(toTuples(association, values_, tuple_, blockedPlayers_, blockedTypes_));
						}
					}
					return tuples;
				}
			};
			threadPool.execute(thread);
		}
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/*
		 * wait until all threads finished
		 */
		while (threadPool.getActiveCount() > 0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		threadPool.shutdown();
		return matches;
	}

}
