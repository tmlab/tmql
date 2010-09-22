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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.parser.core.expressions.Content;
import de.topicmapslab.tmql4j.parser.core.expressions.PathExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.PostfixedExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.QueryExpression;
import de.topicmapslab.tmql4j.parser.core.expressions.ReturnClause;

/**
 * 
 * Special interpreter class to interpret return-clauses.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * return-clause ::= RETURN   content;
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ReturnClauseInterpreter extends
		ExpressionInterpreterImpl<ReturnClause> {
	
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
	public ReturnClauseInterpreter(ReturnClause ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		logger.info("Start.");

		/*
		 * get content-interpreter
		 */
		IExpressionInterpreter<Content> ex = getInterpretersFilteredByEypressionType(
				runtime, Content.class).get(0);

		QueryMatches results = new QueryMatches(runtime);
		/*
		 * check if context is given by upper flwr-expression ( where-clause )
		 */
		if (runtime.getRuntimeContext().peek().contains(
				VariableNames.ITERATED_BINDINGS)) {
			QueryMatches matches = (QueryMatches) runtime.getRuntimeContext()
					.peek().getValue(VariableNames.ITERATED_BINDINGS);

			int pos = 0;
			/*
			 * iterate over all tuples
			 */
			for (Map<String, Object> tuple : matches) {

				/*
				 * get value of content
				 */
				String variable = QueryMatches.getNonScopedVariable();
				if (!ex.getVariables().isEmpty()) {
					variable = ex.getVariables().get(0);
				}
				Object match = tuple.get(variable);				

				/*
				 * clean variable binding on top of the stack
				 */
				runtime.getRuntimeContext().push();
				runtime.getRuntimeContext().peek()
						.remove(
								VariableNames.ITERATED_BINDINGS);
				runtime.getRuntimeContext().peek()
						.setValue(VariableNames.CURRENT_TUPLE,
								match);
				runtime.getRuntimeContext().peek()
						.setValue(VariableNames.CURRENT_POISTION,
								new Integer(pos));
				runtime.getRuntimeContext().peek()
						.setValue(
								QueryMatches.getNonScopedVariable(), match);
				/*
				 * push tuple to the top of the stack
				 */
				for (String var : getVariables()) {
					if (!runtime.isSystemVariable(var) && tuple.get(var) != null ) {						
						runtime.getRuntimeContext().peek()
								.setValue(var, tuple.get(var));
					}
				}

				/*
				 * call sub-expression
				 */
				ex.interpret(runtime);

				Object obj = runtime.getRuntimeContext().pop()
						.getValue(VariableNames.QUERYMATCHES);
				if (!(obj instanceof QueryMatches)) {
					throw new TMQLRuntimeException(
							"Invalid interpretation of expression content. Has to return an instance of QueryMatches.");
				}

				results.add(((QueryMatches) obj));
				pos++;
			}			
		}
		/*
		 * no context given by flwr-expresion
		 */
		else {

			/*
			 * call sub-expression
			 */
			runtime.getRuntimeContext().push();
			ex.interpret(runtime);

			/*
			 * redirect results
			 */
			Object obj = runtime.getRuntimeContext().pop().getValue(
					VariableNames.QUERYMATCHES);
			if (!(obj instanceof QueryMatches)) {
				throw new TMQLRuntimeException(
						"Invalid interpretation of expression content. Has to return an instance of QueryMatches.");
			}
			results = (QueryMatches) obj;
		}

		runtime.getRuntimeContext().peek().setValue(
				VariableNames.QUERYMATCHES, clean(results));

		logger.info(
				"Finished. Results: " + clean(results));

	}

	/**
	 * Method removes all other variables bindings than the non-scoped variable.
	 * 
	 * @param matches
	 *            the matches to clean
	 * @return the cleaned query matches
	 * @throws TMQLRuntimeException
	 */
	private QueryMatches clean(QueryMatches matches)
			throws TMQLRuntimeException {
		Content content = getExpression().getExpressionFilteredByType(
				Content.class).get(0);

		if (content.getGrammarType() == Content.TYPE_QUERY_EXPRESSION) {
			QueryExpression qEx = content.getExpressionFilteredByType(
					QueryExpression.class).get(0);
			if (qEx.getGrammarType() == QueryExpression.TYPE_PATH_EXPRESSION) {
				PathExpression pEx = qEx.getExpressionFilteredByType(
						PathExpression.class).get(0);
				if (pEx.getGrammarType() == PathExpression.TYPE_POSTFIXED_EXPRESSION) {
					PostfixedExpression pfEx = pEx.getExpressionFilteredByType(
							PostfixedExpression.class).get(0);
					if (pfEx.getGrammarType() == PostfixedExpression.TYPE_SIMPLE_CONTENT) {
						return matches
								.extractAndRenameBindingsForVariable(QueryMatches
										.getNonScopedVariable());
					}
				}
			}
		}

		return matches;
	}

}
