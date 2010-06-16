/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.common.context;

import java.util.Map;

import de.topicmapslab.tmql4j.api.impl.tmapi.ConstructResolver;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.IFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.sequences.CompareFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.sequences.ConcatFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.sequences.CountFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.sequences.ExceptFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.sequences.HasDatatypeFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.sequences.HasVariantsFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.sequences.SliceFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.sequences.UniqFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.sequences.ZagZigFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.sequences.ZigZagFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.string.LengthFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.string.RegExpFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.string.StringConcatFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.string.StringGeqFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.string.StringGtFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.string.StringLeqFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.string.StringLtFunctionInvocationInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.interpreter.functions.string.SubStringFunctionInvocationInterpreter;

/**
 * Internal registry for function classes. Provides access to registered
 * functions and enables the registration of new functions.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class FunctionRegistry {

	/**
	 * internal store of all known function interpreter
	 */
	private final Map<String, Class<? extends IFunctionInvocationInterpreter<?>>> functions = HashUtil
			.getHashMap();

	/**
	 * the TMQL runtime instance
	 */
	private final ITMQLRuntime runtime;

	/**
	 * constructor
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 */
	public FunctionRegistry(ITMQLRuntime runtime) {
		this.runtime = runtime;

		/*
		 * initialize all string functions
		 */
		registerFunction("fn:string-concat",
				StringConcatFunctionInvocationInterpreter.class);
		registerFunction("fn:length", LengthFunctionInvocationInterpreter.class);
		registerFunction("fn:string-lt",
				StringLtFunctionInvocationInterpreter.class);
		registerFunction("fn:string-leq",
				StringLeqFunctionInvocationInterpreter.class);
		registerFunction("fn:string-gt",
				StringGtFunctionInvocationInterpreter.class);
		registerFunction("fn:string-geq",
				StringGeqFunctionInvocationInterpreter.class);
		registerFunction("fn:regexp", RegExpFunctionInvocationInterpreter.class);
		registerFunction("fn:substring",
				SubStringFunctionInvocationInterpreter.class);
		/*
		 * set all tuple-sequence functions
		 */
		registerFunction("fn:has-datatype",
				HasDatatypeFunctionInvocationInterpreter.class);
		registerFunction("fn:has-variant",
				HasVariantsFunctionInvocationInterpreter.class);
		registerFunction("fn:slice", SliceFunctionInvocationInterpreter.class);
		registerFunction("fn:count", CountFunctionInvocationInterpreter.class);
		registerFunction("fn:uniq", UniqFunctionInvocationInterpreter.class);
		registerFunction("fn:concat", ConcatFunctionInvocationInterpreter.class);
		registerFunction("fn:except", ExceptFunctionInvocationInterpreter.class);
		registerFunction("fn:compare",
				CompareFunctionInvocationInterpreter.class);
		registerFunction("fn:zigzag", ZigZagFunctionInvocationInterpreter.class);
		registerFunction("fn:zagzig", ZagZigFunctionInvocationInterpreter.class);
	}

	/**
	 * Register a new function in the TMQL engine. Please note that only
	 * registered functions can be used in context of a TMQL query.
	 * 
	 * @param itemIdentifier
	 *            the identifier of the function
	 * @param interpreter
	 *            the interpreter class of the new function
	 */
	public void registerFunction(String itemIdentifier,
			Class<? extends IFunctionInvocationInterpreter<?>> interpreter) {
		functions.put(itemIdentifier, interpreter);
	}

	/**
	 * Method returns the stored interpreter class for a TMQL function
	 * identifies by the given identifier. If the identifier is relative the
	 * runtime tries to resolve the absolute IRI.
	 * 
	 * @param itemIdentifier
	 *            the function identifier
	 * @return the interpreter class if the identifier is known,
	 *         <code>null</code> otherwise
	 */
	public Class<? extends IFunctionInvocationInterpreter<?>> getFunction(
			final String itemIdentifier) {
		/*
		 * get stored interpreter class
		 */
		Class<? extends IFunctionInvocationInterpreter<?>> interpreter = functions
				.get(itemIdentifier);
		/*
		 * no interpreter class found and IRI is relative
		 */
		if (interpreter == null && itemIdentifier.contains(":")) {
			try {
				/*
				 * try to resolve absolute IRI
				 */
				String absoluteIRI = ConstructResolver.toAbsoluteIRI(
						(TMQLRuntime) runtime, itemIdentifier);
				/*
				 * try against with absolute IRI
				 */
				if (!absoluteIRI.equalsIgnoreCase(itemIdentifier)) {
					return getFunction(absoluteIRI);
				}
			} catch (TMQLRuntimeException e) {
				e.printStackTrace();
			}
		}
		return interpreter;
	}

	/**
	 * Method checks if the given identifier is used as identification of a
	 * registered function.
	 * 
	 * @param identifier
	 *            the identifier
	 * @return <code>true</code> if the identifier is known as function,
	 *         <code>false</code> otherwise
	 */
	public boolean isKnownFunction(final String identifier) {
		return functions.containsKey(identifier);
	}

}
