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
package de.topicmapslab.tmql4j.draft2011.path.components.interpreter;

import java.math.BigInteger;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Least;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Most;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Some;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.Content;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.ExistsClause;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.ExistsQuantifiers;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;

/**
 * 
 * Special interpreter class to interpret exists-clauses.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * exists-clause ::= exists-quantifier binding-set satisfies boolean-expression
 * </p>
 * <p>
 * exists-quantifier ::= some | at least integer | at most integer
 * </p>
 * <p>
 * exists-clause ::= exists content ==> some $_ in content satisfies not null
 * </p>
 * </code> </p>
 * 
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ExistsClauseInterpreter extends QuantifiedExpression<ExistsClause> {

	/**
	 * language-specific token identifies the quantifier of exists-clause
	 */
	private final Class<? extends IToken> quantifier;
	/**
	 * the numerical restriction
	 */
	private BigInteger amount;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public ExistsClauseInterpreter(ExistsClause ex) {
		super(ex);
		quantifier = ((ExistsClause) ex).getQuantifier();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		switch (getGrammarTypeOfExpression()) {
		/*
		 * is exists-clause ::= exists-quantifier binding-set satisfies
		 * boolean-expression
		 */
		case 0: {
			amount = (BigInteger) getInterpretersFilteredByEypressionType(runtime, ExistsQuantifiers.class).get(0).interpret(runtime, context, optionalArguments);
			return super.interpret(runtime, context, optionalArguments);
		}
			/*
			 * is exists-clause ::= exists content
			 */
		case 1:
			/*
			 * is exists-clause ::= content
			 */
		case 2: {
			return interpretContent(runtime, context, optionalArguments);
		}
		}
		return QueryMatches.emptyMatches();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected synchronized boolean doSatisfy(ITMQLRuntime runtime, IContext context, IResultSet<?> results) {
		/*
		 * keyword is SOME or LEAST
		 */
		if (quantifier.equals(Least.class) || quantifier.equals(Some.class)) {
			/*
			 * number of content is greater or equal as necessary
			 */
			return results.size() >= amount.longValue();
		}
		/*
		 * keyword is MOST
		 */
		else if (quantifier.equals(Most.class)) {
			/*
			 * content is smaller or equal as necessary
			 */
			return results.size() <= amount.longValue();
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean doSatisfy(QueryMatches context, QueryMatches results) {
		/*
		 * keyword is SOME or LEAST
		 */
		if (quantifier.equals(Least.class) || quantifier.equals(Some.class)) {
			/*
			 * number of content is greater or equal as necessary
			 */
			return results.size() >= amount.longValue();
		}
		/*
		 * keyword is MOST
		 */
		else if (quantifier.equals(Most.class)) {
			/*
			 * content is smaller or equal as necessary
			 */
			return results.size() <= amount.longValue();
		}
		return false;
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
	 *            the current querying context
	 * @param optionalArguments
	 *            optional arguments
	 * @return the query matches
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private QueryMatches interpretContent(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {

		/*
		 * Call subexpression
		 */
		IExpressionInterpreter<Content> ex = getInterpretersFilteredByEypressionType(runtime, Content.class).get(0);
		return ex.interpret(runtime, context, optionalArguments);
	}
}
