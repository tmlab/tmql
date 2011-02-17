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
package de.topicmapslab.tmql4j.update.components.interpreter;

import java.util.Map;
import java.util.Map.Entry;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultProcessor;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.update.grammar.productions.UpdateClause;
import de.topicmapslab.tmql4j.update.grammar.productions.UpdateExpression;
import de.topicmapslab.tmql4j.update.grammar.productions.WhereClause;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * 
 * Special interpreter class to interpret update-expressions.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 *  * <p>
 * update-expression ::= UPDATE anchor [parameter] ( SET | ADD ) value-expression ( ',' anchor [parameter] ( SET | ADD ) value-expression )? WHERE boolean-expression
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class UpdateExpressionInterpreter extends ExpressionInterpreterImpl<UpdateExpression> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public UpdateExpressionInterpreter(UpdateExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		Context newContext = new Context(context);
		/*
		 * execute the contained expressions
		 */
		if (containsExpressionsType(WhereClause.class)) {
			QueryMatches matches = interpretWhereClause(runtime, newContext, optionalArguments);
			newContext.setContextBindings(matches);
		}

		QueryMatches results = new QueryMatches(runtime);
		IResultProcessor resultProcessor = context.getTmqlProcessor().getResultProcessor();
		/*
		 * iterate over update-clauses
		 */
		for (IExpressionInterpreter<UpdateClause> interpreter : getInterpretersFilteredByEypressionType(runtime, UpdateClause.class)) {
			/*
			 * call update-clause
			 */
			QueryMatches matches = interpreter.interpret(runtime, newContext, optionalArguments);
			/*
			 * translate to new indexes
			 */
			for (Map<String, Object> tuple : matches) {
				Map<String, Object> tuple_ = HashUtil.getHashMap();
				for (Entry<String, Object> entry : tuple.entrySet()) {
					int index = resultProcessor.getIndexOfAlias(entry.getKey());
					if (index == -1) {
						throw new TMQLRuntimeException("The update clause may define an alias index!");
					}
					tuple_.put("$" + index, entry.getValue());
				}
				results.add(tuple_);
			}
		}
		context.getTmqlProcessor().getResultProcessor().setAutoReduction(false);
		return results;
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
	 *            the querying context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the query matches containing the results of the interpretation of
	 *         the sub-expression
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretWhereClause(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * get expression interpreter for where-clause
		 */
		IExpressionInterpreter<WhereClause> interpreter = getInterpretersFilteredByEypressionType(runtime, WhereClause.class).get(0);

		/*
		 * redirect to where-clause interpreter interpret
		 */
		return interpreter.interpret(runtime, context, optionalArguments);
	}

}
