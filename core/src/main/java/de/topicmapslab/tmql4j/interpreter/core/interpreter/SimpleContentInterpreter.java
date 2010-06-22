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
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.IExpressionInterpreter;
import de.topicmapslab.tmql4j.interpreter.model.context.IVariableSet;
import de.topicmapslab.tmql4j.parser.core.expressions.Anchor;
import de.topicmapslab.tmql4j.parser.core.expressions.Navigation;
import de.topicmapslab.tmql4j.parser.core.expressions.SimpleContent;

/**
 * 
 * Special interpreter class to interpret simple-content.
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * simple-content ::= anchor [ navigation ]
 * </p>
 * </code> </p>
 * 
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SimpleContentInterpreter extends
		ExpressionInterpreterImpl<SimpleContent> {

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
	public SimpleContentInterpreter(SimpleContent ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		/*
		 * interpret anchor
		 */
		QueryMatches anchor = extractArguments(runtime, Anchor.class, 0);

		/*
		 * don't contains a subexpression
		 */
		if (getInterpreters(runtime).size() == 1) {
			runtime.getRuntimeContext().peek().setValue(
					VariableNames.QUERYMATCHES, anchor);
		}
		/*
		 * contains a subexpression
		 */
		else {
			Anchor a = getExpression()
					.getExpressionFilteredByType(Anchor.class).get(0);
			if (anchor.isEmpty() && a.getGrammarType() == Anchor.TYPE_VARIABLE) {
				Set<Topic> topics = runtime.getTopicMap().getTopics();
				anchor.convertToTuples(topics);
			}
			/*
			 * special interpretation for tm:subject
			 */
			else if (anchor.isEmpty()
					&& ("tm:subject".equalsIgnoreCase(a.getTokens().get(0)) || "tmdm:subject"
							.equalsIgnoreCase(a.getTokens().get(0)))) {
				Map<String, Object> tuple = HashUtil.getHashMap();
				tuple.put(QueryMatches.getNonScopedVariable(), a.getTokens()
						.get(0));
				anchor.add(tuple);
			}
			QueryMatches results = new QueryMatches(runtime);
			/*
			 * simple-content can only contain one expression of type navigation
			 */
			IExpressionInterpreter<Navigation> ex = getInterpretersFilteredByEypressionType(
					runtime, Navigation.class).get(0);
			/*
			 * Iterate over all possible bindings
			 */
			for (Object match : anchor.getPossibleValuesForVariable()) {
				/*
				 * push new instance of @_ to stack
				 */
				runtime.getRuntimeContext().push().setValue(
						VariableNames.CURRENT_TUPLE, match);
				/*
				 * call subexpression ( navigation )
				 */
				ex.interpret(runtime);
				/*
				 * read value of results from top of the stack and pop from
				 * stack
				 */
				IVariableSet set = runtime.getRuntimeContext().pop();
				Object obj = set.getValue(VariableNames.QUERYMATCHES);

				/*
				 * redirect results
				 */
				if (obj instanceof QueryMatches) {
					for (Map<String, Object> object : (QueryMatches) obj) {
						Map<String, Object> binding = HashUtil.getHashMap();
						binding.putAll(object);
						/*
						 * check if anchor is a variable and add an entry to the
						 * tuple containing the binding of this variable
						 */
						if (a.getGrammarType() == Anchor.TYPE_VARIABLE) {
							binding.put(a.getTokens().get(0), match);
						}
						results.add(binding);
					}
				}

				/*
				 * log it :)
				 */
				logger.info("Iteration finished! Results: " + results);
				runtime.getRuntimeContext().peek().setValue(
						VariableNames.QUERYMATCHES, results);

			}

			runtime.getRuntimeContext().peek().setValue(
					VariableNames.QUERYMATCHES, results);
		}

		/*
		 * log it :)
		 */
		logger.info("Finishing! Results: "
				+ runtime.getRuntimeContext().peek().getValue(
						VariableNames.QUERYMATCHES));

	}
}
