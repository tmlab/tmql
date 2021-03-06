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
import de.topicmapslab.tmql4j.path.grammar.productions.Postfix;
import de.topicmapslab.tmql4j.path.grammar.productions.PostfixedExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.SimpleContent;
import de.topicmapslab.tmql4j.path.grammar.productions.TupleExpression;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.TmqlSqlTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.TranslatorRegistry;

/**
 * @author Sven Krosse
 *
 */
public class PostfixedExpressionTranslator extends TmqlSqlTranslatorImpl<PostfixedExpression> {

	/**
	 * {@inheritDoc}
	 */
	public ISqlDefinition toSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		if ( expression.getGrammarType() == PostfixedExpression.TYPE_SIMPLE_CONTENT){
			ISqlDefinition newDefinition = TranslatorRegistry.getTranslator(SimpleContent.class).toSql(runtime, context, expression.getExpressions().get(0), definition);
			if ( expression.getExpressions().size() > 1){
				newDefinition = TranslatorRegistry.getTranslator(Postfix.class).toSql(runtime, context, expression.getExpressions().get(1), newDefinition);
			}
			return newDefinition;
		}
		return TranslatorRegistry.getTranslator(TupleExpression.class).toSql(runtime, context, expression.getExpressions().get(0), definition);
	}

}
