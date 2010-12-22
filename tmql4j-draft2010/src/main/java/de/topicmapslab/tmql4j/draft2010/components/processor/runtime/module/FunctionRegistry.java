/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2010.components.processor.runtime.module;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.module.FunctionRegistryImpl;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.ArrayFunction;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.CountFunction;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.TopicMapFunction;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.literal.BooleanFunction;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.literal.NumberFunction;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.literal.StringFunction;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.maths.CeilingFunction;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.maths.FloorFunction;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.maths.RoundFunction;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.string.ConcatFunction;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.string.ContainsFunction;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.string.EndsWithFunction;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.string.ExtractRegExpFunction;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.string.FindFunction;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.string.MatchesRegExpFunction;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.string.NormalizeSpaceFunction;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.string.StartsWithFunction;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.string.StringLengthFunction;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.string.SubstringAfterFunction;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.string.SubstringBeforeFunction;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.string.SubstringFunction;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.string.TranslateFunction;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.topicmap.AssociationPatternFct;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.topicmap.TopicByItemIdentifier;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.topicmap.TopicBySubjectIdentifier;
import de.topicmapslab.tmql4j.draft2010.grammar.functions.topicmap.TopicBySubjectLocator;

/**
 * @author Sven Krosse
 * 
 */
public class FunctionRegistry extends FunctionRegistryImpl {

	/**
	 * @param runtime
	 */
	public FunctionRegistry(ITMQLRuntime runtime) {
		super(runtime);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void initialize() {

		registerFunction(CeilingFunction.IDENTIFIER, CeilingFunction.class);
		registerFunction(FloorFunction.IDENTIFIER, FloorFunction.class);
		registerFunction(RoundFunction.IDENTIFIER, RoundFunction.class);

		registerFunction(BooleanFunction.IDENTIFIER, BooleanFunction.class);
		registerFunction(NumberFunction.IDENTIFIER, NumberFunction.class);
		registerFunction(StringFunction.IDENTIFIER, StringFunction.class);

		registerFunction(ConcatFunction.IDENTIFIER, ConcatFunction.class);
		registerFunction(ContainsFunction.IDENTIFIER, ContainsFunction.class);
		registerFunction(EndsWithFunction.IDENTIFIER, EndsWithFunction.class);
		registerFunction(ExtractRegExpFunction.IDENTIFIER, ExtractRegExpFunction.class);
		registerFunction(FindFunction.IDENTIFIER, FindFunction.class);
		registerFunction(MatchesRegExpFunction.IDENTIFIER, MatchesRegExpFunction.class);
		registerFunction(NormalizeSpaceFunction.IDENTIFIER, NormalizeSpaceFunction.class);
		registerFunction(StartsWithFunction.IDENTIFIER, StartsWithFunction.class);
		registerFunction(StringLengthFunction.IDENTIFIER, StringLengthFunction.class);
		registerFunction(StringLengthFunction.IDENTIFIER, StringLengthFunction.class);
		registerFunction(SubstringAfterFunction.IDENTIFIER, SubstringAfterFunction.class);
		registerFunction(SubstringBeforeFunction.IDENTIFIER, SubstringBeforeFunction.class);
		registerFunction(SubstringFunction.IDENTIFIER, SubstringFunction.class);
		registerFunction(TranslateFunction.IDENTIFIER, TranslateFunction.class);

		registerFunction(CountFunction.IDENTIFIER, CountFunction.class);
		registerFunction(TopicMapFunction.IDENTIFIER, TopicMapFunction.class);
		registerFunction(ArrayFunction.IDENTIFIER, ArrayFunction.class);
		registerFunction(TopicBySubjectIdentifier.IDENTIFIER, TopicBySubjectIdentifier.class);
		registerFunction(TopicBySubjectLocator.IDENTIFIER, TopicBySubjectLocator.class);
		registerFunction(TopicByItemIdentifier.IDENTIFIER, TopicByItemIdentifier.class);
		registerFunction(AssociationPatternFct.IDENTIFIER, AssociationPatternFct.class);
	}
}
