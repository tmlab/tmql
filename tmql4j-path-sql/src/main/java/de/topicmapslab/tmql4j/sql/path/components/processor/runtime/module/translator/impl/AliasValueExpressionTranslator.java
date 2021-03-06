/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.components.interpreter.AliasExpressionInterpreter;
import de.topicmapslab.tmql4j.path.grammar.productions.AliasExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.AliasValueExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.ValueExpression;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.TmqlSqlTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.TranslatorRegistry;

/**
 * @author Sven Krosse
 * 
 */
public class AliasValueExpressionTranslator extends TmqlSqlTranslatorImpl<AliasValueExpression> {

	/**
	 * {@inheritDoc}
	 */
	public ISqlDefinition toSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		ISqlDefinition newState = TranslatorRegistry.getTranslator(ValueExpression.class).toSql(runtime, context, expression.getExpressions().get(0), definition);
		
			/*
			 * execute alias if exists
			 */
			if (expression.contains(AliasExpression.class)) {
				new AliasExpressionInterpreter(expression.getExpressionFilteredByType(AliasExpression.class).get(0)).interpret(runtime, context);
			}
		return newState;
	}

}
