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
package de.topicmapslab.tmql4j.optimizer.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.topicmapslab.tmql4j.common.core.exception.TMQLOptimizationException;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.optimizer.cache.OptimizationCache;
import de.topicmapslab.tmql4j.optimizer.variablebinding.SelectExpressionVariableBindingOptimizer;
import de.topicmapslab.tmql4j.parser.core.expressions.BooleanExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.BooleanPrimitive;
import de.topicmapslab.tmql4j.parser.core.expressions.ForAllClause;
import de.topicmapslab.tmql4j.parser.core.expressions.SelectExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.WhereClause;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Utility class to order the variable binding to optimize the execution order.
 * Variable are ordered by the number of possible counts.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class BindingOrderUtility {

	/**
	 * Order variables of the given expression by their variable count and their
	 * dependencies from other variables. The variables are dependent if they
	 * are part of the same expression.
	 * 
	 * @param interpreter
	 *            the interpreter contained the variables to optimize
	 * @param runtime
	 *            the current TMQL runtime
	 * @return a list containing the order of variables to iterate over during
	 *         interpretation process
	 * @throws TMQLOptimizationException
	 *             thrown if optimization fails
	 */
	public List<String> orderVariables(IExpressionInterpreter<?> interpreter,
			TMQLRuntime runtime) throws TMQLOptimizationException {

		try {
			/*
			 * extract all variables of the expression
			 */
			List<String> variables = interpreter.getExpression().getVariables();

			/*
			 * check if optimization is activate
			 */
			if (runtime.getProperties().useOptimization()) {
				/*
				 * get cache of variable bindings
				 */
				OptimizationCache cache = OptimizationCache
						.getOptimizationCache();
				/*
				 * create list of new variables binding beans
				 */
				List<VariableBindingBean> bindingCounts = new LinkedList<VariableBindingBean>();
				/*
				 * iterate over variables
				 */
				for (String variable : variables) {
					/*
					 * check if variable is already cached
					 */
					if (!cache.isCached(interpreter, variable)) {
						/*
						 * create optimizer for select-expression
						 */
						SelectExpressionVariableBindingOptimizer optimizer = new SelectExpressionVariableBindingOptimizer(
								runtime);
						Set<Object> optimized = HashUtil.getHashSet();
						/*
						 * optimize variable bindings
						 */
						optimized.addAll((Set<?>) optimizer.optimize(
								interpreter, variable));
						/*
						 * store variable binding
						 */
						cache.cache(interpreter, variable, optimized);
						/*
						 * save current variable bindings for later reuse
						 */
						bindingCounts
								.add(new VariableBindingBean(variable,
										optimized.size(), getMinimumDependency(
												interpreter.getExpression(),
												variable)));
					}
					/*
					 * binding is already cached
					 */
					else {
						/*
						 * load variable binding from cache
						 */
						bindingCounts
								.add(new VariableBindingBean(variable, cache
										.getCachedOptimization(interpreter,
												variable).size(),
										getMinimumDependency(interpreter
												.getExpression(), variable)));
					}
				}

				/*
				 * Sort variable by possible bindings and dependencies
				 */
				Collections.sort(bindingCounts, new BindingsCountComparator());

				/*
				 * reorder variables
				 */
				variables.clear();
				for (VariableBindingBean bean : bindingCounts) {
					variables.add(bean.variable);
				}
				/*
				 * debug output
				 */
				if (runtime.isVerbose()) {
					System.out
							.println("Optimized variable order: " + variables);
				}
			}
			return variables;
		} catch (TMQLRuntimeException e) {
			throw new TMQLOptimizationException(e);
		}

	}

	/**
	 * bean class representing the binding of one variable
	 * 
	 * @author Sven Krosse
	 * @email krosse@informatik.uni-leipzig.de
	 * 
	 */
	class VariableBindingBean {
		/**
		 * possible binding count
		 */
		private Integer count;
		/**
		 * the variable name
		 */
		private String variable;
		/**
		 * the number of dependecies
		 */
		private Integer dependencies;

		/**
		 * private and hidden constructor to create a new variable bean
		 * 
		 * @param variable
		 *            the variable name
		 * @param count
		 *            the possible binding count
		 * @param dependencies
		 *            the count of dependencies
		 */
		private VariableBindingBean(final String variable, final Integer count,
				final Integer dependencies) {
			this.variable = variable;
			this.count = count;
			this.dependencies = dependencies;
		}
	}

	/**
	 * Comparator class to compare two instances of variable beans
	 * 
	 * @author Sven Krosse
	 * @email krosse@informatik.uni-leipzig.de
	 * 
	 */
	class BindingsCountComparator implements Comparator<VariableBindingBean> {
		public int compare(VariableBindingBean o1, VariableBindingBean o2) {
			/*
			 * if two variables have the same dependencies return the
			 * subtraction of variable counts
			 */
			if (o1.dependencies == o2.dependencies) {
				return o1.count - o2.count;
			}
			/*
			 * return subtraction of variable dependencies
			 */
			else {
				return o1.dependencies - o2.dependencies;
			}
		}
	}

	/**
	 * Method return the minimum dependencies of the given variable.
	 * 
	 * @param expression
	 *            the expression used to extract the dependency
	 * @param variable
	 *            the variable to check
	 * @return the minimum number of dependencies
	 * @throws TMQLOptimizationException
	 *             thrown if optimization fails
	 */
	private Integer getMinimumDependency(final IExpression expression,
			final String variable) throws TMQLOptimizationException {
		/*
		 * check if expression is a select expression
		 */
		if (expression instanceof SelectExpression) {
			/*
			 * call select expression dependency class
			 */
			return new SelectExpressionDependency().getMinimumDependency(
					(SelectExpression) expression, variable);
		} else {
			return 9999;
		}
	}

}

