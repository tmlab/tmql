/**
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2009 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.delete.components.interpreter;

import static de.topicmapslab.tmql4j.delete.grammar.productions.DeleteExpression.TYPE_ALL;
import static de.topicmapslab.tmql4j.delete.grammar.productions.DeleteExpression.TYPE_WITHOUT_WHERE_CLAUSE;
import static de.topicmapslab.tmql4j.delete.grammar.productions.DeleteExpression.TYPE_WITH_WHERE_CLAUSE;

import java.util.Map;
import java.util.Set;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.delete.grammar.productions.DeleteClause;
import de.topicmapslab.tmql4j.delete.grammar.productions.DeleteExpression;
import de.topicmapslab.tmql4j.delete.grammar.productions.WhereClause;
import de.topicmapslab.tmql4j.delete.util.DeletionHandler;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * 
 * Special interpreter class to interpret delete-expression.
 * <p>
 * The grammar production rule of the expression is: <code>
 * delete-expression ::= DELETE [CASCADE] &lt;value-expression&gt; [ WHERE boolean-expression ]
 * </code>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class DeleteExpressionInterpreter extends ExpressionInterpreterImpl<DeleteExpression> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public DeleteExpressionInterpreter(DeleteExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		Context newContext = new Context(context);
		/*
		 * switch by grammar type
		 */
		switch (getGrammarTypeOfExpression()) {
		/*
		 * is expression DELETE ALL
		 */
		case TYPE_ALL: {
			Set<String> ids = new DeletionHandler(runtime, context).deleteAll();
			context.getTmqlProcessor().getResultProcessor().setAutoReduction(false);
			QueryMatches matches = new QueryMatches(runtime);
			Map<String, Object> tuple = HashUtil.getHashMap();
			tuple.put("$0", ids.size());
			tuple.put("$1", ids);
			matches.add(tuple);
			return matches;
		}
			/*
			 * is delete-expression
			 */
		case TYPE_WITH_WHERE_CLAUSE: {
			QueryMatches bindings = interpretWhereClause(runtime, context, optionalArguments);
			newContext.setContextBindings(bindings);
		}
		case TYPE_WITHOUT_WHERE_CLAUSE: {
			return interpretDeleteClause(runtime, newContext, optionalArguments);
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
	 *            the context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the query matches containing the results of the interpretation of
	 *         the sub-expression
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretWhereClause(ITMQLRuntime runtime, IContext context, Object optionalArguments) throws TMQLRuntimeException {
		IExpressionInterpreter<WhereClause> whereclause = getInterpretersFilteredByEypressionType(runtime, WhereClause.class).get(0);
		return whereclause.interpret(runtime, context, optionalArguments);
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
	 *            the context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the query matches containing the results of the interpretation of
	 *         the sub-expression
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretDeleteClause(ITMQLRuntime runtime, IContext context, Object optionalArguments) throws TMQLRuntimeException {
		/*
		 * extract the select clause
		 */
		IExpressionInterpreter<DeleteClause> deleteClause = getInterpretersFilteredByEypressionType(runtime, DeleteClause.class).get(0);
		/*
		 * call sub expression
		 */
		return deleteClause.interpret(runtime, context, optionalArguments);
	}

}
