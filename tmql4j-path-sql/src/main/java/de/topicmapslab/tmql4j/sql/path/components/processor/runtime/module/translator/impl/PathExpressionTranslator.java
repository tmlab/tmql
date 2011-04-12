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
import de.topicmapslab.tmql4j.path.grammar.productions.AKOExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.ISAExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.PathExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.PostfixedExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.PredicateInvocation;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.TmqlSqlTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.TranslatorRegistry;

/**
 * @author Sven Krosse
 * 
 */
public class PathExpressionTranslator extends TmqlSqlTranslatorImpl<PathExpression> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISqlDefinition toSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		if (expression.getGrammarType() == PathExpression.TYPE_POSTFIXED_EXPRESSION || expression.getGrammarType() == PathExpression.TYPE_NONCANONICAL_INSTANCE_EXPRESSION) {
			return TranslatorRegistry.getTranslator(PostfixedExpression.class).toSql(runtime, context, expression.getExpressions().get(0), definition);
		} else if (expression.getGrammarType() == PathExpression.TYPE_AKO_EXPRESSION) {
			return TranslatorRegistry.getTranslator(AKOExpression.class).toSql(runtime, context, expression.getExpressions().get(0), definition);
		} else if (expression.getGrammarType() == PathExpression.TYPE_ISA_EXPRESSION) {
			return TranslatorRegistry.getTranslator(ISAExpression.class).toSql(runtime, context, expression.getExpressions().get(0), definition);
		} else if (expression.getGrammarType() == PathExpression.TYPE_PREDICATE_INVOCATION) {
			return TranslatorRegistry.getTranslator(PredicateInvocation.class).toSql(runtime, context, expression.getExpressions().get(0), definition);
		}
		throw new TMQLRuntimeException("Unsupported expression type for SQL translator.");
	}

}
