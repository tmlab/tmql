/**
 * TMQL4J - Javabased TMQL Engine
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.extension.tmml.interpreter;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import de.topicmapslab.tmql4j.common.context.TMQLRuntimeProperties;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.extension.tmml.grammar.expressions.InsertClause;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.parser.core.expressions.TMContent;

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
public class InsertClauseInterpreter extends
		ExpressionInterpreterImpl<InsertClause> {

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
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		/*
		 * store old properties of result set and result tuple classes
		 */
		String resultSetClass = runtime.getProperties().getProperty(
				TMQLRuntimeProperties.RESULT_SET_IMPLEMENTATION_CLASS);
		String resultClass = runtime.getProperties().getProperty(
				TMQLRuntimeProperties.RESULT_TUPLE_IMPLEMENTATION_CLASS);

		/*
		 * extract sub-expression of type tm-content
		 */
		List<IExpressionInterpreter<TMContent>> interpreters = getInterpretersFilteredByEypressionType(
				runtime, TMContent.class);
		if (interpreters.size() != 1) {
			throw new TMQLRuntimeException(
					"Invalid structure. Insert-clauses have to contain exactly one tm-content expression.");
		}

		IExpressionInterpreter<TMContent> interpreter = interpreters.get(0);

		Logger.getLogger(getClass().getSimpleName()).info("Start.");

		QueryMatches results = new QueryMatches(runtime);
		/*
		 * check if context is given by insert-expression ( where-clause )
		 */
		if (runtime.getRuntimeContext().peek().contains(
				VariableNames.ITERATED_BINDINGS)) {
			QueryMatches matches = (QueryMatches) runtime.getRuntimeContext()
					.peek().getValue(VariableNames.ITERATED_BINDINGS);

			/*
			 * iterate over all tuples
			 */
			for (Map<String, Object> tuple : matches) {
				String variable = QueryMatches.getNonScopedVariable();
				Object match = tuple.get(variable);

				/*
				 * clear stack
				 */
				runtime.getRuntimeContext().push();
				runtime.getRuntimeContext().peek()
						.remove(
								VariableNames.ITERATED_BINDINGS);
				runtime.getRuntimeContext().peek()
						.setValue(VariableNames.CURRENT_TUPLE,
								match);
				runtime.getRuntimeContext().peek()
						.setValue(
								QueryMatches.getNonScopedVariable(), match);
				/*
				 * push tuple to the top of the stack
				 */
				for (String var : getVariables()) {
					runtime.getRuntimeContext().peek()
							.setValue(var, tuple.get(var));
				}

				/*
				 * call tm-content
				 */
				interpreter.interpret(runtime);

				Object obj = runtime.getRuntimeContext().pop()
						.getValue(VariableNames.QUERYMATCHES);
				if (!(obj instanceof QueryMatches)) {
					throw new TMQLRuntimeException(
							"Invalid interpretation of expression value-expression. Has to return an instance of QueryMatches.");
				}

				results.add(((QueryMatches) obj).getMatches());
			}

		}
		/*
		 * no context given
		 */
		else {

			runtime.getRuntimeContext().push();
			/*
			 * call tm-content
			 */
			interpreter.interpret(runtime);

			Object obj = runtime.getRuntimeContext().pop().getValue(
					VariableNames.QUERYMATCHES);
			if (!(obj instanceof QueryMatches)) {
				throw new TMQLRuntimeException(
						"Invalid interpretation of expression value-expression. Has to return an instance of QueryMatches.");
			}
			results = (QueryMatches) obj;
		}

		runtime.getRuntimeContext().peek().setValue(
				VariableNames.QUERYMATCHES, results);

		Logger.getLogger(getClass().getSimpleName()).info(
				"Finished. Results: " + results);

		/*
		 * switch result-type back to origin
		 */
		runtime.getProperties().setProperty(
				TMQLRuntimeProperties.RESULT_SET_IMPLEMENTATION_CLASS,
				resultSetClass);
		runtime.getProperties().setProperty(
				TMQLRuntimeProperties.RESULT_TUPLE_IMPLEMENTATION_CLASS,
				resultClass);
	}

}
