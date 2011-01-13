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
import de.topicmapslab.tmql4j.path.grammar.productions.Navigation;
import de.topicmapslab.tmql4j.path.grammar.productions.StepDefinition;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.IState;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.ITmqlSqlTranslator;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TmqlSqlTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TranslatorRegistry;

/**
 * @author Sven Krosse
 * 
 */
public class NavigationTranslator extends TmqlSqlTranslatorImpl<Navigation> {

	/**
	 * {@inheritDoc}
	 */
	public IState transform(ITMQLRuntime runtime, IContext context, IExpression expression, IState state) throws TMQLRuntimeException {
		IState state_ = state;
		ITmqlSqlTranslator<?> translator = TranslatorRegistry.getTranslator(StepDefinition.class);
		for (IExpression e : expression.getExpressions()) {
			state_ = translator.transform(runtime, context, e, state_);
		}
		return state_;
	}

}
