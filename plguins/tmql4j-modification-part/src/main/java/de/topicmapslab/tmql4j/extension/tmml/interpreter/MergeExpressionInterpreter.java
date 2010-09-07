/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.extension.tmml.interpreter;

import static de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.MergeExpression.TYPE_ALL;
import static de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.MergeExpression.TYPE_VALUEEXPRESSION;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.ModelConstraintException;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.extension.tmml.event.MergeEvent;
import de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.MergeExpression;
import de.topicmapslab.tmql4j.extension.tmml.util.MergeHandler;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.core.base.context.VariableSetImpl;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.model.context.IVariableSet;
import de.topicmapslab.tmql4j.parser.core.expressions.ValueExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.WhereClause;

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
public class MergeExpressionInterpreter extends
		ExpressionInterpreterImpl<MergeExpression> {

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
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * switch operation by grammar type of the production rule
		 */
		switch (getGrammarTypeOfExpression()) {
		/*
		 * merge-expression contains a number of value-expression
		 */
		case TYPE_VALUEEXPRESSION: {
			interpretValueExpression(runtime);
		}
			break;
		/*
		 * merge-expression contains a where-clause and the keyword ALL
		 */
		case TYPE_ALL: {
			interpretAllExpression(runtime);
		}
			break;
		default:
			throw new TMQLRuntimeException("Unknown grammar type.");

		}
	}

	/**
	 * Internal method to handle production rule for a merge-expression
	 * containing the keyword ALL and a where-clause
	 * 
	 * @param runtime
	 *            the current TMQL runtime
	 * @throws TMQLRuntimeException
	 *             thrown in interpretation or the merge operation failed.
	 */
	private void interpretAllExpression(TMQLRuntime runtime)
			throws TMQLRuntimeException {

		/*
		 * interpret the where-clause
		 */
		QueryMatches matches = interpreteWhereClause(runtime).unify();

		/*
		 * cache to store merged topics
		 */
		Set<Topic> cache = HashUtil.getHashSet();

		/*
		 * iterate over all tuples
		 */
		QueryMatches results = new QueryMatches(runtime);
		/*
		 * store topics to merge
		 */
		Set<Topic> topics = HashUtil.getHashSet();
		for (Map<String, Object> tuple : matches) {
			/*
			 * iterate over all entries
			 */
			for (Entry<String, Object> entry : tuple.entrySet()) {
				/*
				 * check if variable is not a system variable and variable is
				 * bind to a topic
				 */
				if (!runtime.isSystemVariable(entry.getKey())
						&& entry.getValue() instanceof Topic) {
					topics.add((Topic) entry.getValue());
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
			/*
			 * fire event
			 */
			if (amount > 0) {
				runtime.getEventManager().event(
						new MergeEvent(topic, topics, this));
			}
			/*
			 * store number of merges
			 */
			Map<String, Object> resultTuple = HashUtil.getHashMap();
			resultTuple.put("$0", amount);
			results.add(resultTuple);
		} catch (ModelConstraintException e) {
			throw new TMQLRuntimeException(e);
		}

		/*
		 * set results to stack
		 */
		runtime.getRuntimeContext().peek()
				.setValue(VariableNames.QUERYMATCHES, results);
	}

	/**
	 * Internal method to handle production rule for a merge-expression
	 * containing value-expressions and a where-clause
	 * 
	 * @param runtime
	 *            the current TMQL runtime
	 * @throws TMQLRuntimeException
	 *             thrown in interpretation or the merge operation failed.
	 */
	private void interpretValueExpression(TMQLRuntime runtime)
			throws TMQLRuntimeException {

		QueryMatches results = new QueryMatches(runtime);

		/*
		 * interpret the where-clause
		 */
		QueryMatches iteration = interpreteWhereClause(runtime).unify();

		Collection<Construct> alreadyMerged = HashUtil.getHashSet();

		/*
		 * no binding defined
		 */
		if (iteration.isEmpty()) {
			long count = merge(runtime,
					extractArguments(runtime, ValueExpression.class),
					alreadyMerged);
			if (count > 0) {
				Map<String, Object> t = HashUtil.getHashMap();
				t.put(QueryMatches.getNonScopedVariable(), count);
				results.add(t);
			}
		}
		/*
		 * binding set
		 */
		else {
			for (Map<String, Object> tuple : iteration) {
				IVariableSet set = new VariableSetImpl();
				for (Entry<String, Object> entry : tuple.entrySet()) {
					set.setValue(entry.getKey(), entry.getValue());
				}

				runtime.getRuntimeContext().push(set);
				long count = merge(runtime,
						extractArguments(runtime, ValueExpression.class),
						alreadyMerged);
				if (count > 0) {
					Map<String, Object> t = HashUtil.getHashMap();
					t.put(QueryMatches.getNonScopedVariable(), count);
					results.add(t);
				}
				runtime.getRuntimeContext().pop();
			}
		}

		/*
		 * set results to stack
		 */
		runtime.getRuntimeContext().peek()
				.setValue(VariableNames.QUERYMATCHES, results);
	}

	private long merge(TMQLRuntime runtime, QueryMatches[] arguments,
			Collection<Construct> alreadyMerged) throws TMQLRuntimeException {
		Set<Construct> candidates = HashUtil.getHashSet();
		for (QueryMatches match : arguments) {
			for (Object obj : match.getPossibleValuesForVariable()) {
				if (obj instanceof Topic || obj instanceof Association) {
					candidates.add((Construct) obj);
				} else {
					throw new TMQLRuntimeException(
							"Unsupported merge content '"
									+ (obj == null ? "null" : obj.getClass()
											.getSimpleName()) + "'");
				}
			}
		}

		long count = MergeHandler.doMerge(candidates, alreadyMerged);
		/*
		 * fire event
		 */
		if (count > 0) {
			runtime.getEventManager().event(
					new MergeEvent(candidates.iterator().next(), candidates,
							this));
		}
		return count;
	}

	/**
	 * Internal method to interpret contained where-clause of the
	 * merge-expression.
	 * 
	 * @param runtime
	 *            the current TMQL runtime
	 * @return an instance of {@link QueryMatches} containing the results of
	 *         interpretation
	 * @throws TMQLRuntimeException
	 *             thrown in interpretation failed. Reason could be for example
	 *             an invalid interpretation of the where-expression.
	 */
	private QueryMatches interpreteWhereClause(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		if (containsExpressionsType(WhereClause.class)) {
			/*
			 * get where-clause
			 */
			IExpressionInterpreter<WhereClause> whereClause = getInterpretersFilteredByEypressionType(
					runtime, WhereClause.class).get(0);

			/*
			 * push on top of the stack
			 */
			runtime.getRuntimeContext().push();

			/*
			 * call where-clause
			 */
			whereClause.interpret(runtime);

			/*
			 * pop from stack
			 */
			Object obj = runtime.getRuntimeContext().peek()
					.getValue(VariableNames.QUERYMATCHES);

			/*
			 * check result type, has to be an instance of ITupleSequence<?>
			 */
			if (!(obj instanceof QueryMatches)) {
				throw new TMQLRuntimeException(
						"Invalid interpretation of expression where-clause. Has to return an instance of QueryMatches.");
			}
			return (QueryMatches) obj;
		}
		return new QueryMatches(runtime);
	}

}
