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

import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.path.grammar.lexical.Unique;
import de.topicmapslab.tmql4j.path.grammar.productions.FromClause;
import de.topicmapslab.tmql4j.path.grammar.productions.LimitClause;
import de.topicmapslab.tmql4j.path.grammar.productions.OffsetClause;
import de.topicmapslab.tmql4j.path.grammar.productions.OrderByClause;
import de.topicmapslab.tmql4j.path.grammar.productions.SelectClause;
import de.topicmapslab.tmql4j.path.grammar.productions.SelectExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.WhereClause;

/**
 * 
 * Special interpreter class to interpret select-expressions.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * select-expression	::=		 SELECT    < value-expression > [ FROM   value-expression ] [ WHERE   boolean-expression ] [  ORDER BY  < value-expression > ] [  UNIQUE  ] [  OFFSET  value-expression ][  LIMIT  value-expression ]
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SelectExpressionInterpreter extends
		ExpressionInterpreterImpl<SelectExpression> {

	/**
	 * the Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public SelectExpressionInterpreter(SelectExpression ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		logger.info("Start");

		runtime.getRuntimeContext().push();

		/*
		 * extract context defined by from-clause if exists
		 */
		QueryMatches context = interpretFromClause(runtime);

		/*
		 * get limit value of limit-clause if exists
		 */
		long limit = interpretLimitClause(runtime);

		/*
		 * get offset value of offset-clause if exists
		 */
		long offset = interpretOffsetClause(runtime);

		QueryMatches matches = new QueryMatches(runtime);
		boolean containsWhereClause = containsExpressionsType(WhereClause.class);
		/*
		 * interpret where-clause if exists
		 */
		if (containsWhereClause) {
			matches = interpretWhereClause(runtime, context);
		}

		/*
		 * interpret order-by-clause if exists
		 */
		if (!matches.isEmpty() && containsExpressionsType(OrderByClause.class)) {
			matches = interpreteOrderByClause(runtime, matches);
		}
		/*
		 * interpret select-clause if exists
		 */
		matches = interpretSelectClause(runtime, matches);
		/*
		 * clean results by from-clause if exists and not already cleaned by
		 * where-clause
		 */
		if (!containsWhereClause && context != null) {
			/*
			 * create new temporary sequence to store cleared matches
			 */
			ITupleSequence<Object> context_ = context
					.getPossibleValuesForVariable();
			QueryMatches cleaned = new QueryMatches(runtime);
			/*
			 * iterate over all tuples
			 */
			for (Map<String, Object> tuple : matches) {
				boolean add = true;
				for (Object o : tuple.values()) {
					if (o instanceof Collection) {
						Collection<?> col = (Collection<?>) o;
						col.retainAll(context_);
						add = !col.isEmpty();
					} else {
						add = context_.contains(o);
					}
					if (!add) {
						break;
					}
				}
				/*
				 * check if value is contained in the context
				 */
				if (add) {
					cleaned.add(tuple);
				}
			}
			matches = cleaned;
		}

		/*
		 * unify if necessary
		 */
		if (ParserUtils.containsTokens(getTmqlTokens(), Unique.class)
				|| runtime.getProperties().newSequence().isUniqueSet()) {
			matches = matches.unify();
		}

		/*
		 * reduce query-matches to selection window
		 */
		matches = interpretSelectionWindow(matches, limit, offset);

		runtime.getRuntimeContext().peek()
				.setValue(VariableNames.QUERYMATCHES, matches);
	}

	/**
	 * Internal method to interpret the contained form-clause if exists. The
	 * from-clause define the context of variable bindings during the
	 * interpretation of the where-clause. Variables can only bind to values
	 * specified by the from-clause. If the from-clause is missing, the current
	 * topic map will use as context.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @return a query match containing the results of the from-clause
	 *         interpretation or <code>null</code> if from-clause is missing.
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretFromClause(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * check if from-clause exists
		 */
		if (containsExpressionsType(FromClause.class)) {
			return extractArguments(runtime, FromClause.class, 0);
		}
		return null;
	}

	/**
	 * Internal method to interpret the contained limit-clause if exists. The
	 * limit-clause define the number of maximum results contained in the
	 * overall results of this select-expression.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @return the limit defined by the limit-clause or -1 if limit-clause is
	 *         missing.
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private long interpretLimitClause(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		long limit = -1;
		/*
		 * check if limit-clause is contained
		 */
		if (containsExpressionsType(LimitClause.class)) {
			/*
			 * call limit-clause
			 */
			QueryMatches context = extractArguments(runtime, LimitClause.class,
					0);
			limit = ((BigInteger) context.get(0).get(VariableNames.LIMIT))
					.longValue();
		}
		return limit;
	}

	/**
	 * Internal method to interpret the contained offset-clause if exists. The
	 * offset-clause define the first index of the selection window which will
	 * be used to extract the value from the overall results of this
	 * select-expression.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @return the offset defined by the offset-clause or 0 if offset-clause is
	 *         missing.
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private long interpretOffsetClause(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		long offset = 0;
		/*
		 * check if offset-clause is contained
		 */
		if (containsExpressionsType(OffsetClause.class)) {
			/*
			 * call offset-clause
			 */
			QueryMatches context = extractArguments(runtime,
					OffsetClause.class, 0);
			offset = ((BigInteger) context.get(0).get(VariableNames.OFFSET))
					.longValue();
		}
		return offset;
	}

	/**
	 * Internal method to interpret the internal where-clause if exists. The
	 * where-clause define the condition which shall be satisfied by the tuple
	 * of variable bindings. The where-clause bind variables to defined values
	 * which satisfies the contained expression, only these variables can be
	 * used in the select-clause.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param context
	 *            the context defined by the from-clause
	 * @return the results of the where-clause
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretWhereClause(TMQLRuntime runtime,
			QueryMatches context) throws TMQLRuntimeException {

		/*
		 * check if where-clause is contained
		 */
		if (containsExpressionsType(WhereClause.class)) {
			/*
			 * call where-clause
			 */
			QueryMatches matches = extractArguments(runtime, WhereClause.class,
					0);

			/*
			 * check if context is given by from-clause
			 */
			if (context == null) {
				return matches;
			}
			/*
			 * context defined by from-clause
			 */
			else {
				/*
				 * create new temporary sequence to store cleared matches
				 */
				ITupleSequence<Object> context_ = context
						.getPossibleValuesForVariable();
				QueryMatches cleaned = new QueryMatches(runtime);
				/*
				 * iterate over all tuples
				 */
				for (Map<String, Object> tuple : matches) {
					/*
					 * check if value is contained in the context
					 */
					if (context_.containsAll(tuple.values())) {
						cleaned.add(tuple);
					}
				}
				return cleaned;
			}
		}
		return null;
	}

	/**
	 * Internal method to interpret the order-by-clause if it exists. The
	 * order-by clause orders the variable bindings using short
	 * value-expressions as context of ordering.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param tuples
	 *            the tuple to order
	 * @return the order tuple sequence
	 * @throws TMQLRuntimeException
	 *             thrown if ordering fails
	 */
	private QueryMatches interpreteOrderByClause(TMQLRuntime runtime,
			QueryMatches tuples) throws TMQLRuntimeException {
		/*
		 * check if order-by clause exists and any variable is contained
		 */
		if (containsExpressionsType(OrderByClause.class) && !tuples.isEmpty()) {
			/*
			 * set binding to set on top of the stack, which has to order
			 */
			runtime.getRuntimeContext().push()
					.setValue(VariableNames.ORDER, tuples);

			/*
			 * call order by clause
			 */
			QueryMatches ordered = extractArguments(runtime,
					OrderByClause.class, 0);

			/*
			 * remove top variable layer
			 */
			runtime.getRuntimeContext().pop();
			return ordered;
		}
		return tuples;
	}

	/**
	 * Internal method to interpret the internal select-clause if exists. The
	 * select-clause is the only non-optional expression of the
	 * select-expression and define the tuple-sequence of returned values.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param matches
	 *            the satisfying variable bindings of the where-clause
	 * @return the over all result of the select-clause
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretSelectClause(TMQLRuntime runtime,
			QueryMatches matches) throws TMQLRuntimeException {

		/*
		 * only one select clause has to be contained
		 */
		if (!containsExpressionsType(SelectClause.class)) {
			throw new TMQLRuntimeException(
					"Invalid structure. not select clause.");
		}

		/*
		 * extract the select clause
		 */
		IExpressionInterpreter<SelectClause> selectClause = getInterpretersFilteredByEypressionType(
				runtime, SelectClause.class).get(0);

		runtime.getRuntimeContext().peek()
				.remove(VariableNames.ITERATED_BINDINGS);

		/*
		 * push to the top of the stack
		 */
		runtime.getRuntimeContext().push();
		if (!matches.isEmpty()) {
			runtime.getRuntimeContext().peek()
					.setValue(VariableNames.ITERATED_BINDINGS, matches);
		}

		/*
		 * call sub expression
		 */
		selectClause.interpret(runtime);

		/*
		 * pop from top of the stack
		 */
		Object obj = runtime.getRuntimeContext().pop()
				.getValue(VariableNames.QUERYMATCHES);
		/*
		 * check result type
		 */
		if (!(obj instanceof QueryMatches)) {
			throw new TMQLRuntimeException(
					"Invalid interpretation of expression select-clause. Has to return an instance of Query<?>.");
		}

		return (QueryMatches) obj;
	}

	/**
	 * Internal method to extract the selection window from the over all results
	 * defined by the limit- and offset-clause.
	 * 
	 * @param matches
	 *            the over all results of the select-clause
	 * @param limit
	 *            the limit value defined by limit-clause
	 * @param offset
	 *            the offset value defined by offset-clause
	 * @return the new matches containing only the tuples of the selection
	 *         window
	 * @throws TMQLRuntimeException
	 *             thrown if indexes are invalid
	 */
	private QueryMatches interpretSelectionWindow(QueryMatches matches,
			long limit, long offset) throws TMQLRuntimeException {
		if (limit == -1) {
			if (offset == -1) {
				return matches;
			}else{
				limit = matches.size();
			}
		}
		if ( offset == -1 ){
			offset = 0;
		}
		/*
		 * redirect to QueryMatches
		 */
		return matches.select(offset, offset + limit);
	}

}
