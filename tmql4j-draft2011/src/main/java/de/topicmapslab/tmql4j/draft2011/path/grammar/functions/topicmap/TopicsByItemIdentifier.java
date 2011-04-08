/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2011.path.grammar.functions.topicmap;

import java.util.Map;
import java.util.Set;

import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.grammar.functions.FunctionImpl;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class TopicsByItemIdentifier extends FunctionImpl {

	public static final String IDENTIFIER = "fn:topics-by-itemidentifier";

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters > 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return IDENTIFIER;
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, IExpressionInterpreter<?> caller) {
		/*
		 * get arguments
		 */
		QueryMatches arguments = getParameters(runtime, context, caller);
		/*
		 * iterate over arguments
		 */
		Set<Topic> array = HashUtil.getHashSet();
		/*
		 * iterate over parameters
		 */
		for (Map<String, Object> tuple : arguments) {
			for (Object obj : tuple.values()) {
				try {
					array.add((Topic)runtime.getConstructResolver().getConstructByItemIdentifier(context, obj.toString()));
				} catch (Exception e) {
					// IGNORE
				}
			}
		}
		/*
		 * create result
		 */
		if (array.isEmpty()) {
			return QueryMatches.emptyMatches();
		}
		return QueryMatches.asQueryMatchNS(runtime, array);
	}

}
