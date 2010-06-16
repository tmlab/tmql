package de.topicmapslab.tmql4j.draft2010.interpreter;

import java.util.Collection;
import java.util.Map;

import org.tmapi.core.Construct;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.draft2010.expressions.SimpleExpression;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.context.IVariableSet;

/**
 * Interpreter implementation of the production 'simple-expression'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SimpleExpressionInterpreter extends
		ExpressionInterpreterImpl<SimpleExpression> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression to interpret
	 */
	public SimpleExpressionInterpreter(SimpleExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		QueryMatches context = new QueryMatches(runtime);

		/*
		 * simple-expression contains variable name
		 */
		if (getGrammarTypeOfExpression() == SimpleExpression.TYPE_VARIABLE) {
			/*
			 * get variable name
			 */
			final String variable = getTokens().get(0);
			/*
			 * extract values of variable from current layer
			 */
			IVariableSet set = runtime.getRuntimeContext().peek();
			if (set.contains(variable)) {
				context = extractVariableValues(runtime, variable, set
						.getValue(variable));
			} else if (set.contains(VariableNames.ITERATED_BINDINGS)) {
				context = extractVariableValues(runtime, variable, set
						.getValue(VariableNames.ITERATED_BINDINGS));
			} else {
				throw new TMQLRuntimeException("Variable '" + variable
						+ "' not bound.");
			}
		}
		/*
		 * simple-expression contains topic-reference
		 */
		else if (getGrammarTypeOfExpression() == SimpleExpression.TYPE_TOPICREF) {
			/*
			 * get topic-reference
			 */
			final String identifier = getTokens().get(0);
			TopicMap topicMap = (TopicMap) runtime.getRuntimeContext().peek()
					.getValue(VariableNames.CURRENT_MAP);
			try {
				/*
				 * try to resolve reference
				 */
				Construct construct = runtime.getDataBridge()
						.getConstructResolver().getConstructByIdentifier(
								runtime, identifier, topicMap);
				if (construct instanceof Topic) {
					Map<String, Object> tuple = HashUtil.getHashMap();
					tuple.put(QueryMatches.getNonScopedVariable(), construct);
					context.add(tuple);
				}
			} catch (Exception e) {
				throw new TMQLRuntimeException("The topic with the reference '"
						+ identifier + "' cannot be found.");
			}
		}
		/*
		 * simple-expression contains only simple dot
		 */
		else if (getGrammarTypeOfExpression() == SimpleExpression.TYPE_DOT) {
			/*
			 * extract values of current node from current layer
			 */
			IVariableSet set = runtime.getRuntimeContext().peek();
			if (set.contains(VariableNames.CURRENT_TUPLE)) {
				Map<String, Object> tuple = HashUtil.getHashMap();
				tuple.put(QueryMatches.getNonScopedVariable(), set
						.getValue(VariableNames.CURRENT_TUPLE));
				context.add(tuple);
			}
			/*
			 * current tuple represented as iterative bindings
			 */
			else if (set.contains(VariableNames.ITERATED_BINDINGS)) {
				context = ((QueryMatches) set
						.getValue(VariableNames.ITERATED_BINDINGS))
						.extractAndRenameBindingsForVariable(QueryMatches
								.getNonScopedVariable());
			} else {
				throw new TMQLRuntimeException("Current tuple is not bound.");
			}
		}
		/*
		 * unknown state
		 */
		else {
			throw new TMQLRuntimeException("Unknown grammar type '"
					+ getGrammarTypeOfExpression()
					+ "' of expression 'simple-expression'.");
		}

		/*
		 * store results
		 */
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				context);

	}

	/**
	 * Method extracts the values of a variable from the given object.
	 * 
	 * @param runtime
	 *            the runtime
	 * @param variable
	 *            the variable name
	 * @param values
	 *            the values of the variable
	 * @return an instance of query-matches representing the variable bindings
	 * @throws TMQLRuntimeException
	 *             thrown if query-matches can not be instantiate
	 */
	private QueryMatches extractVariableValues(final TMQLRuntime runtime,
			final String variable, final Object values)
			throws TMQLRuntimeException {
		/*
		 * create new query matches as context
		 */
		QueryMatches context = new QueryMatches(runtime);
		/*
		 * variable values are represented as collection
		 */
		if (values instanceof Collection<?>) {
			context.convertToTuples((Collection<?>) values);
		}
		/*
		 * variable values are represented as subset of query matches
		 */
		else if (values instanceof QueryMatches) {
			context = ((QueryMatches) values)
					.extractAndRenameBindingsForVariable(variable);
		}
		/*
		 * variable is atomic value
		 */
		else {
			Map<String, Object> tuple = HashUtil.getHashMap();
			tuple.put(QueryMatches.getNonScopedVariable(), values);
			context.add(tuple);
		}
		return context;
	}

}
