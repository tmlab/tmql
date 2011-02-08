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
import de.topicmapslab.tmql4j.path.grammar.functions.sequences.ArrayFunction;
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
import de.topicmapslab.tmql4j.path.grammar.functions.topicmap.TopicsByItemIdentifier;
import de.topicmapslab.tmql4j.path.grammar.functions.topicmap.TopicsBySubjectIdentifier;
import de.topicmapslab.tmql4j.path.grammar.functions.topicmap.TopicsBySubjectLocator;
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
		registerFunction(StringConcatFunction.IDENTIFIER, StringConcatFunction.class);
		registerFunction(LengthFunction.IDENTIFIER, LengthFunction.class);
		registerFunction(StringLtFunction.IDENTIFIER, StringLtFunction.class);
		registerFunction(StringLeqFunction.IDENTIFIER, StringLeqFunction.class);
		registerFunction(StringGtFunction.IDENTIFIER, StringGtFunction.class);
		registerFunction(StringGeqFunction.IDENTIFIER, StringGeqFunction.class);
		registerFunction(RegExpFunction.IDENTIFIER, RegExpFunction.class);
		registerFunction(SubStringFunction.IDENTIFIER, SubStringFunction.class);
		/*
		 * set all tuple-sequence functions
		 */
		registerFunction(HasDatatypeFunction.IDENTIFIER, HasDatatypeFunction.class);
		registerFunction(HasVariantsFunction.IDENTIFIER, HasVariantsFunction.class);
		registerFunction(SliceFunction.IDENTIFIER, SliceFunction.class);
		registerFunction(CountFunction.IDENTIFIER, CountFunction.class);
		registerFunction(UniqFunction.IDENTIFIER, UniqFunction.class);
		registerFunction(ConcatFunction.IDENTIFIER, ConcatFunction.class);
		registerFunction(ExceptFunction.IDENTIFIER, ExceptFunction.class);
		registerFunction(CompareFunction.IDENTIFIER, CompareFunction.class);
		registerFunction(ZigZagFunction.IDENTIFIER, ZigZagFunction.class);
		registerFunction(ZagZigFunction.IDENTIFIER, ZagZigFunction.class);
		registerFunction(ArrayFunction.IDENTIFIER, ArrayFunction.class);

		registerFunction(UrlEncodeFunction.IDENTIFIER, UrlEncodeFunction.class);
		registerFunction(UrlDecodeFunction.IDENTIFIER, UrlDecodeFunction.class);

		registerFunction(TopicsByItemIdentifier.IDENTIFIER, TopicsByItemIdentifier.class);
		registerFunction(TopicsBySubjectIdentifier.IDENTIFIER, TopicsBySubjectIdentifier.class);
		registerFunction(TopicsBySubjectLocator.IDENTIFIER, TopicsBySubjectLocator.class);

	}

}
