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
import de.topicmapslab.tmql4j.parser.core.expressions.FlwrExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.PathExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.QueryExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.SelectExpression;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Special implementation of a variable optimizer which is applicable for
 * query-expressions. The variable bindings are ordered in a way that
 * unsuccessful bindings will detect as fast as possible.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class QueryExpressionVariableBindingOptimizer
		extends VariableBindingOptimizer<QueryExpression> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL runtime
	 */
	public QueryExpressionVariableBindingOptimizer(TMQLRuntime runtime) {
		super(runtime);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Set<?> optimize(QueryExpression expression, String variable)
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
			 * is select-expression
			 */
			case QueryExpression.TYPE_SELECT_EXPRESSION: {
				/*
				 * call optimizer to optimize variable binding
				 */
				return new SelectExpressionVariableBindingOptimizer(
						getRuntime()).optimize(
						(SelectExpression) subExpression, variable);
			}
			/*
			 * is flwr-expression
			 */
			case QueryExpression.TYPE_FLWR_EXPRESSION: {
				/*
				 * call optimizer to optimize variable binding
				 */
				return new FlwrExpressionVariableBindingOptimizer(getRuntime())
						.optimize((FlwrExpression) subExpression, variable);
			}
			/*
			 * is path-expression
			 */
			case QueryExpression.TYPE_PATH_EXPRESSION: {
				/*
				 * call optimizer to optimize variable binding
				 */
				return new PathExpressionVariableBindingOptimizer(getRuntime())
						.optimize((PathExpression) subExpression, variable);
			}
			default:
				return getMaximumBindings();
			}
		} catch (TMQLRuntimeException e) {
			throw new TMQLOptimizationException(e);
		}
	}

}
