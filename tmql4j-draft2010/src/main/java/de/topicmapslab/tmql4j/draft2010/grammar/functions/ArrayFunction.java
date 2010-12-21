/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2010.grammar.functions;

import java.util.List;
import java.util.Set;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class ArrayFunction extends FunctionImpl {

	public static final String IDENTIFIER = "array";

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return true;
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
		QueryMatches[] arguments = getParameters(runtime, context, caller);
		/*
		 * iterate over arguments
		 */
		List<Object> array = HashUtil.getList();
		for (QueryMatches argument : arguments) {
			for ( Object o : argument.getPossibleValuesForVariable()){
				if ( !array.contains(o)){
					array.add(o);
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
