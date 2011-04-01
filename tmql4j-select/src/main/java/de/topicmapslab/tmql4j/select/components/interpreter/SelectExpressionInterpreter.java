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
package de.topicmapslab.tmql4j.select.components.interpreter;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.path.grammar.lexical.Unique;
import de.topicmapslab.tmql4j.path.grammar.productions.OrderByClause;
import de.topicmapslab.tmql4j.select.grammar.productions.FromClause;
import de.topicmapslab.tmql4j.select.grammar.productions.GroupByClause;
import de.topicmapslab.tmql4j.select.grammar.productions.LimitClause;
import de.topicmapslab.tmql4j.select.grammar.productions.OffsetClause;
import de.topicmapslab.tmql4j.select.grammar.productions.SelectClause;
import de.topicmapslab.tmql4j.select.grammar.productions.SelectExpression;
import de.topicmapslab.tmql4j.select.grammar.productions.WhereClause;
import de.topicmapslab.tmql4j.util.HashUtil;

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
public class SelectExpressionInterpreter extends ExpressionInterpreterImpl<SelectExpression> {

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
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		Context newContext = new Context(context);
		QueryMatches fromResults = null;
		/*
		 * check if from-clause exists
		 */
		if (containsExpressionsType(FromClause.class)) {
			fromResults = interpretFromClause(runtime, newContext, optionalArguments);
			if (fromResults.isEmpty()) {
				logger.warn("Interpretation of from clause return no results!");
				return QueryMatches.emptyMatches();
			}
		}

		/*
		 * get limit value of limit-clause if exists
		 */
		long limit = -1;
		if (containsExpressionsType(LimitClause.class)) {
			limit = interpretLimitClause(runtime, newContext, optionalArguments);
		}

		/*
		 * get offset value of offset-clause if exists
		 */
		long offset = 0;
		if (containsExpressionsType(OffsetClause.class)) {
			offset = interpretOffsetClause(runtime, newContext, optionalArguments);
		}
		/*
		 * interpret where-clause if exists
		 */
		if (containsExpressionsType(WhereClause.class)) {
			newContext.setContextBindings(getBindingsContext(runtime, fromResults));
			fromResults = null;
			QueryMatches matches = interpretWhereClause(runtime, newContext, optionalArguments);
			if (matches.isEmpty()) {
				logger.warn("Interpretation of where clause return no results!");
				return QueryMatches.emptyMatches();
			}
			newContext.setContextBindings(matches);
		}

		/*
		 * interpret order-by-clause if exists
		 */
		if (containsExpressionsType(OrderByClause.class)) {
			QueryMatches matches = interpreteOrderByClause(runtime, newContext, optionalArguments);
			if (matches.isEmpty()) {
				logger.warn("Interpretation of order-by clause return no results!");
				return QueryMatches.emptyMatches();
			}
			newContext.setContextBindings(matches);
		}
		/*
		 * interpret select-clause if exists
		 */
		QueryMatches matches = interpretSelectClause(runtime, newContext, optionalArguments);
		/*
		 * remove non valid content defined by from-clause
		 */
		if (fromResults != null) {
			/*
			 * create new temporary sequence to store cleared matches
			 */
			List<Object> context_ = fromResults.getPossibleValuesForVariable();
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
		 * interpret group-by if exists
		 */
		if (containsExpressionsType(GroupByClause.class)) {
			newContext.setContextBindings(matches);
			matches = getInterpretersFilteredByEypressionType(runtime, GroupByClause.class).get(0).interpret(runtime, newContext, optionalArguments);
		}

		/*
		 * unify if necessary
		 */
		if (ParserUtils.containsTokens(getTmqlTokens(), Unique.class)) {
			matches = matches.unify();
		}

		/*
		 * reduce query-matches to selection window
		 */
		return interpretSelectionWindow(matches, limit, offset);
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
	 * @param context
	 *            the current querying context
	 * @param optionalArguments
	 *            optional arguments
	 * @return a query match containing the results of the from-clause
	 *         interpretation or <code>null</code> if from-clause is missing.
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretFromClause(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		return extractArguments(runtime, FromClause.class, 0, context, optionalArguments);
	}

	/**
	 * Internal method to interpret the contained limit-clause if exists. The
	 * limit-clause define the number of maximum results contained in the
	 * overall results of this select-expression.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param context
	 *            the current querying context
	 * @param optionalArguments
	 *            optional arguments
	 * @return the limit defined by the limit-clause or -1 if limit-clause is
	 *         missing.
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private long interpretLimitClause(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		BigInteger limit = getInterpretersFilteredByEypressionType(runtime, LimitClause.class).get(0).interpret(runtime, context, optionalArguments);
		return limit.longValue();
	}

	/**
	 * Internal method to interpret the contained offset-clause if exists. The
	 * offset-clause define the first index of the selection window which will
	 * be used to extract the value from the overall results of this
	 * select-expression.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param context
	 *            the current querying context
	 * @param optionalArguments
	 *            optional arguments
	 * @return the offset defined by the offset-clause or 0 if offset-clause is
	 *         missing.
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private long interpretOffsetClause(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		BigInteger limit = getInterpretersFilteredByEypressionType(runtime, OffsetClause.class).get(0).interpret(runtime, context, optionalArguments);
		return limit.longValue();
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
	 *            the current querying context
	 * @param optionalArguments
	 *            optional arguments
	 * @return the results of the where-clause
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretWhereClause(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * call where-clause
		 */
		return extractArguments(runtime, WhereClause.class, 0, context, optionalArguments);
		//
		// /*
		// * check if context is given by from-clause
		// */
		// if (context == null) {
		// return matches;
		// }
		// /*
		// * context defined by from-clause
		// */
		// else {
		// /*
		// * create new temporary sequence to store cleared matches
		// */
		// ITupleSequence<Object> context_ = context
		// .getPossibleValuesForVariable();
		// QueryMatches cleaned = new QueryMatches(runtime);
		// /*
		// * iterate over all tuples
		// */
		// for (Map<String, Object> tuple : matches) {
		// /*
		// * check if value is contained in the context
		// */
		// if (context_.containsAll(tuple.values())) {
		// cleaned.add(tuple);
		// }
		// }
		// return cleaned;
		// }
	}

