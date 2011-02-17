/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.grammar.functions.aggregate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * @author Sven Krosse
 *
 */
public class MaxFunction extends AggregateFunctionImpl {

	public static final String IDENTIFIER = "fn:max";
	
	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return IDENTIFIER;
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected QueryMatches doAggregation(ITMQLRuntime runtime, IContext context, List<Object> values) throws TMQLRuntimeException {
		Collections.sort(values, new Comparator<Object>(){
			/**
			 * {@inheritDoc}
			 */
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public int compare(Object arg0, Object arg1) {
				if ( arg0 instanceof Comparable<?> && arg1 instanceof Comparable<?> ){
					return ((Comparable)arg0).compareTo((Comparable)arg1);
				}
				throw new TMQLRuntimeException("The values cannot be compared");
			}
		});		
		/*
		 * get last value as max
		 */
		return QueryMatches.asQueryMatchNS(runtime, values.get(values.size()-1));
	}

}
