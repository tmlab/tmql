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
package de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.sequences;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.IFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.parser.core.expressions.FunctionInvocation;
import de.topicmapslab.tmql4j.parser.core.expressions.Parameters;

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
public class CompareFunctionInvocationInterpreter
		extends ExpressionInterpreterImpl<FunctionInvocation> implements
		IFunctionInvocationInterpreter<FunctionInvocation> {

	public CompareFunctionInvocationInterpreter(FunctionInvocation ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		QueryMatches results = new QueryMatches(runtime);

		/*
		 * extract arguments
		 */
		QueryMatches parameters = extractArguments(runtime, Parameters.class, 0);
		
		if (parameters.getOrderedKeys().size() < getRequiredVariableCount()) {
			throw new TMQLRuntimeException(getItemIdentifier()
					+ "() requieres " + getRequiredVariableCount()
					+ " parameter.");
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
			result.put(QueryMatches.getNonScopedVariable(), sequenceA
					.retainAll(sequenceB));
			results.add(result);
		}
		runtime.getRuntimeContext().peek().setValue(
				VariableNames.QUERYMATCHES, results);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return "fn:compare";
	}

	/**
	 * {@inheritDoc}
	 */
	public long getRequiredVariableCount() {
		return 2;
	}
}
