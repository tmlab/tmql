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
package de.topicmapslab.tmql4j.path.grammar.functions.string;

import java.util.Collection;
import java.util.List;
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
 * 
 * </code>
 * </p>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class StringConcatFunction extends
		FunctionImpl {

	/**
	 * {@inheritDoc}
	 */
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
		 * iterate over parameters
		 */
		for (Map<String, Object> tuple : parameters) {
			Object s = tuple.get("$0");
			Object b = tuple.get("$1");
			List<String> seq = HashUtil.getList();
			/*
			 * check if 2nd argument is a sequence
			 */
			if (b instanceof Collection<?>) {
				/*
				 * check if 1st is a sequence
				 */
				if (s instanceof Collection<?>) {
					/*
					 * add all possible combinations
					 */
					for (Object oS : (Collection<?>) s) {
						for (Object oB : (Collection<?>) b) {
							seq.add(oS.toString() + oB.toString());
						}
					}
				} else {
					/*
					 * add all possible combinations
					 */
					for (Object oB : (Collection<?>) b) {
						seq.add(s.toString() + oB.toString());
					}
				}
			}
			/*
			 * check if 1st is a sequence
			 */
			else if (s instanceof Collection<?>) {
				/*
				 * add all possible combinations
				 */
				for (Object oS : (Collection<?>) s) {
					seq.add(oS.toString() + b.toString());

				}
			} else {
				/*
				 * add the combination of the two strings
				 */
				seq.add(s.toString() + b.toString());
			}

			Map<String, Object> result = HashUtil.getHashMap();
			/*
			 * extract singleton value
			 */
			result.put(QueryMatches.getNonScopedVariable(),
					seq.size() == 1 ? seq.iterator().next() : seq);
			results.add(result);
		}
		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return "fn:string-concat";
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 2;
	}
}
