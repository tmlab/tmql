/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.string;

import de.topicmapslab.tmql4j.grammar.productions.IFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.string.StringLeqFunction;
import de.topicmapslab.tmql4j.path.grammar.lexical.LowerEquals;

/**
 * @author Sven Krosse
 * 
 */
public class StringLeqFunctionTranslator extends ComparisonStringFunctionTranslatorImpl {

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends IFunction> getFunction() {
		return StringLeqFunction.class;
	}

	/**
	 * {@inheritDoc}
	 */
	protected String getOperator() {
		return LowerEquals.TOKEN;
	}

}
