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
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.Construct;
import org.tmapi.core.Scoped;

import de.topicmapslab.tmql4j.api.exceptions.DataBridgeException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.model.tuplesequence.ITupleSequence;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.model.context.IVariableSet;
import de.topicmapslab.tmql4j.optimizer.variablebinding.BooleanPrimitiveVariableBindingOptimizer;
import de.topicmapslab.tmql4j.parser.core.expressions.BooleanExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.BooleanPrimitive;
import de.topicmapslab.tmql4j.parser.core.expressions.ExistsClause;
import de.topicmapslab.tmql4j.parser.core.expressions.ForAllClause;

/**
 * 
 * Special interpreter class to interpret boolean-primitives.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * boolean-primitive ::= '(' boolean-expression ')'
 * </p>
 * 
 * <p>
 * boolean-primitive ::= 'not' boolean-primitive
 * </p>
 * 
 * <p>
 * boolean-primitive ::= forall-clause
 * </p>
 * 
 * <p>
 * boolean-primitive ::= exists-clause
 * </p>
 * 
 * <p>
 * boolean-primitive ::= '@' anchor
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class BooleanPrimitiveInterpreter extends
		ExpressionInterpreterImpl<BooleanPrimitive> {

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
	public BooleanPrimitiveInterpreter(BooleanPrimitive ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		switch (getGrammarTypeOfExpression()) {
		/*
		 * is cramped-boolean expression
		 */
		case BooleanPrimitive.TYPE_BOOLEAN_EXPRESSION: {
			interpretCrampedBooleanExpression(runtime);
		}
			break;
		/*
		 * is not-expression
		 */
		case BooleanPrimitive.TYPE_NOT_EXPRESSION: {
			interpretNotExpression(runtime);
		}
			break;
		/*
		 * is for-all-clause
		 */
		case BooleanPrimitive.TYPE_EVERY_CLAUSE: {
			interpretForAllExpression(runtime);
		}
			break;
		/*
		 * is exists-clause
		 */
		case BooleanPrimitive.TYPE_EXISTS_CLAUSE: {
			interpretExsistsExpression(runtime);
		}
			break;
		/*
		 * is scoped-expression
		 */
		case BooleanPrimitive.TYPE_SCOPED_EXPRESSION: {
			interpretScopedExpression(runtime);
		}
			break;
		}

	}

	/**
	 * The method is called to interpret the given sub-expression by using the
	 * given runtime. The interpretation will call the sub-expression if the
	 * given expression isn't a leaf in parsing-tree.
	 * 
	 * <p>
	 * The interpretation will transform the value on top of the stack and put
	 * its results also on top.
	 * </p>
	 * 
	 * @param runtime
	 *            the runtime which contains all necessary information for
	 *            querying process
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private void interpretCrampedBooleanExpression(TMQLRuntime runtime)
			throws TMQLRuntimeException {

		/*
		 * redirect to sub-expression
		 */
		IExpressionInterpreter<BooleanExpression> ex = getInterpretersFilteredByEypressionType(
				runtime, BooleanExpression.class).get(0);
		ex.interpret(runtime);
	}

	/**
	 * The method is called to interpret the given sub-expression by using the
	 * given runtime. The interpretation will call the sub-expression if the
	 * given expression isn't a leaf in parsing-tree.
	 * 
	 * <p>
	 * The interpretation will transform the value on top of the stack and put
	 * its results also on top.
	 * </p>
	 * 
	 * @param runtime
	 *            the runtime which contains all necessary information for
	 *            querying process
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	@SuppressWarnings("unchecked")
	private void interpretNotExpression(TMQLRuntime runtime)
			throws TMQLRuntimeException {

		QueryMatches iterations = null;
		/*
		 * check if iteration results of pre-proceeded expressions are existing
		 */
		if (runtime.getRuntimeContext().peek().contains(
				VariableNames.ITERATED_BINDINGS)) {
			iterations = (QueryMatches) runtime.getRuntimeContext().peek()
					.getValue(VariableNames.ITERATED_BINDINGS);
		} else {
			iterations = new QueryMatches(runtime);
		}

		/*
		 * get interpreter of sub-expression
		 */
		IExpressionInterpreter ex = getInterpreters(runtime).get(0);

		/*
		 * push new set on the top of the stack and remove unimportant
		 * information
		 */
		runtime.getRuntimeContext().push();
		runtime.getRuntimeContext().peek().remove(
				VariableNames.ITERATED_BINDINGS);
		/*
		 * Call subexpression
		 */
		ex.interpret(runtime);

		/*
		 * pop variable set containing results from stack
		 */
		IVariableSet set = runtime.getRuntimeContext().pop();

		/*
		 * get matches
		 */
		QueryMatches negatives = (QueryMatches) set
				.getValue(VariableNames.QUERYMATCHES);

		QueryMatches results = new QueryMatches(runtime);
		/*
		 * solution a: check if iteration results of pre-proceeded expression
		 * are available
		 */
		if (!iterations.isEmpty()) {
			/*
			 * iterate over pre-proceeded results and remove matches of
			 * contained boolean-expression
			 */
			for (Map<String, Object> tuple : iterations) {
				boolean satisfy = true;
				for (Entry<String, Object> entry : tuple.entrySet()) {
					if (!runtime.isSystemVariable(entry.getKey())) {
						if (negatives.getPossibleValuesForVariable(
								entry.getKey()).contains(entry.getValue())) {
							satisfy = false;
							break;
						}
					}
				}
				if (satisfy) {
					results.add(tuple);
				}
			}

		}
		/*
		 * check if contained boolean-expression returns negative matches
		 */
		else if (!negatives.getNegation().isEmpty()) {
			results.add(negatives.getNegation());
		}
		/*
		 * 
		 */
		else {
			Set<QueryMatches> matches = HashUtil.getHashSet();
			/*
			 * iterate over all variables
			 */
			for (final String variable : getVariables()) {
				/*
				 * check if variable is a system variable
				 */
				if (!runtime.isSystemVariable(variable)) {
					/*
					 * add possible variable bindings
					 */
					QueryMatches match = new QueryMatches(runtime);
					match
							.convertToTuples(
									(Set<Object>) new BooleanPrimitiveVariableBindingOptimizer(
											runtime).optimize(ex, variable),
									variable);
					matches.add(match);

				}
			}

			/*
			 * iterate over all possible variable bindings
			 */
			for (Map<String, Object> tuple : new QueryMatches(runtime, matches)) {
				if (negatives.getMatches().contains(tuple)) {
					continue;
				}
				/*
				 * push new set on the top of the stack and set @_
				 */
				runtime.getRuntimeContext().push();

				QueryMatches iteration = new QueryMatches(runtime);
				iteration.add(tuple);

				/*
				 * push content to the top of the stack
				 */
				runtime.getRuntimeContext().peek().setValue(
						VariableNames.ITERATED_BINDINGS, iteration);

				/*
				 * call sub-expression
				 */
				ex.interpret(runtime);

				set = runtime.getRuntimeContext().pop();

				/*
				 * extract results
				 */
				QueryMatches result = (QueryMatches) set
						.getValue(VariableNames.QUERYMATCHES);
				if (!result.isEmpty()) {
					results.add(result);
				}
			}
		}

		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);

	}

	/**
	 * The method is called to interpret the given sub-expression by using the
	 * given runtime. The interpretation will call the sub-expression if the
	 * given expression isn't a leaf in parsing-tree.
	 * 
	 * <p>
	 * The interpretation will transform the value on top of the stack and put
	 * its results also on top.
	 * </p>
	 * 
	 * @param runtime
	 *            the runtime which contains all necessary information for
	 *            querying process
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private void interpretForAllExpression(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * redirect to sub-expression
		 */
		IExpressionInterpreter<ForAllClause> ex = getInterpretersFilteredByEypressionType(
				runtime, ForAllClause.class).get(0);
		ex.interpret(runtime);
	}

	/**
	 * The method is called to interpret the given sub-expression by using the
	 * given runtime. The interpretation will call the sub-expression if the
	 * given expression isn't a leaf in parsing-tree.
	 * 
	 * <p>
	 * The interpretation will transform the value on top of the stack and put
	 * its results also on top.
	 * </p>
	 * 
	 * @param runtime
	 *            the runtime which contains all necessary information for
	 *            querying process
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private void interpretExsistsExpression(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		runtime.getRuntimeContext().push();
		/*
		 * redirect to sub-expression
		 */
		IExpressionInterpreter<ExistsClause> ex = getInterpretersFilteredByEypressionType(
				runtime, ExistsClause.class).get(0);
		ex.interpret(runtime);
		IVariableSet set = runtime.getRuntimeContext().pop();
		
		/*
		 * get result
		 */
		QueryMatches result = (QueryMatches)set.getValue(VariableNames.QUERYMATCHES);
		/*
		 * called by filter
		 */
		if ( runtime.getRuntimeContext().peek().contains(VariableNames.CURRENT_TUPLE)){			
			if ( !result.isEmpty()){
				result = new QueryMatches(runtime);
				Map<String, Object> tuple = HashUtil.getHashMap();
				tuple.put(QueryMatches.getNonScopedVariable(), runtime.getRuntimeContext().peek().getValue(VariableNames.CURRENT_TUPLE));
				result.add(tuple);
			}
		}		
			runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES, result);
	}

	/**
	 * The method is called to interpret the given sub-expression by using the
	 * given runtime. The interpretation will call the sub-expression if the
	 * given expression isn't a leaf in parsing-tree.
	 * 
	 * <p>
	 * The interpretation will transform the value on top of the stack and put
	 * its results also on top.
	 * </p>
	 * 
	 * @param runtime
	 *            the runtime which contains all necessary information for
	 *            querying process
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private void interpretScopedExpression(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * Extract anchor specifying the scope
		 */
		String anchor = getTokens().get(0);
		/*
		 * Try to resolve Topic as scope
		 */
		Construct scope = null;
		try {
			scope = runtime.getDataBridge().getConstructByIdentifier(runtime,
					anchor);
		} catch (DataBridgeException e) {
			logger.warn("Cannot find specified scope " + anchor);
		}
		/*
		 * return empty result if theme is unknown
		 */
		if (scope == null) {
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.QUERYMATCHES,
					runtime.getProperties().newSequence());
			return;
		}

		/*
		 * Extract value of @_
		 */
		Object obj = runtime.getRuntimeContext().peek().getValue(
				VariableNames.CURRENT_TUPLE);
		if (obj != null && obj instanceof Scoped) {
			/*
			 * Check if scopes containing the specified theme
			 */
			if (((Scoped) obj).getScope().contains(scope)) {
				ITupleSequence<Object> sequence = runtime.getProperties()
						.newSequence();
				sequence.add(obj);
				runtime.getRuntimeContext().peek().setValue(
						VariableNames.QUERYMATCHES, sequence);
			}
		}
	}

}
