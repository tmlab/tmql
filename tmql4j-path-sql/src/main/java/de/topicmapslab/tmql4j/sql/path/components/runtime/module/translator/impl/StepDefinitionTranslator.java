/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.Step;
import de.topicmapslab.tmql4j.path.grammar.productions.StepDefinition;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.IState;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TmqlSqlTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TranslatorRegistry;

/**
 * @author Sven Krosse
 * 
 */
public class StepDefinitionTranslator extends TmqlSqlTranslatorImpl<StepDefinition> {

	/**
	 * {@inheritDoc}
	 */
	public IState transform(ITMQLRuntime runtime, IContext context, IExpression expression, IState state) throws TMQLRuntimeException {

		IState stepState = TranslatorRegistry.getTranslator(Step.class).transform(runtime, context, expression.getExpressions().get(0), state);

		if (expression.getExpressions().size() > 1) {
			throw new TMQLRuntimeException("Unsupported expression type for SQL translator.");
		}

		return stepState;
	}

}
