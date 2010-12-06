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

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.util.Restriction;
import de.topicmapslab.tmql4j.update.grammar.productions.PredicateInvocation;
import de.topicmapslab.tmql4j.update.grammar.productions.PredicateInvocationRolePlayerExpression;

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
		return interpretAsUpdateStream(runtime, context, optionalArguments);
	}

	/**
	 * Method is called if the expression is child of a update-clause.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the current querying context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the query matches
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation failed
	 */
	private QueryMatches interpretAsUpdateStream(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {

		/*
		 * store number of updates
		 */
		long count = 0;
		/*
		 * create the association type
		 */
		final String reference = getTokens().get(0);
		TopicMap topicMap = context.getQuery().getTopicMap();
		Topic associationType;

		Construct c = runtime.getConstructResolver().getConstructByIdentifier(context, reference);
		if (c instanceof Topic) {
			associationType = (Topic) c;
		}
		if (c == null) {
			count++;
			associationType = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(runtime.getLanguageContext().getPrefixHandler().toAbsoluteIRI(reference)));
		} else {
			throw new TMQLRuntimeException("Construct used as association type is not a topic!");
		}

		/*
		 * no iteration bindings
		 */
		if (context.getContextBindings() == null) {
			count += createAssociation(runtime, context, topicMap, associationType);
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
				count += createAssociation(runtime, newContext, topicMap, associationType);
			}
		}
		return QueryMatches.asQueryMatchNS(runtime, count);
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
	 * @return the number of created constructs
	 */
	private long createAssociation(ITMQLRuntime runtime, IContext context, TopicMap topicMap, Topic associationType) {
		/*
		 * create association
		 */
		Association association = topicMap.createAssociation(associationType);
		long count = 1;
		/*
		 * get role-player definitions
		 */
		for (IExpressionInterpreter<?> interpreter : getInterpretersFilteredByEypressionType(runtime, PredicateInvocationRolePlayerExpression.class)) {
			Restriction restriction = interpreter.interpret(runtime, context);
			if (restriction == null) {
				logger.warn("Role-Player-Constraint does not fits!");
				return -1;
			}
			Object roleType = restriction.getRoleType();
			if (roleType instanceof String) {
				throw new TMQLRuntimeException("The identifier '" + roleType.toString() + "' is not allowed as role-type");
			}
			Object player = restriction.getPlayer();
			if (player instanceof String) {
				throw new TMQLRuntimeException("The identifier '" + roleType.toString() + "' is not allowed as player");
			}
			/*
			 * create role
			 */
			association.createRole((Topic) roleType, (Topic) player);
			count++;
		}
		return count;
	}

}
