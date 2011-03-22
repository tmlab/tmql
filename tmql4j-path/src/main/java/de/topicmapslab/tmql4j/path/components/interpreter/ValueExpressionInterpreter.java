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
package de.topicmapslab.tmql4j.path.components.interpreter;

import static de.topicmapslab.tmql4j.path.grammar.productions.ValueExpression.TYPE_BOOLEAN;
import static de.topicmapslab.tmql4j.path.grammar.productions.ValueExpression.TYPE_CONTENT;
import static de.topicmapslab.tmql4j.path.grammar.productions.ValueExpression.TYPE_FUNCTION_INVOCATION;
import static de.topicmapslab.tmql4j.path.grammar.productions.ValueExpression.TYPE_INFIX_OPERATOR;
import static de.topicmapslab.tmql4j.path.grammar.productions.ValueExpression.TYPE_PREFIX_OPERATOR;
import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.lexical.Asc;
import de.topicmapslab.tmql4j.path.grammar.productions.BooleanExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.Content;
import de.topicmapslab.tmql4j.path.grammar.productions.FunctionInvocation;
import de.topicmapslab.tmql4j.path.grammar.productions.ValueExpression;
import de.topicmapslab.tmql4j.path.util.QueryMatchUtils;

/**
 * 
 * Special interpreter class to interpret value-expressions.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * value-expression ::= value-expression infix-operator value-expression (0)
 * </p>
 * <p>
 * value-expression ::= prefix-operator value-expression
 * </p>
 * <p>
 * value-expression ::= function-invocation
 * </p>
 * <p>
 * value-expression ::= content
 * </p>
 * <p>
 * infix-operator ::= + | - | * | % | mod | < | <= | > | >= | =~ | == | !=
 * </p>
 * <p>
 * prefix-operator ::= - |
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ValueExpressionInterpreter extends ExpressionInterpreterImpl<ValueExpression> {

	/**
	 * index of operator of value-expression is of an
	 * {@link ValueExpression#TYPE_INFIX_OPERATOR} or
	 * {@link ValueExpression#TYPE_PREFIX_OPERATOR}
	 */
	private final int indexOfOperator;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public ValueExpressionInterpreter(ValueExpression ex) {
		super(ex);
		indexOfOperator = ((ValueExpression) ex).getIndexOfOperator();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * switch by grammar type
		 */
		switch (getGrammarTypeOfExpression()) {
			/*
			 * is infix-operator expression
			 */
			case TYPE_INFIX_OPERATOR: {
				return interpretInfixOpertor(runtime, context, optionalArguments);
			}
				/*
				 * is prefix-operator expression
				 */
			case TYPE_PREFIX_OPERATOR: {
				return interpretPrefixOpertor(runtime, context, optionalArguments);
			}
				/*
				 * is function-invocation
				 */
			case TYPE_FUNCTION_INVOCATION: {
				return interpretFunctionInvocation(runtime, context, optionalArguments);
			}
				/*
				 * is content
				 */
			case TYPE_CONTENT: {
				return interpretContent(runtime, context, optionalArguments);
			}
				/*
				 * is boolean
				 */
			case TYPE_BOOLEAN: {
				return interpretBooleanExpression(runtime, context, optionalArguments);
			}
		}
		return QueryMatches.emptyMatches();
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
	 * @param context
	 *            the current querying context
	 * @param optionalArguments
	 *            optional arguments
	 * @return the query matches
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretPrefixOpertor(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * call value expression
		 */
		QueryMatches argument = extractArguments(runtime, ValueExpression.class, 0, context, optionalArguments);
		/*
		 * call operator-handler to interpret operator
		 */
		return QueryMatchUtils.operation(runtime, getTmqlTokens().get(indexOfOperator), argument);
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
	private QueryMatches interpretInfixOpertor(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * call value expressions
		 */
		QueryMatches[] arguments = extractArguments(runtime, ValueExpression.class, context, optionalArguments);

		/*
		 * call operator-handler to interpret operator
		 */
		return QueryMatchUtils.operation(runtime, getTmqlTokens().get(indexOfOperator), arguments[0], arguments[1]);
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
	 * @param context
	 *            the current querying context
	 * @param optionalArguments
	 *            optional arguments
	 * @return the query matches
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretBooleanExpression(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * redirect to subexpression
		 */
		IExpressionInterpreter<?> ex = getInterpretersFilteredByEypressionType(runtime, BooleanExpression.class).get(0);

		QueryMatches content = ex.interpret(runtime, context, optionalArguments);
		return QueryMatches.asQueryMatchNS(runtime, content.contains(true));
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
	 * @param context
	 *            the current querying context
	 * @param optionalArguments
	 *            optional arguments
	 * @return the query matches
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretFunctionInvocation(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * redirect to subexpression
		 */
		IExpressionInterpreter<FunctionInvocation> ex = getInterpretersFilteredByEypressionType(runtime, FunctionInvocation.class).get(0);
		QueryMatches content = ex.interpret(runtime, context, optionalArguments);
		/*
		 * check if content should be ordered
		 */
		if (((ValueExpression) getExpression()).isAscOrDescOrdering()) {
			content = content.orderBy(getTmqlTokens().get(getTmqlTokens().size() - 1).equals(Asc.class));
		}
		return content;
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
	 * @param context
	 *            the current querying context
	 * @param optionalArguments
	 *            optional arguments
	 * @return the query matches
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretContent(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * call content
		 */
		QueryMatches content = extractArguments(runtime, Content.class, 0, context, optionalArguments);

		/*
		 * check if content should be ordered
		 */
		if (((ValueExpression) getExpression()).isAscOrDescOrdering()) {
			content = content.orderBy(getTmqlTokens().get(getTmqlTokens().size() - 1).equals(Asc.class));
		}
		return content;
	}
}
