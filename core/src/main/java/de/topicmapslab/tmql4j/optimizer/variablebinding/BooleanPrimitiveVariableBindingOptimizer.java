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
import de.topicmapslab.tmql4j.parser.core.expressions.ExistsClause;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Special implementation of a variable optimizer which is applicable for
 * boolean-primitves. The variable bindings are ordered in a way that
 * unsuccessful bindings will detect as fast as possible.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class BooleanPrimitiveVariableBindingOptimizer extends
		VariableBindingOptimizer<BooleanPrimitive> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL runtime
	 */
	public BooleanPrimitiveVariableBindingOptimizer(TMQLRuntime runtime) {
		super(runtime);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Set<?> optimize(BooleanPrimitive expression, String variable)
			throws TMQLOptimizationException {
		try {
			/*
			 * extract contained expression
			 */
			IExpression subExpression = expression.getExpressions().get(0);
			/*
			 * switch by grammar type
			 */
			switch (expression.getGrammarType()) {
			/*
			 * is boolean-expression
			 */
			case BooleanPrimitive.TYPE_BOOLEAN_EXPRESSION: {
				/*
				 * optimize variable binding by calling optimizer
				 */
				return new BooleanExpressionVariableBindingOptimizer(
						getRuntime()).optimize(
						(BooleanExpression) subExpression, variable);
			}
				/*
				 * is not-expression
				 */
			case BooleanPrimitive.TYPE_NOT_EXPRESSION: {
				Set<Object> bindings = HashUtil.getHashSet();
				/*
				 * add all possible bindings
				 */
				bindings.addAll(getMaximumBindings());
				/*
				 * remove matching bindings of contained expression
				 */
				bindings
						.removeAll(new BooleanPrimitiveVariableBindingOptimizer(
								getRuntime()).optimize(
								(BooleanPrimitive) subExpression, variable));
				return bindings;
			}
				/*
				 * is exists-clause
				 */
			case BooleanPrimitive.TYPE_EXISTS_CLAUSE: {
				/*
				 * optimize variable binding by calling optimizer
				 */
				return new ExistsClauseVariableBindingOptimizer(getRuntime())
						.optimize((ExistsClause) subExpression, variable);
			}
			default:
				return getMaximumBindings();
			}
		} catch (TMQLRuntimeException e) {
			throw new TMQLOptimizationException(e);
		}
	}

}
