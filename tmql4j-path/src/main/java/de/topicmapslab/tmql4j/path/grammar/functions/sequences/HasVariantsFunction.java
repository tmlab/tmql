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
import java.util.List;
import java.util.Map;

import org.tmapi.core.Name;
import org.tmapi.core.Variant;

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
 * fn:has-variant (s: tuple-sequence, s: item-reference) return tuple-sequence
 * </code>
 * </p>
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class HasVariantsFunction extends
FunctionImpl {

	/**
	 * 
	 */
	public static final String IDENTIFIER = "fn:has-variant";

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
			Object value = tuple.get("$0");
			Object scope = tuple.get("$1");
			Map<String, Object> result = HashUtil.getHashMap();

			List<Variant> variants = HashUtil.getList();
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
				return QueryMatches.emptyMatches();
			}
			/*
			 * add results
			 */
			else {
				result.put(QueryMatches.getNonScopedVariable(), variants);
			}
			results.add(result);
		}

		return results;
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
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 2;
	}

}
