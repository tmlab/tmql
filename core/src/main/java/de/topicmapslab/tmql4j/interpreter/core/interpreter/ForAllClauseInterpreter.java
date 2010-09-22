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
package de.topicmapslab.tmql4j.interpreter.core.interpreter;

import java.util.Map;
import java.util.Map.Entry;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.QuantifiedExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.BindingSet;
import de.topicmapslab.tmql4j.parser.core.expressions.ForAllClause;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;

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
	@Override
	protected synchronized boolean doSatisfy(TMQLRuntime runtime,
			Map<String, Object> tuple, IResultSet<?> results) {
		try {
			/*
			 * create new variable layer with current tuple
			 */
			runtime.getRuntimeContext().push();
			for (Entry<String, Object> t : tuple.entrySet()) {
				runtime.getRuntimeContext().peek().setValue(
						t.getKey(), t.getValue());
			}
			/*
			 * call iteration bindings to extract all possible values
			 */
			QueryMatches context = extractArguments(runtime, BindingSet.class,
					0);
			runtime.getRuntimeContext().pop();
			/*
			 * check if count of possible values are equals than the count of
			 * satisfying values
			 */
			return results.size() == context.size() && !context.isEmpty();
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
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * redirect to parent method
		 */
		super.interpret(runtime);
	}

}
