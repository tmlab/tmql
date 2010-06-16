package de.topicmapslab.tmql4j.draft2010.interpreter;

import java.util.Map;
import java.util.Map.Entry;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.draft2010.expressions.BooleanExpression;
import de.topicmapslab.tmql4j.draft2010.expressions.ComparisonExpression;
import de.topicmapslab.tmql4j.draft2010.expressions.Expression;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.model.context.IVariableSet;

/**
 * Interpreter implementation of production 'boolean-expression' (
 * {@link BooleanExpression} )
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class BooleanExpressionInterpeter extends
		ExpressionInterpreterImpl<BooleanExpression> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression to interpret
	 */
	public BooleanExpressionInterpeter(BooleanExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		QueryMatches results = null;
		/*
		 * switch by grammar type
		 */
		if (getGrammarTypeOfExpression() == BooleanExpression.TYPE_CONJUNCTION) {
			results = conjunction(runtime);
		} else if (getGrammarTypeOfExpression() == BooleanExpression.TYPE_DISJUNCTION) {
			results = disjunction(runtime);
		} else if (getGrammarTypeOfExpression() == BooleanExpression.TYPE_NOTEXPRESSION) {
			results = negation(runtime);
		} else if (getGrammarTypeOfExpression() == BooleanExpression.TYPE_COMPARISIONEXPRESSION) {
			results = comparison(runtime);
		} else if (getGrammarTypeOfExpression() == BooleanExpression.TYPE_PARETHETIC) {
			results = parenthetics(runtime);
		} else if (getGrammarTypeOfExpression() == BooleanExpression.TYPE_EXPRESSION) {
			results = expression(runtime);
		} else {
			throw new TMQLRuntimeException("Invalid grammar type '"
					+ getGrammarTypeOfExpression()
					+ "' of production 'boolean-expression'.");
		}

		/*
		 * store results
		 */
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);
	}

	/**
	 * Interpretation method of grammar type
	 * {@link BooleanExpression#TYPE_CONJUNCTION}
	 * 
	 * @param runtime
	 *            the runtime
	 * @return the result of interpretation
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches conjunction(final TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * get interpretation context
		 */
		IVariableSet set = runtime.getRuntimeContext().peek();
		if (!set.contains(VariableNames.ITERATED_BINDINGS)) {
			throw new TMQLRuntimeException(
					"Missing context for interpretation of conjunction");
		}

		QueryMatches context = (QueryMatches) set
				.getValue(VariableNames.ITERATED_BINDINGS);

		/*
		 * interpret boolean-expressions
		 */
		for (IExpressionInterpreter<BooleanExpression> interpreters : getInterpretersFilteredByEypressionType(
				runtime, BooleanExpression.class)) {
			/*
			 * create new variable layer
			 */
			runtime.getRuntimeContext().push();
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.ITERATED_BINDINGS, context);

			interpreters.interpret(runtime);

			/*
			 * extract and check results
			 */
			set = runtime.getRuntimeContext().pop();
			if (!set.contains(VariableNames.QUERYMATCHES)) {
				throw new TMQLRuntimeException(
						"Missing result of interpretation of  boolean expression");
			}
			context = (QueryMatches) set.getValue(VariableNames.QUERYMATCHES);
		}

		return context;
	}

	/**
	 * Interpretation method of grammar type
	 * {@link BooleanExpression#TYPE_DISJUNCTION}
	 * 
	 * @param runtime
	 *            the runtime
	 * @return the result of interpretation
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches disjunction(final TMQLRuntime runtime)
			throws TMQLRuntimeException {

		QueryMatches results = new QueryMatches(runtime);

		/*
		 * get interpretation context
		 */
		IVariableSet set = runtime.getRuntimeContext().peek();
		if (!set.contains(VariableNames.ITERATED_BINDINGS)) {
			throw new TMQLRuntimeException(
					"Missing context for interpretation of conjunction");
		}

		/*
		 * interpret boolean-expressions
		 */
		for (IExpressionInterpreter<BooleanExpression> interpreters : getInterpretersFilteredByEypressionType(
				runtime, BooleanExpression.class)) {
			/*
			 * create new variable layer
			 */
			runtime.getRuntimeContext().push();

			interpreters.interpret(runtime);

			/*
			 * extract and check results
			 */
			set = runtime.getRuntimeContext().pop();
			if (!set.contains(VariableNames.QUERYMATCHES)) {
				throw new TMQLRuntimeException(
						"Missing result of interpretation of boolean expression");
			}
			QueryMatches context = (QueryMatches) set
					.getValue(VariableNames.QUERYMATCHES);
			/*
			 * handle disjunction
			 */
			if (results.isEmpty()) {
				results.add(context);
				results.getNegation().add(context.getNegation());
			} else {
				results.disjunction(context);
				results.getNegation().conjunction(context.getNegation());
			}

		}

		return results;
	}

	/**
	 * Interpretation method of grammar type
	 * {@link BooleanExpression#TYPE_NOTEXPRESSION}
	 * 
	 * @param runtime
	 *            the runtime
	 * @return the result of interpretation
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches negation(final TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * extract and check results
		 */
		IVariableSet set = runtime.getRuntimeContext().peek();
		if (!set.contains(VariableNames.ITERATED_BINDINGS)) {
			throw new TMQLRuntimeException(
					"Missing context for interpretation of negation");
		}

		QueryMatches context = (QueryMatches) set
				.getValue(VariableNames.ITERATED_BINDINGS);

		/*
		 * interpret boolean-expression
		 */
		IExpressionInterpreter<BooleanExpression> interpreters = getInterpretersFilteredByEypressionType(
				runtime, BooleanExpression.class).get(0);
		/*
		 * create new variable layer
		 */
		runtime.getRuntimeContext().push();
		runtime.getRuntimeContext().peek().setValue(
				VariableNames.ITERATED_BINDINGS, context);

		interpreters.interpret(runtime);

		/*
		 * extract and check results
		 */
		set = runtime.getRuntimeContext().pop();
		if (!set.contains(VariableNames.QUERYMATCHES)) {
			throw new TMQLRuntimeException(
					"Missing result of interpretation of filter");
		}

		QueryMatches negation = (QueryMatches) set
				.getValue(VariableNames.QUERYMATCHES);
		return negation.getNegation();
	}

	/**
	 * Interpretation method of grammar type
	 * {@link BooleanExpression#TYPE_COMPARISIONEXPRESSION}
	 * 
	 * @param runtime
	 *            the runtime
	 * @return the result of interpretation
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches comparison(final TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * get querying context
		 */
		QueryMatches context;
		IVariableSet set = runtime.getRuntimeContext().peek();
		if (!set.contains(VariableNames.ITERATED_BINDINGS)) {
			if (!set.contains(VariableNames.CURRENT_TUPLE)) {
				context = new QueryMatches(runtime);
				Map<String, Object> tuple = HashUtil.getHashMap();
				tuple.put(QueryMatches.getNonScopedVariable(), set
						.getValue(VariableNames.CURRENT_TUPLE));
				context.add(tuple);
			} else {
				throw new TMQLRuntimeException(
						"Missing context for boolean expression.");
			}
		} else {
			context = (QueryMatches) set
					.getValue(VariableNames.ITERATED_BINDINGS);
		}

		/*
		 * get expression interpreter
		 */
		IExpressionInterpreter<ComparisonExpression> interpreter = getInterpretersFilteredByEypressionType(
				runtime, ComparisonExpression.class).get(0);

		QueryMatches results = new QueryMatches(runtime);

		/*
		 * iterate over all possible bindings
		 */
		for (Map<String, Object> tuple : context) {

			/*
			 * add new variable layer and add all variables
			 */
			runtime.getRuntimeContext().push();
			runtime.getRuntimeContext().peek().remove(
					VariableNames.ITERATED_BINDINGS);
			for (Entry<String, Object> entry : tuple.entrySet()) {
				if (entry.getKey().equalsIgnoreCase(
						QueryMatches.getNonScopedVariable())) {
					runtime.getRuntimeContext().peek().setValue(
							VariableNames.CURRENT_TUPLE, entry.getValue());
				} else {
					runtime.getRuntimeContext().peek().setValue(entry.getKey(),
							entry.getValue());
				}
			}

			/*
			 * redirect to expression interpreter
			 */
			interpreter.interpret(runtime);

			/*
			 * get result of interpretation
			 */
			IVariableSet varSet = runtime.getRuntimeContext().pop();
			if (!varSet.contains(VariableNames.QUERYMATCHES)) {
				throw new TMQLRuntimeException(
						"Missing interpretation result of expression.");
			}

			/*
			 * check if result satisifies boolean expression
			 */
			QueryMatches result = (QueryMatches) varSet
					.getValue(VariableNames.QUERYMATCHES);
			boolean satisfies = !result.isEmpty();
			/*
			 * result may not be empty
			 */
			if (satisfies) {
				/*
				 * iterate over result tuples
				 */
				for (Map<String, Object> t : result) {
					/*
					 * result tuple may be an singleton
					 */
					if (t.containsKey(QueryMatches.getNonScopedVariable())
							/*
							 * check if result is a boolean value
							 */
							&& t.get(QueryMatches.getNonScopedVariable()) instanceof Boolean
							&& t.size() == 1) {
						/*
						 * check boolean value
						 */
						if (!((Boolean) t.get(QueryMatches
								.getNonScopedVariable())).booleanValue()) {
							satisfies = false;
							break;
						}
					}
					/*
					 * no boolean value of tuple or more than one tuple element
					 */
					else {
						satisfies = true;
						break;
					}
				}
			}

			/*
			 * store tuple as result of filter
			 */
			if (satisfies) {
				results.add(tuple);
			} else {
				results.getNegation().add(tuple);
			}
		}

		/*
		 * redirect negation
		 */
		results.getNegation().add(context.getNegation().getMatches());

		return results;
	}

	/**
	 * Interpretation method of grammar type
	 * {@link BooleanExpression#TYPE_PARETHETIC}
	 * 
	 * @param runtime
	 *            the runtime
	 * @return the result of interpretation
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches parenthetics(final TMQLRuntime runtime)
			throws TMQLRuntimeException {

		/*
		 * add new variable layer
		 */
		runtime.getRuntimeContext().push();

		/*
		 * redirect to contained expression
		 */
		getInterpreters(runtime).get(0).interpret(runtime);
		IVariableSet set = runtime.getRuntimeContext().pop();
		if (!set.contains(VariableNames.QUERYMATCHES)) {
			throw new TMQLRuntimeException(
					"Missing interpretation result of boolean-expression.");
		}

		/*
		 * redirect result
		 */
		QueryMatches results = (QueryMatches) set
				.getValue(VariableNames.QUERYMATCHES);
		return results;
	}

	/**
	 * Interpretation method of grammar type
	 * {@link BooleanExpression#TYPE_EXPRESSION}
	 * 
	 * @param runtime
	 *            the runtime
	 * @return the result of interpretation
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches expression(final TMQLRuntime runtime)
			throws TMQLRuntimeException {

		/*
		 * get querying context
		 */
		QueryMatches context;
		IVariableSet set = runtime.getRuntimeContext().peek();
		if (!set.contains(VariableNames.ITERATED_BINDINGS)) {
			if (!set.contains(VariableNames.CURRENT_TUPLE)) {
				context = new QueryMatches(runtime);
				Map<String, Object> tuple = HashUtil.getHashMap();
				tuple.put(QueryMatches.getNonScopedVariable(), set
						.getValue(VariableNames.CURRENT_TUPLE));
				context.add(tuple);
			} else {
				throw new TMQLRuntimeException(
						"Missing context for boolean expression.");
			}
		} else {
			context = (QueryMatches) set
					.getValue(VariableNames.ITERATED_BINDINGS);
		}

		/*
		 * get expression interpreter
		 */
		IExpressionInterpreter<Expression> interpreter = getInterpretersFilteredByEypressionType(
				runtime, Expression.class).get(0);

		QueryMatches results = new QueryMatches(runtime);

		/*
		 * iterate over all possible bindings
		 */
		for (Map<String, Object> tuple : context) {

			/*
			 * add new variable layer and add all variables
			 */
			runtime.getRuntimeContext().push();
			runtime.getRuntimeContext().peek().remove(
					VariableNames.ITERATED_BINDINGS);
			for (Entry<String, Object> entry : tuple.entrySet()) {
				if (entry.getKey().equalsIgnoreCase(
						QueryMatches.getNonScopedVariable())) {
					runtime.getRuntimeContext().peek().setValue(
							VariableNames.CURRENT_TUPLE, entry.getValue());
				} else {
					runtime.getRuntimeContext().peek().setValue(entry.getKey(),
							entry.getValue());
				}
			}

			/*
			 * redirect to expression interpreter
			 */
			interpreter.interpret(runtime);

			/*
			 * get result of interpretation
			 */
			IVariableSet varSet = runtime.getRuntimeContext().pop();
			if (!varSet.contains(VariableNames.QUERYMATCHES)) {
				throw new TMQLRuntimeException(
						"Missing interpretation result of expression.");
			}

			/*
			 * check if result satisifies boolean expression
			 */
			QueryMatches result = (QueryMatches) varSet
					.getValue(VariableNames.QUERYMATCHES);
			boolean satisfies = !result.isEmpty();
			/*
			 * result may not be empty
			 */
			if (satisfies) {
				/*
				 * iterate over result tuples
				 */
				for (Map<String, Object> t : result) {
					/*
					 * result tuple may be an singleton
					 */
					if (t.containsKey(QueryMatches.getNonScopedVariable())
							/*
							 * check if result is a boolean value
							 */
							&& t.get(QueryMatches.getNonScopedVariable()) instanceof Boolean
							&& t.size() == 1) {
						/*
						 * check boolean value
						 */
						if (!((Boolean) t.get(QueryMatches
								.getNonScopedVariable())).booleanValue()) {
							satisfies = false;
							break;
						}
					}
					/*
					 * no boolean value of tuple or more than one tuple element
					 */
					else {
						satisfies = true;
						break;
					}
				}
			}

			/*
			 * store tuple as result of filter
			 */
			if (satisfies) {
				results.add(tuple);
			} else {
				results.getNegation().add(tuple);
			}
		}

		/*
		 * redirect negation
		 */
		results.getNegation().add(context.getNegation().getMatches());

		return results;
	}
}
