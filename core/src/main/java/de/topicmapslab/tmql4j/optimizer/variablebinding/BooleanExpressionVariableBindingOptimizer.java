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
package de.topicmapslab.tmql4j.optimizer.variablebinding;

import java.util.Set;

import de.topicmapslab.tmql4j.common.core.exception.TMQLOptimizationException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.parser.core.expressions.BooleanExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.BooleanPrimitive;

/**
 * Special implementation of a variable optimizer which is applicable for
 * boolean-expressions. The variable bindings are ordered in a way that
 * unsuccessful bindings will detect as fast as possible.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class BooleanExpressionVariableBindingOptimizer extends
		VariableBindingOptimizer<BooleanExpression> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL runtime
	 */
	public BooleanExpressionVariableBindingOptimizer(TMQLRuntime runtime) {
		super(runtime);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<?> optimize(BooleanExpression expression, String variable)
			throws TMQLOptimizationException {

		try {
			Set<Object> bindings = HashUtil.getHashSet();
			/*
			 * switch by grammar type
			 */
			switch (expression.getGrammarType()) {
			/*
			 * expression is conjunction
			 */
			case BooleanExpression.TYPE_CONJUNCTION: {
				/*
				 * add all possible bindings to temporary set
				 */
				bindings.addAll(getMaximumBindings());
			}
				/*
				 * expression is disjunction
				 */
			case BooleanExpression.TYPE_DISJUNCTION: {
				/*
				 * iterate over contained boolean expressions
				 */
				for (BooleanExpression booleanExpression : expression
						.getExpressionFilteredByType(BooleanExpression.class)) {
					/*
					 * check if variable is contained
					 */
					if (!booleanExpression.getVariables().contains(variable)) {
						continue;
					}
					/*
					 * optimize variable binding of current boolean-expression
					 */
					BooleanExpressionVariableBindingOptimizer optimizer = new BooleanExpressionVariableBindingOptimizer(
							getRuntime());
					/*
					 * add possible bindings to temporary set if expression is a
					 * disjunction
					 */
					if (expression.getGrammarType() == BooleanExpression.TYPE_DISJUNCTION) {
						bindings.addAll(optimizer.optimize(booleanExpression,
								variable));
					}
					/*
					 * retain possible bindings in temporary set
					 */
					else {
						bindings.retainAll(optimizer.optimize(
								booleanExpression, variable));
					}
				}
			}
				break;
			/*
			 * expression is boolean primitive
			 */
			case BooleanExpression.TYPE_BOOLEAN_PRIMITIVE: {
				/*
				 * optimize variable binding of current boolean-primitive
				 */
				bindings.addAll(new BooleanPrimitiveVariableBindingOptimizer(
						getRuntime()).optimize(expression
						.getExpressionFilteredByType(BooleanPrimitive.class)
						.get(0), variable));
			}
				break;
			/*
			 * other grammar types wont be optimized
			 */
			default:
				bindings.addAll(getMaximumBindings());
			}
			return bindings;
		} catch (TMQLRuntimeException e) {
			throw new TMQLOptimizationException(e);
		}
	}

}
