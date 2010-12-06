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
package de.topicmapslab.tmql4j.flwr.components.interpreter;

import java.util.List;
import java.util.Map;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.flwr.grammar.productions.ReturnClause;
import de.topicmapslab.tmql4j.path.grammar.productions.Content;
import de.topicmapslab.tmql4j.path.grammar.productions.PathExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.PostfixedExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.QueryExpression;

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
public class ReturnClauseInterpreter extends ExpressionInterpreterImpl<ReturnClause> {

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
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * get content-interpreter
		 */
		IExpressionInterpreter<?> interpreter = getInterpreters(runtime).get(0);

		QueryMatches results;
		/*
		 * check if context is given by upper FLWR-expression ( where-clause )
		 */
		if (context.getContextBindings() != null) {
			results = new QueryMatches(runtime);
			int index = 0;
			/*
			 * iterate over all tuples
			 */
			for (Map<String, Object> tuple : context.getContextBindings()) {
				Context newContext = new Context(context);
				/*
				 * get value of content
				 */
				String variable = QueryMatches.getNonScopedVariable();
				if (!interpreter.getVariables().isEmpty()) {
					variable = interpreter.getVariables().get(0);
				}
				Object match = tuple.get(variable);

				newContext.setContextBindings(null);
				newContext.setCurrentTuple(tuple);
				newContext.setCurrentNode(match);
				newContext.setCurrentIndex(index++);
				/*
				 * call sub-expression
				 */
				QueryMatches matches = interpreter.interpret(runtime, newContext, optionalArguments);
				results.add(matches);
			}
		}
		/*
		 * no context given by FLWR-expression
		 */
		else {
			/*
			 * call sub-expression
			 */
			results = interpreter.interpret(runtime, context, optionalArguments);
		}
		if (results.isEmpty()) {
			return QueryMatches.emptyMatches();
		}
		return clean(results);
	}

	/**
	 * Method removes all other variables bindings than the non-scoped variable.
	 * 
	 * @param matches
	 *            the matches to clean
	 * @return the cleaned query matches
	 * @throws TMQLRuntimeException
	 */
	private QueryMatches clean(QueryMatches matches) throws TMQLRuntimeException {
		List<Content> expressionFilteredByType = getExpression().getExpressionFilteredByType(Content.class);
		if (!expressionFilteredByType.isEmpty()) {
			Content content = expressionFilteredByType.get(0);

			if (content.getGrammarType() == Content.TYPE_QUERY_EXPRESSION) {
				QueryExpression qEx = content.getExpressionFilteredByType(QueryExpression.class).get(0);
				PathExpression pEx = qEx.getExpressionFilteredByType(PathExpression.class).get(0);
				if (pEx.getGrammarType() == PathExpression.TYPE_POSTFIXED_EXPRESSION) {
					PostfixedExpression pfEx = pEx.getExpressionFilteredByType(PostfixedExpression.class).get(0);
					if (pfEx.getGrammarType() == PostfixedExpression.TYPE_SIMPLE_CONTENT) {
						return matches.extractAndRenameBindingsForVariable(QueryMatches.getNonScopedVariable());
					}
				}
			}
		}
		return matches;
	}

}
