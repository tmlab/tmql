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
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.productions.Postfix;
import de.topicmapslab.tmql4j.path.grammar.productions.PostfixedExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.TupleExpression;

/**
 * 
 * Special interpreter class to interpret postfixed-expressions.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 *  postfixed-expression ::= ( tuple-expression | simple-content ) { postfix }
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PostfixedExpressionInterpreter extends
		ExpressionInterpreterImpl<PostfixedExpression> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public PostfixedExpressionInterpreter(PostfixedExpression ex) {
		super(ex);
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
		 * is tuple-expression
		 */
		case PostfixedExpression.TYPE_TUPLE_EXPRESSSION: {
			interpretTupleExpression(runtime);
		}
			break;
		/*
		 * is simple-content
		 */
		case PostfixedExpression.TYPE_SIMPLE_CONTENT: {
			interpretSimpleContent(runtime);
		}
			break;
		default:
			throw new TMQLRuntimeException("Unknown state.");
		}

		/*
		 * check if expression contains a postfix
		 */
		if (getInterpreters(runtime).size() > 1) {
			/*
			 * interpret postfix
			 */
			interpretPostfix(runtime);
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
	private void interpretTupleExpression(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * redirect to subexpression
		 */
		IExpressionInterpreter<TupleExpression> ex = getInterpretersFilteredByEypressionType(
				runtime, TupleExpression.class).get(0);
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
	private void interpretSimpleContent(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * redirect to subexpression
		 */
		IExpressionInterpreter<SimpleContent> ex = getInterpretersFilteredByEypressionType(
				runtime, SimpleContent.class).get(0);
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
	private void interpretPostfix(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * extract current matches
		 */
		IVariableSet set = runtime.getRuntimeContext().peek();

		if (!set.contains(VariableNames.QUERYMATCHES)) {
			throw new TMQLRuntimeException(
					"mising projection context, variable %%%___ not set.");
		}

		/*
		 * set current matches to variable %_postfixed
		 */
		runtime.getRuntimeContext().push().setValue(VariableNames.POSTFIXED,
				set.getValue(VariableNames.QUERYMATCHES));

		/*
		 * call postfix-interpreter
		 */
		IExpressionInterpreter<Postfix> ex = getInterpretersFilteredByEypressionType(
				runtime, Postfix.class).get(0);
		ex.interpret(runtime);

		/*
		 * redirect results
		 */
		set = runtime.getRuntimeContext().pop();
		if (!set.contains(VariableNames.QUERYMATCHES)) {
			throw new TMQLRuntimeException(
					"mising interpetation result of postfix.");
		}

		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				set.getValue(VariableNames.QUERYMATCHES));

	}

}
