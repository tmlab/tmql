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
import java.util.Map;

import org.tmapi.core.Name;
import org.tmapi.core.Variant;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.model.tuplesequence.ITupleSequence;
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
 * fn:has-variant (s: tuple-sequence, s: item-reference) return tuple-sequence
 * </code>
 * </p>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class HasVariantsFunctionInvocationInterpreter extends
		ExpressionInterpreterImpl<FunctionInvocation> implements
		IFunctionInvocationInterpreter<FunctionInvocation> {

	/**
	 * Base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression of type function-invocation to interpret
	 */
	public HasVariantsFunctionInvocationInterpreter(FunctionInvocation ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		QueryMatches results = new QueryMatches(runtime);
		/*
		 * extract arguments
		 */
		QueryMatches parameters = extractArguments(runtime, Parameters.class, 0);

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
			Object value = tuple.get("$0");
			Object scope = tuple.get("$1");
			Map<String, Object> result = HashUtil.getHashMap();
			ITupleSequence<Variant> variants = runtime.getProperties()
					.newSequence();
			/*
			 * check if value is a name
			 */
			if (value instanceof Name) {
				/*
				 * add all variants
				 */
				for (Variant variant : ((Name) value).getVariants()) {
					if (variant.getScope().contains(scope)) {
						variants.add(variant);
					}
				}
			}
			/*
			 * check if value is a collection
			 */
			else if (value instanceof Collection<?>) {
				/*
				 * iterate over values
				 */
				for (Object v : (Collection<?>) value) {
					/*
					 * check if value is a name
					 */
					if (v instanceof Name) {
						/*
						 * add all variants
						 */
						for (Variant variant : ((Name) v).getVariants()) {
							if (variant.getScope().contains(scope)) {
								variants.add(variant);
							}
						}
					}
				}
			}
			/*
			 * add undef-topic if result is empty
			 */
			if (variants.isEmpty()) {
				result.put(QueryMatches.getNonScopedVariable(), runtime
						.getInitialContext().getEnvironment()
						.getTmqlTopicUndef());
			}
			/*
			 * add results
			 */
			else {
				result.put(QueryMatches.getNonScopedVariable(), variants);
			}
			results.add(result);
		}

		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getItemIdentifier() {
		return "fn:has-variant";
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 2;
	}

}
