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
package de.topicmapslab.tmql4j.draft2011.path.grammar.functions.sequences;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.grammar.functions.FunctionImpl;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Special implementation of an {@link IFunctionInvocationInterpreter}.
 * <p>
 * This implementation realize the execution of the following function:
 * <p>
 * <code>
 * fn:compare (s : tuple-sequence, t : tuple-sequence) return tuple-sequence
 * </code>
 * </p>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class CompareFunction extends FunctionImpl {

	/**
	 * 
	 */
	public static final String IDENTIFIER = "fn:compare";

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, IExpressionInterpreter<?> caller) {
		QueryMatches results = new QueryMatches(runtime);

		/*
		 * extract arguments
		 */
		QueryMatches parameters = getParameters(runtime, context, caller);

		if (!isExpectedNumberOfParameters(parameters.getOrderedKeys().size())) {
			throw new TMQLRuntimeException(getItemIdentifier() + "() requieres 2 parameters.");
		}

		/*
		 * iterate over all parameters
		 */
		for (Map<String, Object> tuple : parameters) {
			Object a = tuple.get("$0");
			Object b = tuple.get("$1");
			Map<String, Object> result = HashUtil.getHashMap();
			Collection<Object> sequenceA;

			/*
			 * extract first sequence
			 */
			if (a instanceof Collection<?>) {
				sequenceA = (Collection<Object>) a;
			} else {
				sequenceA = new LinkedList<Object>();
				sequenceA.add(a);
			}

			/*
			 * extract second sequence
			 */
			Collection<Object> sequenceB;
			if (b instanceof Collection<?>) {
				sequenceB = (Collection<Object>) b;
			} else {
				sequenceB = new LinkedList<Object>();
				sequenceB.add(b);
			}
			/*
			 * combine tuples
			 */
			sequenceA.retainAll(sequenceB);
			result.put("$0", !sequenceA.isEmpty());
			results.add(result);
		}
		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getItemIdentifier() {
		return IDENTIFIER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 2;
	}
}
