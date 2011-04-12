/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.aggregate;

import de.topicmapslab.tmql4j.grammar.productions.IFunction;
import de.topicmapslab.tmql4j.path.grammar.functions.aggregate.MinFunction;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants;

/**
 * @author Sven Krosse
 * 
 */
public class MinFunctionTranslator extends AggregateFunctionTranslatorImpl {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<? extends IFunction> getFunction() {
		return MinFunction.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getFunctionName() {
		return ISqlConstants.ISqlFunctions.MIN;
	}
}
