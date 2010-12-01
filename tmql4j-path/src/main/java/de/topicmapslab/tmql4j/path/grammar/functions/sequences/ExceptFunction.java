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
package de.topicmapslab.tmql4j.path.grammar.functions.sequences;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.functions.FunctionImpl;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Special implementation of an {@link IFunctionInvocationInterpreter}.
 * <p>
 * This implementation realize the execution of the following function:
 * <p>
 * <code>
 * fn:except (s : tuple-sequence, t : tuple-sequence) return tuple-sequence
 * </code>
 * </p>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ExceptFunction extends
FunctionImpl {

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public de.topicmapslab.tmql4j.components.processor.core.QueryMatches interpret(ITMQLRuntime runtime, IContext context, IExpressionInterpreter<?> caller) {
		QueryMatches results = new QueryMatches(runtime);

		/*
		 * extract arguments
		 */
		QueryMatches parameters = getParameters(runtime, context, caller);

		/*
		 * check count of variables
		 */
		if (!isExpectedNumberOfParameters(parameters.getOrderedKeys().size())) {
			throw new TMQLRuntimeException(getItemIdentifier()
					+ "() requieres 2 parameters.");
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
			 * get first sequence
			 */
			if (a instanceof Collection<?>) {
				sequenceA = (Collection<Object>) a;
			} else {
				sequenceA = new LinkedList<Object>();
				sequenceA.add(a);
			}

			/*
			 * get second sequence
			 */
			Collection<Object> sequenceB;
			if (b instanceof Collection<?>) {
				sequenceB = (Collection<Object>) b;
			} else {
				sequenceB = new LinkedList<Object>();
				sequenceB.add(b);
			}
			/*
			 * remove value of second sequence for the first sequences
			 */
			sequenceA.removeAll(sequenceB);
			result.put(QueryMatches.getNonScopedVariable(), sequenceA);
			results.add(result);
		}
		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return "fn:except";
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 2;
	}
}
