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
import de.topicmapslab.tmql4j.path.grammar.functions.string.StringLtFunction;
import de.topicmapslab.tmql4j.path.grammar.lexical.LowerThan;

/**
 * @author Sven Krosse
 * 
 */
public class StringLtFunctionTranslator extends ComparisonStringFunctionTranslatorImpl {

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends IFunction> getFunction() {
		return StringLtFunction.class;
	}

	/**
	 * {@inheritDoc}
	 */
	protected String getOperator() {
		return LowerThan.TOKEN;
	}

}
