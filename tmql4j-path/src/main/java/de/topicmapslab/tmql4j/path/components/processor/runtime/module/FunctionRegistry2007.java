/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.components.processor.runtime.module;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.module.FunctionRegistry;

/**
 * Internal registry for function classes. Provides access to registered
 * functions and enables the registration of new functions.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class FunctionRegistry2007 extends FunctionRegistry {

	/**
	 * constructor
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 */
	public FunctionRegistry2007(ITMQLRuntime runtime) {
		super(runtime);
	}

	protected void initialize() {
		// /*
		// * initialize all string functions
		// */
		// registerFunction("fn:string-concat",
		// StringConcatFunctionInvocationInterpreter.class);
		// registerFunction("fn:length",
		// LengthFunctionInvocationInterpreter.class);
		// registerFunction("fn:string-lt",
		// StringLtFunctionInvocationInterpreter.class);
		// registerFunction("fn:string-leq",
		// StringLeqFunctionInvocationInterpreter.class);
		// registerFunction("fn:string-gt",
		// StringGtFunctionInvocationInterpreter.class);
		// registerFunction("fn:string-geq",
		// StringGeqFunctionInvocationInterpreter.class);
		// registerFunction("fn:regexp",
		// RegExpFunctionInvocationInterpreter.class);
		// registerFunction("fn:substring",
		// SubStringFunctionInvocationInterpreter.class);
		// /*
		// * set all tuple-sequence functions
		// */
		// registerFunction("fn:has-datatype",
		// HasDatatypeFunctionInvocationInterpreter.class);
		// registerFunction("fn:has-variant",
		// HasVariantsFunctionInvocationInterpreter.class);
		// registerFunction("fn:slice",
		// SliceFunctionInvocationInterpreter.class);
		// registerFunction("fn:count",
		// CountFunctionInvocationInterpreter.class);
		// registerFunction("fn:uniq", UniqFunctionInvocationInterpreter.class);
		// registerFunction("fn:concat",
		// ConcatFunctionInvocationInterpreter.class);
		// registerFunction("fn:except",
		// ExceptFunctionInvocationInterpreter.class);
		// registerFunction("fn:compare",
		// CompareFunctionInvocationInterpreter.class);
		// registerFunction("fn:zigzag",
		// ZigZagFunctionInvocationInterpreter.class);
		// registerFunction("fn:zagzig",
		// ZagZigFunctionInvocationInterpreter.class);
		// registerFunction("fn:url-encode",
		// UrlEncodeFunctionInterpreter.class);
		// registerFunction("fn:url-decode",
		// UrlDecodeFunctionInterpreter.class);
	}

}
