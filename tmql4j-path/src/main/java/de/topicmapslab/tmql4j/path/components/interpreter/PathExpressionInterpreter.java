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
import de.topicmapslab.tmql4j.path.grammar.productions.AKOExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.ISAExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.PathExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.PostfixedExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.PredicateInvocation;

/**
*
* Special interpreter class to interpret path-expressions.
* 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * path-expression ::= postfixed-expression
 * </p>
 * <p>
 * path-expression ::= predicate-invocation
 * </p>
 * <p>
 * path-expression ::= // anchor { postfix } ==> %_ // anchor { postfix }
 * </p>
 * <p>
 * path-expression ::= simple-content-1 AKO simple-content-2 ==> tm:subclass-of
 * ( tm:subclass : simple-content-1 , tm:superclass : simple-content-2 )
 * </p>
 * <p>
 * path-expression ::= simple-content-1 ISA simple-content-2 ==>
 * tm:type-instance ( tm:instance : simple-content-1 , tm:type :
 * simple-content-2 )
 * </p>
 * </code> </p>
* @author Sven Krosse
* @email krosse@informatik.uni-leipzig.de
*
*/
public class PathExpressionInterpreter
		extends ExpressionInterpreterImpl<PathExpression> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public PathExpressionInterpreter(PathExpression ex) {
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
		 * is postfix-expression
		 */
		case 0: {
			interpretPostfixExpression(runtime);
		}
			break;
			/*
			 * is predicate-invocation
			 */
		case 1: {
			interpretPredicateExpression(runtime);
		}
			break;
		case 2: {
			/*
			 * is non-canonical type anchor //
			 */
			interpretPostfixExpression(runtime);
		}
			break;
			/*
			 * is AKO-expression
			 */
		case 3: {
			interpretAKOExpression(runtime);
		}
			break;
			/*
			 * is ISA-expression
			 */
		case 4: {
			interpretISAExpression(runtime);
		}
			break;
		default:
			throw new TMQLRuntimeException("Unknown state");
		}
	}

	/**
	 * The method is called to
	 * interpret the given sub-expression by using the given runtime. The
	 * interpretation will call the sub-expression if the given expression isn't a
	 * leaf in parsing-tree.
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
	private void interpretPostfixExpression(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * redirect to subexpression
		 */
		IExpressionInterpreter<PostfixedExpression> ex = getInterpretersFilteredByEypressionType(runtime,
				PostfixedExpression.class).get(0);
		ex.interpret(runtime);
	}

	/**
	 * The method is called to
	 * interpret the given sub-expression by using the given runtime. The
	 * interpretation will call the sub-expression if the given expression isn't a
	 * leaf in parsing-tree.
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
	private void interpretPredicateExpression(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * redirect to subexpression
		 */
		IExpressionInterpreter<PredicateInvocation> ex = getInterpretersFilteredByEypressionType(runtime,
				PredicateInvocation.class).get(0);
		ex.interpret(runtime);
	}

	/**
	 * The method is called to
	 * interpret the given sub-expression by using the given runtime. The
	 * interpretation will call the sub-expression if the given expression isn't a
	 * leaf in parsing-tree.
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
	private void interpretISAExpression(TMQLRuntime runtime)
			throws TMQLRuntimeException {

		/*
		 * redirect to subexpression
		 */
		IExpressionInterpreter<ISAExpression> ex = getInterpretersFilteredByEypressionType(runtime,
				ISAExpression.class).get(0);
		ex.interpret(runtime);
	}

	/**
	 * The method is called to
	 * interpret the given sub-expression by using the given runtime. The
	 * interpretation will call the sub-expression if the given expression isn't a
	 * leaf in parsing-tree.
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
	private void interpretAKOExpression(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * Call subexpression
		 */
		IExpressionInterpreter<AKOExpression> ex = getInterpretersFilteredByEypressionType(runtime,
				AKOExpression.class).get(0);
		ex.interpret(runtime);
	}

}
