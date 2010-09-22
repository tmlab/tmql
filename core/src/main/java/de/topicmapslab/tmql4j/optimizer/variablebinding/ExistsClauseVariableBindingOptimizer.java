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
import de.topicmapslab.tmql4j.parser.core.expressions.Content;
import de.topicmapslab.tmql4j.parser.core.expressions.ExistsClause;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Special implementation of a variable optimizer which is applicable for
 * exists-clauses. The variable bindings are ordered in a way that unsuccessful
 * bindings will detect as fast as possible.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ExistsClauseVariableBindingOptimizer extends
		VariableBindingOptimizer<ExistsClause> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL runtime
	 */
	public ExistsClauseVariableBindingOptimizer(TMQLRuntime runtime) {
		super(runtime);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Set<?> optimize(ExistsClause expression, String variable)
			throws TMQLOptimizationException {
		try {
			/*
			 * extract first contained expression
			 */
			IExpression subExpression = expression.getExpressions().get(0);
			/*
			 * switch by grammar type
			 */
			switch (expression.getGrammarType()) {
			/*
			 * is content-expression
			 */
			case ExistsClause.TYPE_NON_CANONICAL_EXPRESSION: {
				/*
				 * optimize variable binding by calling optimizer
				 */
				return new ContentVariableBindingOptimizer(getRuntime())
						.optimize((Content) subExpression, variable);
			}
			default:
				return getMaximumBindings();
			}
		} catch (TMQLRuntimeException e) {
			throw new TMQLOptimizationException(e);
		}
	}

}
