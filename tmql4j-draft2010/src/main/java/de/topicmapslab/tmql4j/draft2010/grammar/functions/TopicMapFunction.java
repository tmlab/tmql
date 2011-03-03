package de.topicmapslab.tmql4j.draft2010.grammar.functions;

import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;

/**
 * Interpreter implementation of function 'topicmap'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TopicMapFunction extends FunctionImpl {

	/**
	 * 
	 */
	public static final String IDENTIFIER = "topicmap";

	/**
	 * {@inheritDoc}
	 */

	public String getItemIdentifier() {
		return IDENTIFIER;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 1;
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, IExpressionInterpreter<?> caller) {
		/*
		 * get the current topic map
		 */
		TopicMap topicMap = context.getQuery().getTopicMap();
		return QueryMatches.asQueryMatchNS(runtime, topicMap);
	}

}
