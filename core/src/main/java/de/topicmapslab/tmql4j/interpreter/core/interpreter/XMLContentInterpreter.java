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

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.common.context.TMQLRuntimeProperties;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.model.context.IVariableSet;
import de.topicmapslab.tmql4j.interpreter.utility.xtm.XTMConverter;
import de.topicmapslab.tmql4j.parser.core.expressions.NonInterpretedContent;
import de.topicmapslab.tmql4j.parser.core.expressions.XMLContent;
import de.topicmapslab.tmql4j.resultprocessing.core.xml.XMLFragment;
import de.topicmapslab.tmql4j.resultprocessing.core.xml.XMLResult;

/**
 * 
 * Special interpreter class to interpret xml-content.
 * <p>
 * The grammar production rule of the expression is: <code>
 * xml-content ::= every-valid-XML
 * </code>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class XMLContentInterpreter extends
		ExpressionInterpreterImpl<XMLContent> {

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
	public XMLContentInterpreter(XMLContent ex) {
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
			 * results are not empty and has to be converted to XTM or XML
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
				 * results as XML or XTM
				 */
				builder.append(XTMConverter.toXTMString(values, runtime));
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
				XMLResult.class.getCanonicalName());
		runtime.getProperties().setProperty(
				TMQLRuntimeProperties.RESULT_TUPLE_IMPLEMENTATION_CLASS,
				XMLFragment.class.getCanonicalName());
	}
}