	/**
	 * Internal method to interpret the order-by-clause if it exists. The
	 * order-by clause orders the variable bindings using short
	 * value-expressions as context of ordering.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param context
	 *            the current querying context
	 * @param optionalArguments
	 *            optional arguments
	 * @return the order tuple sequence
	 * @throws TMQLRuntimeException
	 *             thrown if ordering fails
	 */
	private QueryMatches interpreteOrderByClause(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * call order by clause
		 */
		return extractArguments(runtime, OrderByClause.class, 0, context, optionalArguments);
	}

	/**
	 * Internal method to interpret the internal select-clause if exists. The
	 * select-clause is the only non-optional expression of the
	 * select-expression and define the tuple-sequence of returned values.
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param context
	 *            the current querying context
	 * @param optionalArguments
	 *            optional arguments
	 * @return the over all result of the select-clause
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretSelectClause(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * only one select clause has to be contained
		 */
		if (!containsExpressionsType(SelectClause.class)) {
			throw new TMQLRuntimeException("Invalid structure. not select clause.");
		}
		/*
		 * extract the select clause
		 */
		IExpressionInterpreter<SelectClause> selectClause = getInterpretersFilteredByEypressionType(runtime, SelectClause.class).get(0);
		/*
		 * call sub expression
		 */
		return selectClause.interpret(runtime, context, optionalArguments);
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
	private QueryMatches interpretSelectionWindow(QueryMatches matches, long limit, long offset) throws TMQLRuntimeException {
		if (limit == -1) {
			if (offset == -1) {
				return matches;
			} else {
				limit = matches.size();
			}
		}
		if (offset == -1) {
			offset = 0;
		}
		/*
		 * redirect to QueryMatches
		 */
		return matches.select(offset, offset + limit);
	}

	/**
	 * Utility method to create a binding context for where-clause execution
	 * 
	 * @param runtime
	 *            the runtime
	 * @param fromBindings
	 *            the from bindings
	 * @return the generated context
	 */
	private QueryMatches getBindingsContext(ITMQLRuntime runtime, QueryMatches fromBindings) {
		if (fromBindings == null) {
			return null;
		}

		QueryMatches context = new QueryMatches(runtime);
		/*
		 * iterate over all possible values
		 */
		for (Object value : fromBindings.getPossibleValuesForVariable()) {
			/*
			 * iterate over all keys
			 */
			Map<String, Object> tuple = HashUtil.getHashMap();
			for (String key : getExpression().getExpressionFilteredByType(WhereClause.class).get(0).getVariables()) {
				tuple.put(key, value);
			}
			context.add(tuple);
		}
		return context;
	}

}
