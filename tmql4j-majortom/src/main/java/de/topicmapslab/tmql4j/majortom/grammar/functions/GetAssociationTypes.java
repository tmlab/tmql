/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.majortom.grammar.functions;

import de.topicmapslab.majortom.model.index.ITransitiveTypeInstanceIndex;
import de.topicmapslab.majortom.model.index.ITypeInstanceIndex;
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.functions.FunctionImpl;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * @author Sven Krosse
 * 
 */
public class GetAssociationTypes extends FunctionImpl {

	public static final String GetAssociationTypes = "fn:get-association-types";

	/**
	 * {@inheritDoc}
	 */
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
		ITypeInstanceIndex index;
		if (parameters.isEmpty()) {
			index = context.getQuery().getTopicMap().getIndex(ITypeInstanceIndex.class);
		} else {
			Object value = parameters.getFirstValue("$0");
			Boolean transitive = Boolean.parseBoolean(LiteralUtils.asString(value.toString()));
			if (transitive) {
				index = context.getQuery().getTopicMap().getIndex(ITransitiveTypeInstanceIndex.class);
			} else {
				index = context.getQuery().getTopicMap().getIndex(ITypeInstanceIndex.class);
			}
		}
		if (!index.isOpen()){
			index.open();
		}
		return QueryMatches.asQueryMatchNS(runtime, index.getAssociationTypes());

	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return GetAssociationTypes;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 0 || numberOfParameters == 1;
	}

}