/**
 * Abstract class to represent the dependencies of a specified expression
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 * @param <T>
 *            the expression type
 */
abstract class Dependency<T extends IExpression> {
	/**
	 * Method return the minimum dependencies of the given variable.
	 * 
	 * @param expression
	 *            the expression used to extract the dependency
	 * @param variable
	 *            the variable to check
	 * @return the minimum number of dependencies
	 * @throws TMQLOptimizationException
	 *             thrown if optimization fails
	 */
	public abstract Integer getMinimumDependency(T expression,
			final String variable) throws TMQLOptimizationException;
}

/**
 * Implementation of dependencies method of select-expressions
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
class SelectExpressionDependency extends Dependency<SelectExpression> {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getMinimumDependency(SelectExpression expression,
			final String variable) throws TMQLOptimizationException {
		try {
			/*
			 * try to extract dependencies of internal where-clause
			 */
			return new WhereClauseDependency().getMinimumDependency(expression
					.getExpressionFilteredByType(WhereClause.class).get(0),
					variable);
		} catch (TMQLRuntimeException e) {
			throw new TMQLOptimizationException(e);
		}
	}
}

/**
 * Implementation of dependencies method of where-clause
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
class WhereClauseDependency extends Dependency<WhereClause> {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getMinimumDependency(WhereClause expression,
			final String variable) throws TMQLOptimizationException {
		try {
			/*
			 * try to extract dependencies of top level boolean-expression
			 */
			return new BooleanExpressionDependency().getMinimumDependency(
					expression.getExpressionFilteredByType(
							BooleanExpression.class).get(0), variable);
		} catch (TMQLRuntimeException e) {
			throw new TMQLOptimizationException(e);
		}
	}
}

/**
 * Implementation of dependencies method of boolean-expression
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
class BooleanExpressionDependency extends Dependency<BooleanExpression> {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getMinimumDependency(BooleanExpression expression,
			final String variable) throws TMQLOptimizationException {
		try {
			/*
			 * switch by grammar type
			 */
			switch (expression.getGrammarType()) {
			/*
			 * contains only a boolean-primitive
			 */
			case BooleanExpression.TYPE_BOOLEAN_PRIMITIVE: {
				/*
				 * try to extract dependencies of boolean-primitive
				 */
				return new BooleanPrimitiveDependency().getMinimumDependency(
						expression.getExpressionFilteredByType(
								BooleanPrimitive.class).get(0), variable);
			}
				/*
				 * contains only a for-all clause
				 */
			case BooleanExpression.TYPE_FORALL_CLAUSE: {
				/*
				 * get reference of contained for-all-clause
				 */
				ForAllClause clause = expression.getExpressionFilteredByType(
						ForAllClause.class).get(0);
				/*
				 * check if variable is contained
				 */
				if (clause.getVariables().contains(variable)) {
					/*
					 * return the number of variables
					 */
					return clause.getVariables().size() - 1;
				}
			}
				break;
			/*
			 * contains boolean combination
			 */
			case BooleanExpression.TYPE_CONJUNCTION:
			case BooleanExpression.TYPE_DISJUNCTION: {
				Integer minimum = 9999;
				/*
				 * iterate over contained boolean-expressions
				 */
				for (BooleanExpression booleanExpression : expression
						.getExpressionFilteredByType(BooleanExpression.class)) {
					/*
					 * extract dependency
					 */
					Integer tmp = new BooleanExpressionDependency()
							.getMinimumDependency(booleanExpression, variable);
					/*
					 * set minimum
					 */
					if (tmp < minimum) {
						minimum = tmp;
					}
				}
				return minimum;
			}
			}
		} catch (TMQLRuntimeException e) {
			throw new TMQLOptimizationException(e);
		}
		return 9999;
	}
}

/**
 * Implementation of dependencies method of boolean-primitive
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
class BooleanPrimitiveDependency extends Dependency<BooleanPrimitive> {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getMinimumDependency(BooleanPrimitive expression,
			final String variable) throws TMQLOptimizationException {
		try {
			/*
			 * switch by grammar type
			 */
			switch (expression.getGrammarType()) {
			/*
			 * contains only a boolean-expression
			 */
			case BooleanPrimitive.TYPE_BOOLEAN_EXPRESSION: {
				/*
				 * try to extract dependencies of boolean-expression
				 */
				return new BooleanExpressionDependency().getMinimumDependency(
						expression.getExpressionFilteredByType(
								BooleanExpression.class).get(0), variable);
			}
				/*
				 * contains only a not-expression
				 */
			case BooleanPrimitive.TYPE_NOT_EXPRESSION: {
				/*
				 * try to extract dependencies of boolean-primitive
				 */
				return new BooleanPrimitiveDependency().getMinimumDependency(
						expression.getExpressionFilteredByType(
								BooleanPrimitive.class).get(0), variable);
			}
				/*
				 * all other grammar types
				 */
			default: {
				/*
				 * check if expression contains the given variable
				 */
				if (expression.getVariables().contains(variable)) {
					/*
					 * return the variable count
					 */
					return expression.getVariables().size() - 1;
				}
			}
			}
		} catch (TMQLRuntimeException e) {
			throw new TMQLOptimizationException(e);
		}
		return 9999;
	}
}