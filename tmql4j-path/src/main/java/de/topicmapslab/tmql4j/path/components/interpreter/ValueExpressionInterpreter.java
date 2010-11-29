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

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.lexical.Asc;
import de.topicmapslab.tmql4j.path.grammar.productions.FunctionInvocation;
import de.topicmapslab.tmql4j.path.grammar.productions.ValueExpression;

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
public class ValueExpressionInterpreter extends
		ExpressionInterpreterImpl<ValueExpression> {

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
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * switch by grammar type
		 */
		switch (getGrammarTypeOfExpression()) {
		/*
		 * is infix-operator expression
		 */
		case TYPE_INFIX_OPERATOR: {
			interpretInfixOpertor(runtime);
		}
			break;
		/*
		 * is prefix-operator expression
		 */
		case TYPE_PREFIX_OPERATOR: {
			interpretPrefixOpertor(runtime);
		}
			break;
		/*
		 * is function-invocation
		 */
		case TYPE_FUNCTION_INVOCATION: {
			interpretFunctionInvocation(runtime);
		}
			break;
		/*
		 * is content
		 */
		case TYPE_CONTENT: {
			interpretContent(runtime);
		}
			break;
		default:
			throw new TMQLRuntimeException("Unexprected state!");
		}
		;
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
	private void interpretPrefixOpertor(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * call value expression
		 */
		QueryMatches argument = extractArguments(runtime,
				ValueExpression.class, 0);
		/*
		 * call operator-handler to interpret operator
		 */
		QueryMatches results = QueryMatchUtils.operation(runtime,
				getTmqlTokens().get(indexOfOperator), argument);
		/*
		 * store result
		 */
		runtime.getRuntimeContext().peek().setValue(
				VariableNames.QUERYMATCHES, results);
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
	private void interpretInfixOpertor(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * call value expressions
		 */
		QueryMatches[] arguments = extractArguments(runtime,
				ValueExpression.class);

		/*
		 * call operator-handler to interpret operator
		 */
		QueryMatches results = QueryMatchUtils.operation(runtime,
				getTmqlTokens().get(indexOfOperator), arguments[0],
				arguments[1]);

		/*
		 * store results
		 */
		runtime.getRuntimeContext().peek().setValue(
				VariableNames.QUERYMATCHES, results);
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
	private void interpretFunctionInvocation(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * redirect to subexpression
		 */
		IExpressionInterpreter<FunctionInvocation> ex = getInterpretersFilteredByEypressionType(
				runtime, FunctionInvocation.class).get(0);
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
	private void interpretContent(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * call content
		 */
		QueryMatches content = extractArguments(runtime, Content.class, 0);

		/*
		 * check if content should be ordered
		 */
		if (((ValueExpression) getExpression()).isAscOrDescOrdering()) {
			content = content.orderBy(getTmqlTokens().get(
					getTmqlTokens().size() - 1).equals(Asc.class));
		}

		/*
		 * store results
		 */
		runtime.getRuntimeContext().peek().setValue(
				VariableNames.QUERYMATCHES, content);
	}
}
