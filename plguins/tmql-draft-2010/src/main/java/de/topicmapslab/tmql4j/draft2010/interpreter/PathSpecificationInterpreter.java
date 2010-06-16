package de.topicmapslab.tmql4j.draft2010.interpreter;

import java.util.Map;

import org.tmapi.core.Construct;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.draft2010.core.Axis;
import de.topicmapslab.tmql4j.draft2010.core.axis.DefaultAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.expressions.AssociationPattern;
import de.topicmapslab.tmql4j.draft2010.expressions.PathSpecification;
import de.topicmapslab.tmql4j.draft2010.tokens.DoubleColon;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.model.context.IVariableSet;
import de.topicmapslab.tmql4j.lexer.token.Star;

/**
 * Interpreter of an expression of the type {@link PathSpecification}.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PathSpecificationInterpreter extends
		ExpressionInterpreterImpl<PathSpecification> {

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
	@Override
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		/*
		 * is association pattern
		 */
		if (getGrammarTypeOfExpression() == PathSpecification.TYPE_ASSOCIATION_PATTERN) {
			/*
			 * call association-pattern to interpret
			 */
			IExpressionInterpreter<AssociationPattern> interpreter = getInterpretersFilteredByEypressionType(
					runtime, AssociationPattern.class).get(0);
			interpreter.interpret(runtime);
		}
		/*
		 * is normal navigation step
		 */
		else {
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
				if (getTmqlTokens().size() == 3
						&& !getTmqlTokens().get(2).equals(Star.class)) {
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
				/*
				 * get queried topic map
				 */
				TopicMap topicMap = (TopicMap) runtime.getRuntimeContext()
						.peek().getValue(VariableNames.CURRENT_MAP);
				/*
				 * call data-bridge to get topic type
				 */
				try {
					Construct construct = runtime.getDataBridge()
							.getConstructResolver().getConstructByIdentifier(
									runtime, identifier, topicMap);
					if (construct instanceof Topic) {
						type = (Topic) construct;
					}
				} catch (Exception e) {
					throw new TMQLRuntimeException(e);
				}
			}

			/*
			 * extract context from variable layer
			 */
			IVariableSet set = runtime.getRuntimeContext().peek();
			if (!set.contains(VariableNames.ITERATED_BINDINGS)) {
				throw new TMQLRuntimeException("Missing navigation context.");
			}
			QueryMatches context = (QueryMatches) set
					.getValue(VariableNames.ITERATED_BINDINGS);

			/*
			 * iterate over all elements of the context and call the navigation
			 * axis
			 */
			QueryMatches results = new QueryMatches(runtime);
			for (Object o : context.getPossibleValuesForVariable()) {
				if (o instanceof Construct) {					
					/*
					 * convert to query-match
					 */
					for (Object result : axis.navigate((Construct) o, type)) {
						Map<String, Object> tuple = HashUtil.getHashMap();
						tuple.put(QueryMatches.getNonScopedVariable(), result);
						results.add(tuple);
					}
				}
			}

			/*
			 * store results
			 */
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.QUERYMATCHES, results);
		}

	}
}
