package de.topicmapslab.tmql4j.draft2010.components.interpreter;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.BooleanExpression;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.ComparisonExpression;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.Expression;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;

/**
 * Interpreter implementation of production 'boolean-expression' (
 * {@link BooleanExpression} )
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class BooleanExpressionInterpeter extends ExpressionInterpreterImpl<BooleanExpression> {
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
	public BooleanExpressionInterpeter(BooleanExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		if (context.getContextBindings() == null) {
			logger.warn("Missing context to execute conjunction!");
			return QueryMatches.emptyMatches();
		}
		/*
		 * switch by grammar type
		 */
		if (getGrammarTypeOfExpression() == BooleanExpression.TYPE_CONJUNCTION) {
			return conjunction(runtime, context, optionalArguments);
		} else if (getGrammarTypeOfExpression() == BooleanExpression.TYPE_DISJUNCTION) {
			return disjunction(runtime, context, optionalArguments);
		} else if (getGrammarTypeOfExpression() == BooleanExpression.TYPE_NOTEXPRESSION) {
			return negation(runtime, context, optionalArguments);
		} else if (getGrammarTypeOfExpression() == BooleanExpression.TYPE_COMPARISIONEXPRESSION) {
			return comparison(runtime, context, optionalArguments);
		} else if (getGrammarTypeOfExpression() == BooleanExpression.TYPE_CLAMPED) {
			return parenthetics(runtime, context, optionalArguments);
		} else if (getGrammarTypeOfExpression() == BooleanExpression.TYPE_EXPRESSION) {
			return expression(runtime, context, optionalArguments);
		}
		logger.warn("Invalid grammar type '" + getGrammarTypeOfExpression() + "' of production 'boolean-expression'.");
		return QueryMatches.emptyMatches();
	}

	/**
	 * Interpretation method of grammar type
	 * {@link BooleanExpression#TYPE_CONJUNCTION}
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the querying context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the result of interpretation
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches conjunction(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		Context newContex = new Context(context);
		/*
		 * interpret boolean-expressions
		 */
		for (IExpressionInterpreter<BooleanExpression> interpreters : getInterpretersFilteredByEypressionType(runtime, BooleanExpression.class)) {

			QueryMatches matches = interpreters.interpret(runtime, newContex, optionalArguments);
			newContex.setContextBindings(matches);
			if (matches.isEmpty()) {
				return QueryMatches.emptyMatches();
			}
		}
		return newContex.getContextBindings();
	}

	/**
	 * Interpretation method of grammar type
	 * {@link BooleanExpression#TYPE_DISJUNCTION}
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the querying context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the result of interpretation
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches disjunction(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		QueryMatches matches = new QueryMatches(runtime);
		/*
		 * interpret boolean-expressions
		 */
		for (IExpressionInterpreter<BooleanExpression> interpreters : getInterpretersFilteredByEypressionType(runtime, BooleanExpression.class)) {
			QueryMatches results = interpreters.interpret(runtime, context, optionalArguments);
			/*
			 * handle disjunction
			 */
			if (matches.isEmpty()) {
				matches.add(results);
				matches.getNegation().add(results.getNegation());
			} else {
				matches.disjunction(results);
				matches.getNegation().conjunction(results.getNegation());
			}
		}
		return matches;
	}

	/**
	 * Interpretation method of grammar type
	 * {@link BooleanExpression#TYPE_NOTEXPRESSION}
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the querying context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the result of interpretation
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches negation(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * interpret boolean-expression
		 */
		IExpressionInterpreter<BooleanExpression> interpreters = getInterpretersFilteredByEypressionType(runtime, BooleanExpression.class).get(0);
		QueryMatches matches = interpreters.interpret(runtime, context, optionalArguments);
		return matches.getNegation();
	}

	/**
	 * Interpretation method of grammar type
	 * {@link BooleanExpression#TYPE_COMPARISIONEXPRESSION}
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the querying context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the result of interpretation
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches comparison(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * get expression interpreter
		 */
		return redirectToSubExpression(runtime, context, ComparisonExpression.class, optionalArguments);
	}

	/**
	 * Interpretation method of grammar type
	 * {@link BooleanExpression#TYPE_CLAMPED}
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the querying context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the result of interpretation
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches parenthetics(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * redirect to contained expression
		 */
		return getInterpreters(runtime).get(0).interpret(runtime, context, optionalArguments);
	}

	/**
	 * Interpretation method of grammar type
	 * {@link BooleanExpression#TYPE_EXPRESSION}
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the querying context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the result of interpretation
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches expression(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * get expression interpreter
		 */
		return redirectToSubExpression(runtime, context, Expression.class, optionalArguments);
	}

	/**
	 * Interpretation method of grammar type
	 * {@link BooleanExpression#TYPE_EXPRESSION}
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the querying context
	 * @param expressionType
	 *            the expression type to extract the interpreter to call
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the result of interpretation
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches redirectToSubExpression(ITMQLRuntime runtime, IContext context, Class<? extends IExpression> expressionType, Object... optionalArguments) {

		IExpressionInterpreter<?> interpreter = getInterpretersFilteredByEypressionType(runtime, expressionType).get(0);
		QueryMatches results = new QueryMatches(runtime);

		/*
		 * iterate over all possible bindings
		 */
		for (Map<String, Object> tuple : context.getContextBindings()) {
			Context newContext = new Context(context);
			newContext.setCurrentTuple(tuple);
			newContext.setContextBindings(null);

			/*
			 * redirect to expression interpreter
			 */
			QueryMatches matches = interpreter.interpret(runtime, newContext, optionalArguments);
			boolean satisfies = !matches.isEmpty();
			/*
			 * result may not be empty
			 */
			if (satisfies) {
				/*
				 * iterate over result tuples
				 */
				for (Map<String, Object> t : matches) {
					/*
					 * result tuple may be an singleton
					 */
					if (t.containsKey(QueryMatches.getNonScopedVariable())
					/*
					 * check if result is a boolean value
					 */
					&& t.get(QueryMatches.getNonScopedVariable()) instanceof Boolean && t.size() == 1) {
						/*
						 * check boolean value
						 */
						if (!((Boolean) t.get(QueryMatches.getNonScopedVariable())).booleanValue()) {
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
		results.getNegation().add(context.getContextBindings().getNegation().getMatches());
		return results;
	}
}
