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
package de.topicmapslab.tmql4j.extension.tmml.interpreter;

import java.util.Map;

import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.extension.tmml.exception.UpdateException;
import de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.TopicDefinition;
import de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.UpdateClause;
import de.topicmapslab.tmql4j.extension.tmml.util.UpdateHandler;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.model.context.IVariableSet;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.parser.core.expressions.PredicateInvocation;
import de.topicmapslab.tmql4j.parser.core.expressions.ValueExpression;

/**
 * 
 * Special interpreter class to interpret update-clauses.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * update-clause ::= anchor [parameter] ( SET | ADD ) value-expression
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class UpdateClauseInterpreter extends
		ExpressionInterpreterImpl<UpdateClause> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public UpdateClauseInterpreter(UpdateClause ex) {
		super(ex);
	}

	@Override
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		switch (getGrammarTypeOfExpression()) {
		case UpdateClause.TOPIC_ADD: {
			interpretTopicDefinition(runtime);
		}
			break;
		case UpdateClause.ASSOCIATION_ADD: {
			interpretAssocationDefinition(runtime);
		}
			break;
		default: {
			interpretContentModification(runtime);
		}
		}
	}

	/**
	 * The method is called if the update-clause does represent a topic
	 * definition.
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
	private void interpretTopicDefinition(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * store of update-results
		 */
		QueryMatches results = new QueryMatches(runtime);
		long count = 0;

		for (IExpressionInterpreter<TopicDefinition> interpreter : getInterpretersFilteredByEypressionType(
				runtime, TopicDefinition.class)) {
			/*
			 * push to stack
			 */
			runtime.getRuntimeContext().push();
			/*
			 * run interpreter
			 */
			interpreter.interpret(runtime);
			/*
			 * pop from stack
			 */
			IVariableSet set = runtime.getRuntimeContext().pop();
			if (set.contains(VariableNames.QUERYMATCHES)) {
				QueryMatches r = (QueryMatches) set
						.getValue(VariableNames.QUERYMATCHES);
				if (!r.isEmpty()) {
					Object obj = r.get(0).get(
							QueryMatches.getNonScopedVariable());
					if (obj instanceof Long) {
						count += (Long) obj;
					}
				}
			}
		}

		/*
		 * create result of update-expression as count of performed changes
		 */
		Map<String, Object> result = HashUtil.getHashMap();
		result.put(QueryMatches.getNonScopedVariable(), count);
		results.add(result);

		/*
		 * set to stack
		 */
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);
	}

	/**
	 * The method is called if the update-clause does represent an association
	 * definition.
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
	private void interpretAssocationDefinition(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * store of update-results
		 */
		QueryMatches results = new QueryMatches(runtime);
		for (IExpressionInterpreter<PredicateInvocation> interpreter : getInterpretersFilteredByEypressionType(
				runtime, PredicateInvocation.class)) {
			/*
			 * push to stack
			 */
			runtime.getRuntimeContext().push();
			/*
			 * run interpreter
			 */
			interpreter.interpret(runtime);
			/*
			 * pop from stack
			 */
			IVariableSet set = runtime.getRuntimeContext().pop();
			if (set.contains(VariableNames.QUERYMATCHES)) {
				QueryMatches r = (QueryMatches) set
						.getValue(VariableNames.QUERYMATCHES);
				if (!r.isEmpty()) {
					Object obj = r.get(0).get(
							QueryMatches.getNonScopedVariable());
					/*
					 * create result of update-expression as count of performed
					 * changes
					 */
					Map<String, Object> result = HashUtil.getHashMap();
					result.put(QueryMatches.getNonScopedVariable(), obj);
					results.add(result);
				}
			}
		}
		/*
		 * set to stack
		 */
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);
	}

	/**
	 * The method is called if the update-clause does not represent the insert
	 * of a topic or an association definition.
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
	private void interpretContentModification(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		Object obj = runtime.getRuntimeContext().peek().getValue(
				VariableNames.ITERATED_BINDINGS);
		if (!(obj instanceof QueryMatches)) {
			throw new TMQLRuntimeException("Missing iteration results.");
		}

		QueryMatches matches = (QueryMatches) obj;

		/*
		 * store of update-results
		 */
		QueryMatches results = new QueryMatches(runtime);
		long count = 0;
		/*
		 * iterate over all tuple sequences returned by the where-clause
		 */
		for (Map<String, Object> tuple : matches) {
			/*
			 * get modification context
			 */
			Object context = tuple.get(getExpression().getVariableName());
			/*
			 * check if context variables was projected to other name by
			 * internal system method
			 */
			if (context == null
					&& matches.getOrigin(getExpression().getVariableName()) != null) {
				context = tuple.get(matches.getOrigin(getExpression()
						.getVariableName()));
			}
			runtime.getRuntimeContext().push();
			/*
			 * swap iterated bindings to current tuple
			 */
			QueryMatches match = new QueryMatches(runtime);
			match.add(tuple);
			match.setOrigins(matches.getOrigins());
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.ITERATED_BINDINGS, match);

			/*
			 * get values to add or set
			 */
			QueryMatches values = interpretValueExpression(runtime);

			Class<? extends IToken> anchor = getExpression().getAnchor();

			/*
			 * check optionalType
			 */
			String optionalType_ = ((UpdateClause) getExpression())
					.getOptionalType();
			Topic optionalType = null;
			if (optionalType_ != null) {
				try {
					optionalType = (Topic) runtime.getDataBridge()
							.getConstructByIdentifier(runtime, optionalType_);
				} catch (Exception e) {
					try {
						optionalType = runtime.getTopicMap().createTopicBySubjectIdentifier(
								runtime.getTopicMap().createLocator(
										runtime.getLanguageContext().getPrefixHandler().toAbsoluteIRI(optionalType_)));			
						count++;
					} catch (Exception e2) {
						throw new UpdateException(e);
					}
				}
			}
			/*
			 * iterate over all values to set or add
			 */
			for (Object val : values.getPossibleValuesForVariable()) {
				/*
				 * perform update
				 */
				count += new UpdateHandler(runtime).update(context, val,
						anchor, optionalType,
						getGrammarTypeOfExpression() == UpdateClause.TYPE_SET);				
			}

			runtime.getRuntimeContext().pop();
		}
		/*
		 * create result of update-expression as count of performed
		 * changes
		 */
		Map<String, Object> result = HashUtil.getHashMap();
		result.put(QueryMatches.getNonScopedVariable(), count);
		results.add(result);
		/*
		 * set to stack
		 */
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);

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
	private QueryMatches interpretValueExpression(TMQLRuntime runtime)
			throws TMQLRuntimeException {

		/*
		 * get expression interpreter for navigation
		 */
		IExpressionInterpreter<ValueExpression> value = getInterpretersFilteredByEypressionType(
				runtime, ValueExpression.class).get(0);

		/*
		 * push to stack
		 */
		runtime.getRuntimeContext().push();

		/*
		 * interpret
		 */
		value.interpret(runtime);

		/*
		 * get results from stack
		 */
		IVariableSet set = runtime.getRuntimeContext().pop();

		Object obj = set.getValue(VariableNames.QUERYMATCHES);
		if (!(obj instanceof QueryMatches)) {
			throw new TMQLRuntimeException(
					"Invalid interpretation of value-expression. Has to return an instance of QueryMatches");
		}

		return (QueryMatches) obj;
	}

}
