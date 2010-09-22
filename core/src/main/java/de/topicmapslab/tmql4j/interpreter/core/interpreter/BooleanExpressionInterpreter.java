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

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.model.tuplesequence.ITupleSequence;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.parser.core.expressions.BooleanExpression;

/**
 * 
 * Special interpreter class to interpret boolean-expression.
 * 
 * <p>
 * The grammar production rule of the expression is: <code> * 
 * <p>
 *   boolean-expression ::= boolean-expression | boolean-expression |
 * </p>
 * <p>
 * boolean-expression & boolean-expression | boolean-primitive
 * </p>
 * <p>
 * boolean-expression ::= forall-clause
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class BooleanExpressionInterpreter extends
		ExpressionInterpreterImpl<BooleanExpression> {

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
	public BooleanExpressionInterpreter(BooleanExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		switch (getGrammarTypeOfExpression()) {
		/*
		 * is boolean-expression ::= boolean-expression | boolean-expression
		 */
		case BooleanExpression.TYPE_DISJUNCTION: {
			interpretDisjunction(runtime);
		}
			break;
		/*
		 * is boolean-expression ::= boolean-expression & boolean-expression
		 */
		case BooleanExpression.TYPE_CONJUNCTION: {
			interpretConjunction(runtime);
		}
			break;
		/*
		 * is boolean-expression ::= boolean-primitive
		 */
		case BooleanExpression.TYPE_BOOLEAN_PRIMITIVE: {
			interpretBooleanPrimitve(runtime);
		}
			break;
		/*
		 * is boolean-expression ::= every binding-set satisfies
		 * boolean-expression
		 */
		case BooleanExpression.TYPE_FORALL_CLAUSE: {
			interpretForAllClause(runtime);
		}
			break;
		default:
			throw new TMQLRuntimeException("Unknown state");
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
	private void interpretDisjunction(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * cache of overall results
		 */
		QueryMatches queryMatches = new QueryMatches(runtime);

		/*
		 * Iterate over all boolean-expression
		 */
		for (IExpressionInterpreter<?> ex : getInterpreters(runtime)) {
			/*
			 * push new set to stack
			 */
			runtime.getRuntimeContext().push();

			/*
			 * call subexpression
			 */
			ex.interpret(runtime);

			/*
			 * pop stack from stack
			 */
			Object obj = runtime.getRuntimeContext().pop().getValue(
					VariableNames.QUERYMATCHES);

			if (obj instanceof QueryMatches) {
				QueryMatches matches = (QueryMatches) obj;
				if (!matches.isEmpty()) {
					if (queryMatches.isEmpty()) {
						queryMatches.add(matches);
					} else {
						queryMatches.disjunction(matches);
					}
				}
			}
		}
		/*
		 * save overall results
		 */
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				queryMatches);

		/*
		 * log it :)
		 */
		logger.info("Finished! Boolean expression reutrn true!");
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
	private void interpretConjunction(TMQLRuntime runtime)
			throws TMQLRuntimeException {

		/*
		 * cache of overall results
		 */
		QueryMatches queryMatches = new QueryMatches(runtime);

		boolean satisfy = true;

		/*
		 * Iterate over all boolean-expression
		 */
		for (IExpressionInterpreter<?> ex : getInterpreters(runtime)) {
			/*
			 * push new set to stack
			 */
			runtime.getRuntimeContext().push();

			/*
			 * call subexpression
			 */
			ex.interpret(runtime);

			/*
			 * pop stack from stack
			 */
			Object obj = runtime.getRuntimeContext().pop().getValue(
					VariableNames.QUERYMATCHES);

			if (obj instanceof QueryMatches) {
				QueryMatches matches = (QueryMatches) obj;
				if (matches.isEmpty()) {
					satisfy = false;
					/*
					 * save overall results
					 */
					runtime.getRuntimeContext().peek().setValue(
							VariableNames.QUERYMATCHES,
							new QueryMatches(runtime));
					return;
				} else if (queryMatches.isEmpty()) {
					queryMatches.add(matches);
				} else {
					boolean missingVariable = false;
					for (String key : ex.getVariables()) {
						if (runtime.variables.containsKey(key)) {
							continue;
						}
						ITupleSequence<Object> matchesObject = matches
								.getPossibleValuesForVariable(key);
						if (matchesObject.isEmpty()
								|| (matchesObject.get(0) instanceof Collection<?> && ((Collection<Object>) matchesObject
										.get(0)).isEmpty())) {
							missingVariable = true;
						}
					}
					if (missingVariable) {
						queryMatches = new QueryMatches(runtime);
						break;
					} else {
						queryMatches.conjunction(matches);
					}
				}
			}

			/*
			 * Store for later reuse
			 */
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.ITERATED_BINDINGS, queryMatches);

			/*
			 * check if result is empty -> boolean-expression returns empty
			 * tuple sequence
			 */
			if (queryMatches.isEmpty() || !satisfy) {
				runtime.getRuntimeContext().peek().setValue(
						VariableNames.QUERYMATCHES, new QueryMatches(runtime));
				/*
				 * log it :)
				 */
				logger.info("Finished! Boolean expression reutrn false!");
				break;
			}
		}

		/*
		 * save overall results
		 */
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				queryMatches);
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
	private void interpretBooleanPrimitve(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * redirect to sub-expression
		 */
		IExpressionInterpreter<?> ex = getInterpreters(runtime).get(0);
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
	private void interpretForAllClause(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * redirect to subexpression
		 */
		IExpressionInterpreter<?> ex = getInterpreters(runtime).get(0);
		ex.interpret(runtime);
	}

}
