/**
 * TMQL4J - Javabased TMQL Engine
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.insert.components.interpreter;

import java.util.List;
import java.util.Map;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.insert.grammar.productions.InsertClause;
import de.topicmapslab.tmql4j.insert.grammar.productions.TMContent;
import de.topicmapslab.tmql4j.path.components.processor.results.SimpleResultSet;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * 
 * Special interpreter class to interpret insert-clauses.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * insert-clause ::= INSERT tm-content
 * </code>
 * </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class InsertClauseInterpreter extends ExpressionInterpreterImpl<InsertClause> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public InsertClauseInterpreter(InsertClause ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		/*
		 * extract sub-expression of type TM-content
		 */
		List<IExpressionInterpreter<TMContent>> interpreters = getInterpretersFilteredByEypressionType(runtime, TMContent.class);
		if (interpreters.size() != 1) {
			throw new TMQLRuntimeException("Invalid structure. Insert-clauses have to contain exactly one tm-content expression.");
		}
		IExpressionInterpreter<TMContent> interpreter = interpreters.get(0);
		/*
		 * check if context is given by insert-expression ( where-clause )
		 */
		if (context.getContextBindings() != null) {
			List<Object> results = HashUtil.getList();
			/*
			 * iterate over all tuples
			 */
			for (Map<String, Object> tuple : context.getContextBindings()) {
				String variable = QueryMatches.getNonScopedVariable();
				Object match = tuple.get(variable);
				Context newContext = new Context(context);
				newContext.setContextBindings(null);
				newContext.setCurrentTuple(tuple);
				newContext.setCurrentNode(match);
				/*
				 * call TM-content
				 */
				QueryMatches matches = interpreter.interpret(runtime, newContext, optionalArguments);
				results.add(matches.getMatches());
			}
			context.getTmqlProcessor().getResultProcessor().setResultType(SimpleResultSet.class);
			return QueryMatches.asQueryMatchNS(runtime, results.toArray());
		}
		/*
		 * no context given
		 */
		return interpreter.interpret(runtime, context, optionalArguments);
	}

}
