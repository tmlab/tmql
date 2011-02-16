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

import java.util.Collection;
import java.util.Map;
import java.util.Set;
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
import de.topicmapslab.tmql4j.path.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.path.grammar.lexical.Datatype;
import de.topicmapslab.tmql4j.path.grammar.lexical.DatatypedElement;
import de.topicmapslab.tmql4j.update.components.results.IUpdateAlias;
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
 * update-clause ::= anchor [parameter] ( SET | ADD | REMOVE ) value-expression
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
		QueryMatches results = new QueryMatches(runtime);
		for (IExpressionInterpreter<TopicDefinition> interpreter : getInterpretersFilteredByEypressionType(runtime, TopicDefinition.class)) {
			/*
			 * run interpreter
			 */
			QueryMatches matches = interpreter.interpret(runtime, context, getExpression().getOperator());
			results.add(matches.getMatches());
		}
		return results;
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
		QueryMatches results = new QueryMatches(runtime);
		for (IExpressionInterpreter<PredicateInvocation> interpreter : getInterpretersFilteredByEypressionType(runtime, PredicateInvocation.class)) {
			/*
			 * run interpreter
			 */
			QueryMatches matches = interpreter.interpret(runtime, context, optionalArguments);
			results.add(matches.getMatches());
		}
		return results;
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
	@SuppressWarnings("unchecked")
	private QueryMatches interpretContentModification(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		if (context.getContextBindings() == null) {
			logger.warn("Context of modification is missing!");
			return QueryMatches.emptyMatches();
		}
		QueryMatches results = new QueryMatches(runtime);
		/*
		 * iterate over all tuple sequences returned by the where-clause
		 */
		for (Map<String, Object> tuple : context.getContextBindings()) {
			Set<String> topicIds = HashUtil.getHashSet();
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
			Object optionalType = ((UpdateClause) getExpression()).getOptionalType();
			/*
			 * is wildcard
			 */
			if (containsExpressionsType(PreparedExpression.class)) {
				QueryMatches matches = extractArguments(runtime, PreparedExpression.class, 0, context, optionalArguments);
				if (matches.isEmpty()) {
					throw new TMQLRuntimeException("Prepared statement has to be bound to a value!");
				}
				Object obj = matches.getFirstValue();
				if (obj instanceof Topic) {
					optionalType = (Topic) obj;
				} else if (obj instanceof String) {
					optionalType = obj;
				} else {
					throw new TMQLRuntimeException("Invalid result of prepared statement, expects a string literal");
				}
			}
			/*
			 * iterate over all values to set or add
			 */
			for (Map<String, Object> vTuple : values) {
				Object value = vTuple.get(QueryMatches.getNonScopedVariable());
				/*
				 * missing value
				 */
				if (value == null) {
					continue;
				}
				Locator optionalDatatype = vTuple.containsKey(DATATYPE) ? (Locator) vTuple.get(DATATYPE) : null;
				/*
				 * check if string contains datatype
				 */
				if (optionalDatatype == null && value.toString().contains("^^")) {
					/*
					 * split string into value and datatype
					 */
					int i = value.toString().indexOf("^^");
					String ref = value.toString().substring(i + 2);
					ref = XmlSchemeDatatypes.toExternalForm(ref);
					value = value.toString().substring(0, i);
					try {
						optionalDatatype = context.getQuery().getTopicMap().createLocator(ref);
					} catch (Exception e) {
						throw new TMQLRuntimeException("Cannot generate the datatype reference for " + ref + ".");
					}
				}
				/*
				 * perform update
				 */
				QueryMatches result = new UpdateHandler(runtime, context).update(node, value, anchor, optionalType, getExpression().getOperator() ,optionalDatatype);
				if (!result.isEmpty()) {
					if (!topicIds.isEmpty()) {
						for (Map<String, Object> match : result) {
							if (match.containsKey(IUpdateAlias.TOPICS)) {
								if (match.get(IUpdateAlias.TOPICS) instanceof String) {
									Set<String> set = HashUtil.getHashSet(topicIds);
									set.add(match.get(IUpdateAlias.TOPICS).toString());
									match.put(IUpdateAlias.TOPICS, set);
								} else if (match.get(IUpdateAlias.TOPICS) instanceof Collection<?>) {
									Set<String> set = HashUtil.getHashSet(topicIds);
									set.addAll((Collection<String>) match.get(IUpdateAlias.TOPICS));
									match.put(IUpdateAlias.TOPICS, set);
								}
							}
						}
					}
					results.add(result.getMatches());
				}
			}
		}
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
	@SuppressWarnings("unchecked")
	private QueryMatches interpretValueExpression(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {

		/*
		 * get expression interpreter for navigation
		 */
		IExpressionInterpreter<ValueExpression> value = getInterpretersFilteredByEypressionType(runtime, ValueExpression.class).get(0);

		/*
		 * interpret
		 */
		QueryMatches matches = value.interpret(runtime, context, optionalArguments);
		/*
		 * check for datatype as part of a datatyped-element
		 */
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
		/*
		 * datatype is provided as separate symbol
		 */
		else if (ParserUtils.containsTokens(value.getTmqlTokens(), Datatype.class)) {
			int index = ParserUtils.indexOfTokens(value.getTmqlTokens(), Datatype.class);
			String token = value.getTokens().get(index).substring(2);
			try {
				Locator loc = context.getQuery().getTopicMap().createLocator(XmlSchemeDatatypes.toExternalForm(token));
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
