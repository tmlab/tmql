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

import org.tmapi.core.Association;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

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
public class GetRoleTypes extends FunctionImpl {

	public static final String GetRoleTypes = "fn:get-role-types";

	/**
	 * {@inheritDoc}
	 */
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context,
			IExpressionInterpreter<?> caller) {
		
		ITypeInstanceIndex index = context.getQuery().getTopicMap().getIndex(
				ITypeInstanceIndex.class);
		if (!index.isOpen()) {
			index.open();
		}
		/*
		 * has arguments ?
		 */
		if ( caller.containsExpressionsType(Parameters.class)){
			QueryMatches results = new QueryMatches(runtime);
			QueryMatches parameters = getParameters(runtime, context, caller);
			/*
			 * iterate over parameters
			 */
			for (Map<String, Object> tuple : parameters) {
				Object param0 = tuple.get("$0");
				Object duplicates = tuple.get("$1");
				if ( param0 instanceof ITopic ){
					ITopic type = (ITopic) param0;
					Set<List<Topic>> roleTypes = HashUtil.getHashSet();
					/*
					 * get instances
					 */
					for ( Association a : index.getAssociations(type)){						
						List<Topic> list = new ArrayList<Topic>();
						/*
						 * get role types with duplicates
						 */
						if ( duplicates != null && Boolean.parseBoolean(duplicates.toString())){
							for ( Role r : a.getRoles()){
								list.add(r.getType());
							}
						}
						/*
						 * each role-type only one times
						 */
						else{
							list.addAll(a.getRoleTypes());
						}
						if ( !roleTypes.contains(list)){
							roleTypes.add(list);
						}
					}
					/*
					 * set results
					 */
					for ( List<Topic> rt : roleTypes){
						Map<String, Object> t = HashUtil.getHashMap();						
						t.put("$0", rt);
						results.add(t);
					}
				}
				/*
				 * second argument is boolean argument
				 */
				else if ( param0 != null && Boolean.parseBoolean(param0.toString())){
					return QueryMatches.asQueryMatchNS(runtime, getTransitiveIndex(context.getQuery().getTopicMap())
							.getRoleTypes());
				}
			}
			return results;
		}
		return QueryMatches.asQueryMatchNS(runtime, index
				.getRoleTypes());

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
	

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return GetRoleTypes;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 0 || numberOfParameters == 1 || numberOfParameters == 2;
	}

}
