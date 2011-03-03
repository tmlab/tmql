/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2010.grammar.functions.topicmap;

import java.util.Set;

import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.components.interpreter.AssociationPatternInterpeter;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.FunctionImpl;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class AssociationPatternFct extends FunctionImpl {

	public static final String IDENTIFIER = "association-pattern";

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 3;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return IDENTIFIER;
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, IExpressionInterpreter<?> caller) {
		/*
		 * get arguments
		 */
		QueryMatches[] arguments = getParameters(runtime, context, caller);
		/*
		 * iterate over arguments
		 */
		Set<Topic> result = HashUtil.getHashSet();
		/*
		 * use default association type
		 */
		if (arguments[0].isEmpty()) {
			result.addAll(forAssociationType(runtime, context, arguments, null));
		}
		/*
		 * is association type contained
		 */
		for (Object obj : arguments[0].getPossibleValuesForVariable()) {
			if (obj instanceof Topic) {
				result.addAll(forAssociationType(runtime, context, arguments, (Topic) obj));
			}
		}		
		/*
		 * create result
		 */
		if (result.isEmpty()) {
			return QueryMatches.emptyMatches();
		}
		return QueryMatches.asQueryMatchNS(runtime, result);
	}

	private Set<Topic> forAssociationType(ITMQLRuntime runtime, IContext context, QueryMatches[] arguments, Topic associationType) {
		Set<Topic> topics = HashUtil.getHashSet();
		if (arguments[1].isEmpty()) {
			topics.addAll(forRoleType(runtime, context, arguments, associationType, null));
		}
		for (Object obj : arguments[1].getPossibleValuesForVariable()) {
			if (obj instanceof Topic) {
				topics.addAll(forRoleType(runtime, context, arguments, associationType, (Topic) obj));
			}
		}
		return topics;
	}

	private Set<Topic> forRoleType(ITMQLRuntime runtime, IContext context, QueryMatches[] arguments, Topic associationType, Topic roleType) {
		Set<Topic> topics = HashUtil.getHashSet();
		if (arguments[2].isEmpty()) {
			topics.addAll(forOtherRoleType(runtime, context, associationType, roleType, null));
		}
		for (Object obj : arguments[2].getPossibleValuesForVariable()) {
			if (obj instanceof Topic) {
				topics.addAll(forOtherRoleType(runtime, context, associationType, roleType, (Topic) obj));
			}
		}
		return topics;
	}

	private Set<Topic> forOtherRoleType(ITMQLRuntime runtime, IContext context, Topic associationType, Topic roleType, Topic otherRoleType) {
		Set<Topic> topics = HashUtil.getHashSet();
		for (Object o : context.getContextBindings().getPossibleValuesForVariable()) {
			if (o instanceof Topic) {
				topics.addAll(AssociationPatternInterpeter.getTraversalPlayers((Topic) o, associationType, roleType, otherRoleType));
			}
		}
		return topics;
	}

}
