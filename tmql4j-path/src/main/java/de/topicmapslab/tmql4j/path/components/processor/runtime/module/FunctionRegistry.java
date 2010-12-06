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
import de.topicmapslab.tmql4j.components.processor.runtime.module.FunctionRegistryImpl;
import de.topicmapslab.tmql4j.path.grammar.functions.sequences.CompareFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.sequences.ConcatFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.sequences.CountFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.sequences.ExceptFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.sequences.HasDatatypeFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.sequences.HasVariantsFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.sequences.SliceFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.sequences.UniqFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.sequences.ZagZigFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.sequences.ZigZagFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.string.LengthFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.string.RegExpFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.string.StringConcatFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.string.StringGeqFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.string.StringGtFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.string.StringLeqFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.string.StringLtFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.string.SubStringFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.url.UrlDecodeFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.url.UrlEncodeFunction;

/**
 * Internal registry for function classes. Provides access to registered
 * functions and enables the registration of new functions.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class FunctionRegistry extends FunctionRegistryImpl {

	/**
	 * constructor
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 */
	public FunctionRegistry(ITMQLRuntime runtime) {
		super(runtime);
	}

	protected void initialize() {
		/*
		 * initialize all string functions
		 */
		registerFunction("fn:string-concat", StringConcatFunction.class);
		registerFunction("fn:length", LengthFunction.class);
		registerFunction("fn:string-lt", StringLtFunction.class);
		registerFunction("fn:string-leq", StringLeqFunction.class);
		registerFunction("fn:string-gt", StringGtFunction.class);
		registerFunction("fn:string-geq", StringGeqFunction.class);
		registerFunction("fn:regexp", RegExpFunction.class);
		registerFunction("fn:substring", SubStringFunction.class);
		/*
		 * set all tuple-sequence functions
		 */
		registerFunction("fn:has-datatype", HasDatatypeFunction.class);
		registerFunction("fn:has-variant", HasVariantsFunction.class);
		registerFunction("fn:slice", SliceFunction.class);
		registerFunction("fn:count", CountFunction.class);
		registerFunction("fn:uniq", UniqFunction.class);
		registerFunction("fn:concat", ConcatFunction.class);
		registerFunction("fn:except", ExceptFunction.class);
		registerFunction("fn:compare", CompareFunction.class);
		registerFunction("fn:zigzag", ZigZagFunction.class);
		registerFunction("fn:zagzig", ZagZigFunction.class);
		registerFunction("fn:url-encode", UrlEncodeFunction.class);
		registerFunction("fn:url-decode", UrlDecodeFunction.class);
	}

}
