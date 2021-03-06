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

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.BindingSet;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.ForAllClause;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * 
 * Special interpreter class to interpret for-all-clauses.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * forall-clause ::= every binding-set satisfies boolean-expression
 * </p>
 * <p>
 * forall-clause ::= not some binding-set satisfies not ( boolean-expression )
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ForAllClauseInterpreter extends QuantifiedExpression<ForAllClause> {
	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public ForAllClauseInterpreter(ForAllClause ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean doSatisfy(ITMQLRuntime runtime, IContext context, IResultSet<?> results) {
		try {
			/*
			 * call iteration bindings to extract all possible values
			 */
			QueryMatches matches = extractArguments(runtime, BindingSet.class, 0, context);
			/*
			 * check if count of possible values are equals than the count of
			 * satisfying values
			 */
			return results.size() == matches.size() && !matches.isEmpty();
		} catch (TMQLRuntimeException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean doSatisfy(QueryMatches context, QueryMatches results) {
		/*
		 * check if count of possible values are equals than the count of
		 * satisfying values
		 */
		return context.size() == results.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		return super.interpret(runtime, context, optionalArguments);
	}

}
