/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.majortom.grammar.functions;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.tmapi.core.Topic;

import de.topicmapslab.majortom.model.core.ICharacteristics;
import de.topicmapslab.majortom.model.index.ILiteralIndex;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.functions.FunctionImpl;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class GetTopicsByCharacteristicValue extends FunctionImpl {

	public static final String GetTopicsByCharacteristicValue = "fn:get-topics-by-characteristic-value";

	/**
	 * {@inheritDoc}
	 */
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, IExpressionInterpreter<?> caller) {

		ILiteralIndex index = context.getQuery().getTopicMap().getIndex(ILiteralIndex.class);
		if (!index.isOpen()) {
			index.open();
		}
		QueryMatches parameters = getParameters(runtime, context, caller);
		List<String> orderedKeys = parameters.getOrderedKeys();
		if (!isExpectedNumberOfParameters(orderedKeys.size()))
			throw new TMQLRuntimeException("Illegal Number Of Arguments for " + GetTopicsByCharacteristicValue);

		Set<Topic> topics = HashUtil.getHashSet();

		/*
		 * iterate over parameters
		 */
		for (Map<String, Object> tuple : parameters) {
			/*
			 * iterate over value strings
			 */
			for (String orderedKey : orderedKeys) {
				/*
				 * get parent of characteristics
				 */
				for (ICharacteristics c : index.getCharacteristics(tuple.get(orderedKey).toString())) {
					topics.add((Topic) c.getParent());
				}
			}
		}
		return QueryMatches.asQueryMatchNS(runtime, topics);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return GetTopicsByCharacteristicValue;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters >= 1;
	}

}
