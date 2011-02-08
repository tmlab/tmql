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
import de.topicmapslab.tmql4j.path.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.path.grammar.lexical.Asc;
import de.topicmapslab.tmql4j.path.grammar.productions.Content;
import de.topicmapslab.tmql4j.path.grammar.productions.ValueExpression;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.orderBy.OrderBy;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TmqlSqlTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TranslatorRegistry;

/**
 * @author Sven Krosse
 * 
 */
public class ValueExpressionTranslator extends TmqlSqlTranslatorImpl<ValueExpression> {

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public ISqlDefinition toSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		if (expression.getGrammarType() == ValueExpression.TYPE_CONTENT) {
			ISqlDefinition def = TranslatorRegistry.getTranslator(Content.class).toSql(runtime, context, expression.getExpressions().get(0), definition);
			if (((ValueExpression)expression).isAscOrDescOrdering()){
				OrderBy orderBy = new OrderBy(def.getLastSelection().getSelection(), ParserUtils.containsTokens(expression.getTmqlTokens(), Asc.class));
				def.addOrderByPart(orderBy);
			}
			return def;
		}
		throw new TMQLRuntimeException("Unsupported expression type for SQL translator.");
	}

}
