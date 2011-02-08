/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.majortom.grammar.functions;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.tmapi.core.Topic;

import de.topicmapslab.majortom.model.index.ISupertypeSubtypeIndex;
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
public class GetSupertypes extends FunctionImpl {

	public static final String GetSupertypes = "fn:get-supertypes";

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context,
			IExpressionInterpreter<?> caller) {
		/*
		 * extract arguments
		 */
		QueryMatches parameters = getParameters(runtime, context, caller);
		/*
		 * check count of variables
		 */
		if (!isExpectedNumberOfParameters(parameters.getOrderedKeys().size())) {
			throw new TMQLRuntimeException(getItemIdentifier() + "() requieres 0 or 1 parameters.");
		}
		
		ISupertypeSubtypeIndex index = context.getQuery().getTopicMap().getIndex(
				ISupertypeSubtypeIndex.class);
		if (!index.isOpen()) {
			index.open();
		}
		/*
		 * no parameters given
		 */
		if ( parameters.isEmpty()){
			return QueryMatches.asQueryMatchNS(runtime, index
					.getSupertypes());
		}
		Set<Topic> supertypes = HashUtil.getHashSet();
		/*
		 * iterate over parameters
		 */
		for (Map<String, Object> tuple : parameters) {
			if (!isExpectedNumberOfParameters(tuple.size())) {
				continue;
			}
			Object oTopics = tuple.get("$0");
			/*
			 * is collection
			 */
			if (oTopics instanceof Collection<?>){
				supertypes.addAll(index.getSupertypes((Collection<? extends Topic>)oTopics));
			}
			/*
			 * is topic
			 */
			else if ( oTopics instanceof Topic ){
				supertypes.addAll(index.getSupertypes((Topic)oTopics));
			}
		}
		/*
		 * return result
		 */
		if ( supertypes.isEmpty()){
			return QueryMatches.emptyMatches();
		}
		return QueryMatches.asQueryMatchNS(runtime, supertypes.toArray());

	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return GetSupertypes;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 0 || numberOfParameters == 1;
	}

}
