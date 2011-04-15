package de.topicmapslab.tmql4j.draft2011.path.components.interpreter;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.Association;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.index.TypeInstanceIndex;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.SubtypesAxis;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.Anchor;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.AssociationPattern;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.AssociationPatternRolePart;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.FilterPostfix;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * The interpreter implementation of the production 'association-pattern'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class AssociationPatternInterpeter extends ExpressionInterpreterImpl<AssociationPattern> {

	/**
	 * the logger
	 */
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression to interpret
	 */
	public AssociationPatternInterpeter(AssociationPattern ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		if (context.getCurrentNode() == null) {
			logger.warn("Missing context to execute association pattern!");
			return QueryMatches.emptyMatches();
		}

		QueryMatches matches = extractArguments(runtime, Anchor.class, 0, context, optionalArguments);
		if (matches.isEmpty()) {
			logger.warn("Missing association type to execute association pattern!");
			return QueryMatches.emptyMatches();
		}
		/*
		 * get all associations of type
		 */
		Set<Association> associations = HashUtil.getHashSet();
		SubtypesAxis axis = new SubtypesAxis();
		TopicMap topicMap = context.getQuery().getTopicMap();
		TypeInstanceIndex index = topicMap.getIndex(TypeInstanceIndex.class);
		if (!index.isOpen()) {
			index.open();
		}
		for (Object o : matches.getPossibleValuesForVariable()) {
			if (o instanceof Topic) {
				associations.addAll(index.getAssociations((Topic) o));
				if (context.isTransitive()) {
					for (Object o2 : axis.navigate(context, o, null)) {
						if (o2 instanceof Topic) {
							associations.addAll(index.getAssociations((Topic) o2));
						}
					}
				}
			}
		}
		if (associations.isEmpty()) {
			return QueryMatches.emptyMatches();
		}

		/*
		 * handle filter parts
		 */
		Context newContext = new Context(context);
		newContext.setContextBindings(QueryMatches.asQueryMatchNS(runtime, associations));
		for (IExpressionInterpreter<FilterPostfix> interpreter : getInterpretersFilteredByEypressionType(runtime, FilterPostfix.class)) {
			QueryMatches iteration = interpreter.interpret(runtime, newContext, optionalArguments);
			if (iteration.isEmpty()) {
				return iteration;
			}
			newContext.setContextBindings(iteration);
		}
		/*
		 * filter by first role-type and input player
		 */
		matches = extractArguments(runtime, AssociationPatternRolePart.class, 0, newContext, Association.class);

		if (matches.isEmpty()) {
			return QueryMatches.emptyMatches();
		}
		/*
		 * get players of second role type
		 */
		newContext.setContextBindings(matches);
		return extractArguments(runtime, AssociationPatternRolePart.class, 1, newContext, Topic.class);
	}

}
