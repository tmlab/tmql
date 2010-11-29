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
import java.util.Map;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.results.IResultSet;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.path.grammar.lexical.Least;
import de.topicmapslab.tmql4j.path.grammar.lexical.Most;
import de.topicmapslab.tmql4j.path.grammar.lexical.Some;
import de.topicmapslab.tmql4j.path.grammar.productions.Content;
import de.topicmapslab.tmql4j.path.grammar.productions.ExistsClause;
import de.topicmapslab.tmql4j.path.grammar.productions.ExistsQuantifiers;

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
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		switch (getGrammarTypeOfExpression()) {
		/*
		 * is exists-clause ::= exists-quantifier binding-set satisfies
		 * boolean-expression
		 */
		case 0: {
			amount = extractArguments(runtime, ExistsQuantifiers.class, 0,
					VariableNames.QUANTIFIES);
			super.interpret(runtime);
		}
			break;
		/*
		 * is exists-clause ::= exists content
		 */
		case 1:
			/*
			 * is exists-clause ::= content
			 */
		case 2: {
			interpretContent(runtime);
		}
			;
			break;
		default:
			throw new TMQLRuntimeException("Unexpected state!");
		}
		;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected synchronized boolean doSatisfy(TMQLRuntime runtime, Map<String, Object> tuple, IResultSet<?> results) {
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
	 * @throws TMQLRuntimeException
	 *             thrown if interpretation fails
	 */
	private void interpretContent(TMQLRuntime runtime)
			throws TMQLRuntimeException {
		/*
		 * Push to stack
		 */
		runtime.getRuntimeContext().push();

		/*
		 * Call subexpression
		 */
		IExpressionInterpreter<Content> ex = getInterpretersFilteredByEypressionType(
				runtime, Content.class).get(0);
		ex.interpret(runtime);

		/*
		 * pop from stack
		 */
		IVariableSet set = runtime.getRuntimeContext().pop();
		if (set.contains(VariableNames.QUERYMATCHES)) {
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.QUERYMATCHES,
					set.getValue(VariableNames.QUERYMATCHES));
		} else {
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.QUERYMATCHES, new QueryMatches(runtime));
		}

		/*
		 * shift negative matches
		 */
		if (set.contains(VariableNames.NEGATIVE_MATCHES)) {
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.NEGATIVE_MATCHES,
					set.getValue(VariableNames.NEGATIVE_MATCHES));
		}
	}
}
