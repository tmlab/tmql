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

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.util.HashUtil;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.productions.NonInterpretedContent;
import de.topicmapslab.tmql4j.path.grammar.productions.TMContent;

/**
 * 
 * Special interpreter class to interpret tm-content.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * tm-content ::= ctm-fragment
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TMContentInterpreter extends ExpressionInterpreterImpl<TMContent> {

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
	public TMContentInterpreter(TMContent ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		StringBuilder builder = new StringBuilder();

		/*
		 * iterate over all internal interpreter instances
		 */
		for (IExpressionInterpreter<?> interpreter : getInterpreters(runtime)) {

			/*
			 * create new variable layer
			 */
			runtime.getRuntimeContext().push();
			/*
			 * call sub-expression
			 */
			interpreter.interpret(runtime);

			/*
			 * check and extract results of interpretation
			 */
			IVariableSet set = runtime.getRuntimeContext().pop();
			if (!set.contains(VariableNames.QUERYMATCHES)) {
				throw new TMQLRuntimeException(
						"Missing interpretation result of production '"
								+ interpreter.getExpression().getClass()
										.getSimpleName() + "'.");
			}
			QueryMatches matches = (QueryMatches) set
					.getValue(VariableNames.QUERYMATCHES);
			/*
			 * result is simple text content and has to be added unmodified
			 */
			if (interpreter.getExpression() instanceof NonInterpretedContent) {
				String content = matches.get(0).get(
						QueryMatches.getNonScopedVariable()).toString();
				builder.append(content);
			}
			/*
			 * results are not empty and has to be converted to CTM
			 */
			else if (!matches.isEmpty()) {
				Collection<?> values = null;
				/*
				 * values are bound to '$$$$' because of embed path-expression
				 */
				if (matches.getOrderedKeys().contains(
						QueryMatches.getNonScopedVariable())) {
					values = matches.getPossibleValuesForVariable();
				}
				/*
				 * values are bound to '$0' because of embed flwr- or
				 * select-expression
				 */
				else if (matches.getOrderedKeys().contains("$0")) {
					values = matches.getPossibleValuesForVariable("$0");
				}
				/*
				 * values are bound to unknown variable
				 */
				else {
					throw new TMQLRuntimeException(
							"Variable binding is missing. Expects '$0' or '$$$$' but found '"
									+ matches.getOrderedKeys() + "'");
				}
				/*
				 * results as CTM
				 */
				builder.append(CTMConverter.toCTMString(values, runtime));
			}
			/*
			 * logging information that matches are empty and will be ignored
			 */
			else {
				logger
						.info("Interpretation result is empty and will be ignored.");
			}
		}

		/*
		 * store results as query-match at index zero
		 */
		QueryMatches results = new QueryMatches(runtime);
		Map<String, Object> tuple = HashUtil.getHashMap();
		tuple.put("$0", builder.toString());
		results.add(tuple);

		/*
		 * store information for upper-expression
		 */
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);

		/*
		 * switch result-type to XTM
		 */
		runtime.getProperties().setProperty(
				TMQLRuntimeProperties.RESULT_SET_IMPLEMENTATION_CLASS,
				CTMResult.class.getCanonicalName());
		runtime.getProperties().setProperty(
				TMQLRuntimeProperties.RESULT_TUPLE_IMPLEMENTATION_CLASS,
				CTMFragment.class.getCanonicalName());
	}
}
