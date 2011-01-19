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

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.Locator;
import org.tmapi.core.MalformedIRIException;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.PreparedExpression;
import de.topicmapslab.tmql4j.path.grammar.lexical.DatatypedElement;
import de.topicmapslab.tmql4j.update.grammar.productions.PredicateInvocation;
import de.topicmapslab.tmql4j.update.grammar.productions.TopicDefinition;
import de.topicmapslab.tmql4j.update.grammar.productions.UpdateClause;
import de.topicmapslab.tmql4j.update.grammar.productions.ValueExpression;
import de.topicmapslab.tmql4j.update.util.UpdateHandler;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.XmlSchemeDatatypes;

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
public class UpdateClauseInterpreter extends ExpressionInterpreterImpl<UpdateClause> {

	/**
	 * the internal used variable
	 */
	private static final String DATATYPE = "$_datatype";
	/**
	 * the logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(PredicateInvocationRolePlayerExpressionInterpreter.class);

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public UpdateClauseInterpreter(UpdateClause ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		switch (getGrammarTypeOfExpression()) {
			case UpdateClause.TOPIC_ADD: {
				return interpretTopicDefinition(runtime, context, optionalArguments);
			}
			case UpdateClause.ASSOCIATION_ADD: {
				return interpretAssocationDefinition(runtime, context, optionalArguments);
			}
			default: {
				return interpretContentModification(runtime, context, optionalArguments);
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
	 * @param context
	 *            the querying context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the query matches containing the results of the interpretation of
	 *         the sub-expression
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretTopicDefinition(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * store of update-results
		 */
		long count = 0;
		for (IExpressionInterpreter<TopicDefinition> interpreter : getInterpretersFilteredByEypressionType(runtime, TopicDefinition.class)) {
			/*
			 * run interpreter
			 */
			QueryMatches matches = interpreter.interpret(runtime, context, optionalArguments);
			List<Object> possibleValuesForVariable = matches.getPossibleValuesForVariable();
			if (!possibleValuesForVariable.isEmpty()) {
				Object obj = possibleValuesForVariable.get(0);
				if (obj instanceof Long) {
					count += (Long) obj;
				}
			}
		}
		return QueryMatches.asQueryMatchNS(runtime, count);
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
	 * @param context
	 *            the querying context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the query matches containing the results of the interpretation of
	 *         the sub-expression
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretAssocationDefinition(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * store of update-results
		 */
		List<Object> results = HashUtil.getList();
		for (IExpressionInterpreter<PredicateInvocation> interpreter : getInterpretersFilteredByEypressionType(runtime, PredicateInvocation.class)) {

			/*
			 * run interpreter
			 */
			QueryMatches matches = interpreter.interpret(runtime, context, optionalArguments);
			List<Object> possibleValuesForVariable = matches.getPossibleValuesForVariable();
			if (!possibleValuesForVariable.isEmpty()) {
				results.add(possibleValuesForVariable.get(0));
			} else {
				results.addAll(matches.getMatches());
			}
		}
		return QueryMatches.asQueryMatchNS(runtime, results.toArray());
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
	 * @param context
	 *            the querying context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the query matches containing the results of the interpretation of
	 *         the sub-expression
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretContentModification(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		if (context.getContextBindings() == null) {
			logger.warn("Context of modification is missing!");
			return QueryMatches.emptyMatches();
		}
		/*
		 * store number of updates
		 */
		long count = 0;
		/*
		 * iterate over all tuple sequences returned by the where-clause
		 */
		for (Map<String, Object> tuple : context.getContextBindings()) {
			/*
			 * get modification context
			 */
			Object node = tuple.get(getExpression().getVariableName());
			/*
			 * check if context variables was projected to other name by
			 * internal system method
			 */
			if (node == null) {
				if (context.getContextBindings().getOrigin(getExpression().getVariableName()) != null) {
					node = tuple.get(context.getContextBindings().getOrigin(getExpression().getVariableName()));
				} else {
					logger.warn("Context of modification is missing!");
					return QueryMatches.emptyMatches();
				}
			}
			Context newContext = new Context(context);
			newContext.setContextBindings(null);
			newContext.setCurrentTuple(tuple);
			newContext.setCurrentNode(node);
			/*
			 * get values to add or set
			 */
			QueryMatches values = interpretValueExpression(runtime, newContext, optionalArguments);
			Class<? extends IToken> anchor = getExpression().getAnchor();
			/*
			 * check optionalType
			 */
			String optionalType_ = ((UpdateClause) getExpression()).getOptionalType();
			Topic optionalType = null;
			if (optionalType_ != null) {
				optionalType = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, optionalType_);
				if (optionalType == null) {
					optionalType = context.getQuery().getTopicMap()
							.createTopicBySubjectIdentifier(context.getQuery().getTopicMap().createLocator(runtime.getConstructResolver().toAbsoluteIRI(context, optionalType_)));
					count++;
				}
			}
			/*
			 * is wildcard
			 */
			else if (containsExpressionsType(PreparedExpression.class)) {
				QueryMatches matches = extractArguments(runtime, PreparedExpression.class, 0, context, optionalArguments);
				if (matches.isEmpty()) {
					throw new TMQLRuntimeException("Prepared statement has to be bound to a value!");
				}
				Object obj = matches.getFirstValue();
				if (obj instanceof Topic) {
					optionalType = (Topic) obj;
				} else if (obj instanceof String) {
					optionalType = runtime.getConstructResolver().getTopicBySubjectIdentifier(newContext, (String) obj);
					if (optionalType == null) {
						optionalType = context.getQuery().getTopicMap()
								.createTopicBySubjectIdentifier(context.getQuery().getTopicMap().createLocator(runtime.getConstructResolver().toAbsoluteIRI(context, (String) obj)));
						count++;
					}
				} else {
					throw new TMQLRuntimeException("Invalid result of prepared statement, expects a string literal");
				}
			}
			/*
			 * iterate over all values to set or add
			 */
			for (Map<String, Object> vTuple : values) {
				/*
				 * missing value
				 */
				if (!vTuple.containsKey(QueryMatches.getNonScopedVariable())) {
					continue;
				}
				Locator optionalDatatype = vTuple.containsKey(DATATYPE) ? (Locator) vTuple.get(DATATYPE) : null;
				/*
				 * perform update
				 */
				count += new UpdateHandler(runtime, context).update(node, vTuple.get(QueryMatches.getNonScopedVariable()), anchor, optionalType, getGrammarTypeOfExpression() == UpdateClause.TYPE_SET,
						optionalDatatype);
			}
		}
		return QueryMatches.asQueryMatchNS(runtime, count);
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
	private QueryMatches interpretValueExpression(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {

		/*
		 * get expression interpreter for navigation
		 */
		IExpressionInterpreter<ValueExpression> value = getInterpretersFilteredByEypressionType(runtime, ValueExpression.class).get(0);

		/*
		 * interpret
		 */
		QueryMatches matches = value.interpret(runtime, context, optionalArguments);
		if (value.getTmqlTokens().get(0).equals(DatatypedElement.class) && value.getTmqlTokens().size() == 1) {
			StringTokenizer tokenizer = new StringTokenizer(value.getTokens().get(0), "^^");
			tokenizer.nextToken();
			try {
				Locator loc = context.getQuery().getTopicMap().createLocator(XmlSchemeDatatypes.toExternalForm(tokenizer.nextToken()));
				for (Map<String, Object> tuple : matches) {
					tuple.put(DATATYPE, loc);
				}
			} catch (MalformedIRIException e) {
				logger.warn("The given datatype is invalid: " + e.getLocalizedMessage());
				return QueryMatches.emptyMatches();
			}
		}

		return matches;
	}

}
