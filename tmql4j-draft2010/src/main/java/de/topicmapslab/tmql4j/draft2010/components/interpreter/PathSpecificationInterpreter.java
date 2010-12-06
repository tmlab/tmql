package de.topicmapslab.tmql4j.draft2010.components.interpreter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.Construct;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.components.navigation.Axis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.DefaultAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.DoubleColon;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Star;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.AssociationPattern;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.PathSpecification;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Interpreter of an expression of the type {@link PathSpecification}.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PathSpecificationInterpreter extends ExpressionInterpreterImpl<PathSpecification> {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression to interpret
	 */
	public PathSpecificationInterpreter(PathSpecification ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {

		/*
		 * is association pattern
		 */
		if (getGrammarTypeOfExpression() == PathSpecification.TYPE_ASSOCIATION_PATTERN) {
			/*
			 * call association-pattern to interpret
			 */
			IExpressionInterpreter<AssociationPattern> interpreter = getInterpretersFilteredByEypressionType(runtime, AssociationPattern.class).get(0);
			return interpreter.interpret(runtime, context, optionalArguments);
		}
		/*
		 * is normal navigation step
		 */
		if (context.getContextBindings() == null) {
			logger.warn("Missing context bindings for navigation step!");
			return QueryMatches.emptyMatches();
		}

		IAxis axis = null;
		String identifier = null;
		/*
		 * contains axis identifier
		 */
		if (getTmqlTokens().contains(DoubleColon.class)) {
			axis = Axis.getAxisForToken(getTmqlTokens().get(0));
			/*
			 * get identifier of topic-reference used as type filter
			 */
			if (getTmqlTokens().size() == 3 && !getTmqlTokens().get(2).equals(Star.class)) {
				identifier = getTokens().get(2);
			}
		} else {
			/*
			 * get identifier of topic-reference used as type filter
			 */
			if (!getTmqlTokens().get(0).equals(Star.class)) {
				identifier = getTokens().get(0);
			}
		}

		/*
		 * set to default axis if not exists or set
		 */
		if (axis == null) {
			axis = new DefaultAxis();
		}

		Topic type = null;
		/*
		 * get topic representing the optional type
		 */
		if (identifier != null) {
			Construct construct = runtime.getConstructResolver().getConstructByIdentifier(context, identifier);
			if (construct instanceof Topic) {
				type = (Topic) construct;
			} else {
				logger.warn("Optional type is not a valid topic reference '" + identifier + "'.");
			}
		}
		/*
		 * iterate over all elements of the context and call the navigation axis
		 */
		List<Object> values = HashUtil.getList();
		for (Object o : context.getContextBindings().getPossibleValuesForVariable()) {
			values.addAll(axis.navigate((Construct) o, type));
		}
		return QueryMatches.asQueryMatchNS(runtime, values.toArray());
	}
}
