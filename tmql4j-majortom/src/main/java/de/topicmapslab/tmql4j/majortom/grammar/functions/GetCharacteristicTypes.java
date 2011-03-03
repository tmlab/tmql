/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.majortom.grammar.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.majortom.model.core.ICharacteristics;
import de.topicmapslab.majortom.model.core.ITopic;
import de.topicmapslab.majortom.model.index.ITransitiveTypeInstanceIndex;
import de.topicmapslab.majortom.model.index.ITypeInstanceIndex;
import de.topicmapslab.majortom.util.HashUtil;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.path.grammar.functions.FunctionImpl;
import de.topicmapslab.tmql4j.path.grammar.productions.Parameters;

/**
 * @author Sven Krosse
 * 
 */
public class GetCharacteristicTypes extends FunctionImpl {

	public static final String GetCharacteristicTypes = "fn:get-characteristic-types";

	/**
	 * {@inheritDoc}
	 */
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, IExpressionInterpreter<?> caller) {

		ITypeInstanceIndex index = context.getQuery().getTopicMap().getIndex(ITypeInstanceIndex.class);
		if (!index.isOpen()) {
			index.open();
		}
		/*
		 * has arguments ?
		 */
		if (caller.containsExpressionsType(Parameters.class)) {
			QueryMatches results = new QueryMatches(runtime);
			QueryMatches parameters = getParameters(runtime, context, caller);
			/*
			 * iterate over parameters
			 */
			for (Map<String, Object> tuple : parameters) {
				Object param0 = tuple.get("$0");
				Object duplicates = tuple.get("$1");
				if (param0 instanceof ITopic) {
					ITopic type = (ITopic) param0;
					Set<List<Topic>> characteristicsTypesByInstance = HashUtil.getHashSet();
					Set<Topic> characteristicsTypesByType = HashUtil.getHashSet();
					boolean duplicate = duplicates != null && Boolean.parseBoolean(duplicates.toString());
					/*
					 * get instances
					 */
					for (Topic t : index.getTopics(type)) {
						List<Topic> list = new ArrayList<Topic>();
						/*
						 * get characteristic types
						 */
						for (ICharacteristics c : ((ITopic) t).getCharacteristics()) {
							Topic ty = c.getType();
							if (duplicate) {
								list.add(ty);
							} else {
								characteristicsTypesByType.add(ty);
							}
						}
						if (!characteristicsTypesByInstance.contains(list)) {
							characteristicsTypesByInstance.add(list);
						}
					}
					/*
					 * set results
					 */
					/*
					 * set results
					 */
					if (duplicate) {
						for (List<Topic> rt : characteristicsTypesByInstance) {
							Map<String, Object> t = HashUtil.getHashMap();
							t.put("$0", rt);
							results.add(t);
						}
					}else{
						Map<String, Object> t = HashUtil.getHashMap();
						t.put("$0", characteristicsTypesByType);
						results.add(t);
					}
				}
				/*
				 * second argument is boolean argument
				 */
				else if ( param0 != null && Boolean.parseBoolean(param0.toString())){
					return QueryMatches.asQueryMatchNS(runtime, getTransitiveIndex(context.getQuery().getTopicMap())
							.getCharacteristicTypes());
				}
			}
			return results;
		}
		return QueryMatches.asQueryMatchNS(runtime, index.getCharacteristicTypes());

	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return GetCharacteristicTypes;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 0 || numberOfParameters == 1 || numberOfParameters == 2;
	}
	
	/**
	 * Internal method to get the transitive index
	 * @param topicMap the topic map
	 * @return the transitive index
	 */
	private ITransitiveTypeInstanceIndex getTransitiveIndex(TopicMap topicMap){
		ITransitiveTypeInstanceIndex index = topicMap.getIndex(
				ITransitiveTypeInstanceIndex.class);
		if (!index.isOpen()) {
			index.open();
		}
		return index;
	}

}
