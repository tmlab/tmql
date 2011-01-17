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
import de.topicmapslab.tmql4j.path.grammar.productions.BooleanPrimitive;
import de.topicmapslab.tmql4j.path.grammar.productions.ExistsClause;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TmqlSqlTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TranslatorRegistry;

/**
 * @author Sven Krosse
 *
 */
public class BooleanPrimitiveTranslator extends TmqlSqlTranslatorImpl<BooleanPrimitive> {

	/**
	 * {@inheritDoc}
	 */
	public ISqlDefinition toSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		switch(expression.getGrammarType()){
			case BooleanPrimitive.TYPE_BOOLEAN_EXPRESSION:{
				return booleanToSql(runtime, context, expression, definition);
			}
			case BooleanPrimitive.TYPE_EVERY_CLAUSE:{
				return everyToSql(runtime, context, expression, definition);
			}
			case BooleanPrimitive.TYPE_EXISTS_CLAUSE:{
				return existsToSql(runtime, context, expression, definition);
			}
			case BooleanPrimitive.TYPE_NOT_EXPRESSION:{
				return negationToSql(runtime, context, expression, definition);
			}
		}
		throw new TMQLRuntimeException("Unsupported expression type for SQL translator.");
	}
	
	/**
	 * Transform the given boolean expression by using the given SQL definition
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the current context
	 * @param expression
	 *            the expression to transform
	 * @param definition
	 *            the current SQL definition
	 * @return the new SQL definition
	 * @throws TMQLRuntimeException
	 *             thrown if anything fails
	 */
	public ISqlDefinition booleanToSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		throw new TMQLRuntimeException("Unsupported expression type for SQL translator.");
	}
	
	/**
	 * Transform the given EXISTS clause by using the given SQL definition
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the current context
	 * @param expression
	 *            the expression to transform
	 * @param definition
	 *            the current SQL definition
	 * @return the new SQL definition
	 * @throws TMQLRuntimeException
	 *             thrown if anything fails
	 */
	public ISqlDefinition existsToSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		return TranslatorRegistry.getTranslator(ExistsClause.class).toSql(runtime, context, expression.getExpressions().get(0), definition);
	}
	
	/**
	 * Transform the given FORALL by using the given SQL definition
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the current context
	 * @param expression
	 *            the expression to transform
	 * @param definition
	 *            the current SQL definition
	 * @return the new SQL definition
	 * @throws TMQLRuntimeException
	 *             thrown if anything fails
	 */
	public ISqlDefinition everyToSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		throw new TMQLRuntimeException("Unsupported expression type for SQL translator.");
	}	
	
	/**
	 * Transform the given negation by using the given SQL definition
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the current context
	 * @param expression
	 *            the expression to transform
	 * @param definition
	 *            the current SQL definition
	 * @return the new SQL definition
	 * @throws TMQLRuntimeException
	 *             thrown if anything fails
	 */
	public ISqlDefinition negationToSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		throw new TMQLRuntimeException("Unsupported expression type for SQL translator.");
	}	


}
