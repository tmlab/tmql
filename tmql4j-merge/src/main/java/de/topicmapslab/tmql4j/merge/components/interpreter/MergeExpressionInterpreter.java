/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.merge.components.interpreter;

import static de.topicmapslab.tmql4j.merge.grammar.productions.MergeExpression.TYPE_ALL;
import static de.topicmapslab.tmql4j.merge.grammar.productions.MergeExpression.TYPE_VALUEEXPRESSION;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.ModelConstraintException;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.merge.grammar.productions.MergeExpression;
import de.topicmapslab.tmql4j.merge.grammar.productions.WhereClause;
import de.topicmapslab.tmql4j.merge.util.MergeHandler;
import de.topicmapslab.tmql4j.path.grammar.productions.ValueExpression;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * 
 * Special interpreter class to interpret merge-expressions.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * merge-expression ::= MERGE path-expression
 * </p>
 * <p>
 * merge-expression ::= MERGE &lt;value-expression&gt; WHERE boolean-expression
 * </p>
 * <p>
 * merge-expression ::= MERGE ALL WHERE boolean-expression
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class MergeExpressionInterpreter extends ExpressionInterpreterImpl<MergeExpression> {

	/**
	 * the logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(MergeExpressionInterpreter.class);

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public MergeExpressionInterpreter(MergeExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * switch operation by grammar type of the production rule
		 */
		switch (getGrammarTypeOfExpression()) {
		/*
		 * merge-expression contains a number of value-expression
		 */
		case TYPE_VALUEEXPRESSION: {
			return interpretValueExpression(runtime, context, optionalArguments);
		}
			/*
			 * merge-expression contains a where-clause and the keyword ALL
			 */
		case TYPE_ALL: {
			return interpretAllExpression(runtime, context, optionalArguments);
		}
		}
		logger.warn("The set state of merge-expression is unknown!");
		return QueryMatches.emptyMatches();
	}

	/**
	 * Internal method to handle production rule for a merge-expression
	 * containing the keyword ALL and a where-clause
	 * 
	 * @param runtime
	 *            the current TMQL runtime
	 * @param context
	 *            the querying context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the query matches containing the results of the interpretation of
	 *         the sub-expression
	 * @throws TMQLRuntimeException
	 *             thrown in interpretation or the merge operation failed.
	 */
	private QueryMatches interpretAllExpression(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * interpret the where-clause
		 */
		QueryMatches matches = interpreteWhereClause(runtime, context, optionalArguments).unify();
		/*
		 * cache to store merged topics
		 */
		Set<Topic> cache = HashUtil.getHashSet();
		/*
		 * store topics to merge
		 */
		Set<Topic> topics = HashUtil.getHashSet();
		for (Map<String, Object> tuple : matches) {
			/*
			 * iterate over all entries
			 */
			for (Entry<String, Object> entry : tuple.entrySet()) {
				final String variable = entry.getKey();
				if (!variable.equalsIgnoreCase(QueryMatches.getNonScopedVariable())) {
					if (entry.getValue() instanceof Topic) {
						topics.add((Topic) entry.getValue());
					}
				}
			}
		}
		/*
		 * try to merge
		 */
		try {
			long amount = 0;
			Topic topic = null;
			for (Topic t : topics) {
				/*
				 * Workaround because of permutations { $t = topicA , $t' =
				 * topicB} == { $t = topicB , $t' = topicA } permutation can
				 * only merge one-times
				 */
				if (cache.contains(t)) {
					continue;
				} else if (topic == null) {
					topic = t;
				} else {
					topic.mergeIn(t);
					cache.add(t);
					amount++;
				}
			}
			return QueryMatches.asQueryMatchNS(runtime, amount);
		} catch (ModelConstraintException e) {
			logger.warn("Merging of topics failed!");
			throw new TMQLRuntimeException(e);
		}
	}

	/**
	 * Internal method to handle production rule for a merge-expression
	 * containing value-expressions and a where-clause
	 * 
	 * @param runtime
	 *            the current TMQL runtime
	 * @param context
	 *            the querying context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return the query matches containing the results of the interpretation of
	 *         the sub-expression
	 * @throws TMQLRuntimeException
	 *             thrown in interpretation or the merge operation failed.
	 */
	private QueryMatches interpretValueExpression(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * interpret the where-clause
		 */
		QueryMatches iteration = interpreteWhereClause(runtime, context, optionalArguments).unify();
		Collection<Construct> alreadyMerged = HashUtil.getHashSet();
		List<Long> values = HashUtil.getList();
		/*
		 * no binding defined
		 */
		if (iteration.isEmpty()) {
			QueryMatches arguments[] = extractArguments(runtime, ValueExpression.class, context, optionalArguments);
			long count = merge(runtime, arguments, alreadyMerged);
			if (count > 0) {
				values.add(count);
			}
		}
		/*
		 * binding set
		 */
		else {
			for (Map<String, Object> tuple : iteration) {
				Context newContext = new Context(context);
				newContext.setCurrentTuple(tuple);
				newContext.setContextBindings(null);
				QueryMatches arguments[] = extractArguments(runtime, ValueExpression.class, newContext, optionalArguments);
				long count = merge(runtime, arguments, alreadyMerged);
				if (count > 0) {
					values.add(count);
				}
			}
		}
		return QueryMatches.asQueryMatchNS(runtime, values.toArray());
	}

	/**
	 * Utility method to merge the topics or associations contained by the given
	 * query matches
	 * 
	 * @param runtime
	 *            the runtime
	 * @param arguments
	 *            the query matches containing the topics or associations to
	 *            merge
	 * @param alreadyMerged
	 *            a collection containing all merged constructs
	 * @return the number of merged construct
	 * @throws TMQLRuntimeException
	 *             thrown if operation fails
	 */
	private long merge(ITMQLRuntime runtime, QueryMatches[] arguments, Collection<Construct> alreadyMerged) throws TMQLRuntimeException {
		Set<Construct> candidates = HashUtil.getHashSet();
		for (QueryMatches match : arguments) {
			for (Object obj : match.getPossibleValuesForVariable()) {
				if (obj instanceof Topic || obj instanceof Association) {
					candidates.add((Construct) obj);
				} else {
					throw new TMQLRuntimeException("Unsupported merge content '" + (obj == null ? "null" : obj.getClass().getSimpleName()) + "'");
				}
			}
		}

		long count = MergeHandler.doMerge(candidates, alreadyMerged);
		return count;
	}

	/**
	 * Internal method to interpret contained where-clause of the
	 * merge-expression.
	 * 
	 * @param runtime
	 *            the current TMQL runtime
	 * @param context
	 *            the querying context
	 * @param optionalArguments
	 *            any optional arguments
	 * @return an instance of {@link QueryMatches} containing the results of
	 *         interpretation
	 * @throws TMQLRuntimeException
	 *             thrown in interpretation failed. Reason could be for example
	 *             an invalid interpretation of the where-expression.
	 */
	private QueryMatches interpreteWhereClause(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		if (containsExpressionsType(WhereClause.class)) {
			/*
			 * get where-clause
			 */
			IExpressionInterpreter<WhereClause> whereClause = getInterpretersFilteredByEypressionType(runtime, WhereClause.class).get(0);

			/*
			 * call where-clause
			 */
			return whereClause.interpret(runtime, context, optionalArguments);
		}
		return QueryMatches.emptyMatches();
	}

}
