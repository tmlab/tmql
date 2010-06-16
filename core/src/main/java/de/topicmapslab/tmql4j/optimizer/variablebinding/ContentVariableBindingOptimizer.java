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
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.lexer.token.Combination;
import de.topicmapslab.tmql4j.lexer.token.Equality;
import de.topicmapslab.tmql4j.lexer.token.Substraction;
import de.topicmapslab.tmql4j.parser.core.expressions.Content;
import de.topicmapslab.tmql4j.parser.core.expressions.QueryExpression;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Special implementation of a variable optimizer which is applicable for
 * content. The variable bindings are ordered in a way that unsuccessful
 * bindings will detect as fast as possible.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ContentVariableBindingOptimizer extends
		VariableBindingOptimizer<Content> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL runtime
	 */
	public ContentVariableBindingOptimizer(TMQLRuntime runtime) {
		super(runtime);
	}

	/**
	 * {@inheritDoc}
	 */
	protected java.util.Set<?> optimize(Content expression, String variable)
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
			case Content.TYPE_QUERY_EXPRESSION: {
				/*
				 * optimize variable binding by calling optimizer
				 */
				return new QueryExpressionVariableBindingOptimizer(getRuntime())
						.optimize((QueryExpression) subExpression, variable);
			}
				/*
				 * is content combination, subtraction or equality
				 */
			case Content.TYPE_SET_OPERATION: {
				/*
				 * switch by operator
				 */
				int index = expression.getIndexOfOperator();
				if (index != -1) {
					Class<? extends IToken> token = expression.getTmqlTokens()
							.get(index);
					/*
					 * is combination
					 */
					if (token.equals(Combination.class)) {
						Set<Object> bindings = HashUtil.getHashSet();
						/*
						 * combine possible bindings of all contained
						 * expressions
						 */
						for (Content content : expression
								.getExpressionFilteredByType(Content.class)) {
							if (content.getVariables().contains(variable)) {
								bindings
										.addAll(new ContentVariableBindingOptimizer(
												getRuntime()).optimize(content,
												variable));
							}
						}
					}
					/*
					 * is subtraction or equality
					 */
					else if (token.equals(Substraction.class)
							|| token.equals(Equality.class)) {
						/*
						 * return all possible bindings
						 */
						return getMaximumBindings();
					}
				}
			}
				/*
				 * return all possible bindings for all other grammar types
				 */
			case Content.TYPE_CTM_EXPRESSION:
			case Content.TYPE_NONCANONICAL_CONDITIONAL_EXPRESSION:
			case Content.TYPE_XML_EXPRESSION:
			case Content.TYPE_CONDITIONAL_EXPRESSION:
			default:
				return getMaximumBindings();
			}
		} catch (TMQLRuntimeException e) {
			throw new TMQLOptimizationException(e);
		}
	};

}
