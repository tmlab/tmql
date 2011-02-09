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
import de.topicmapslab.tmql4j.path.grammar.productions.BooleanExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.BooleanPrimitive;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.SqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.where.Intersect;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.where.Union;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TmqlSqlTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TranslatorRegistry;

/**
 * @author Sven Krosse
 * 
 */
public class BooleanExpressionTranslator extends TmqlSqlTranslatorImpl<BooleanExpression> {

	/**
	 * {@inheritDoc}
	 */
	public ISqlDefinition toSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		switch (expression.getGrammarType()) {
			case BooleanExpression.TYPE_BOOLEAN_PRIMITIVE: {
				return primitveToSql(runtime, context, expression, definition);
			}
			case BooleanExpression.TYPE_CONJUNCTION: {
				return conjunctionToSql(runtime, context, expression, definition);
			}
			case BooleanExpression.TYPE_DISJUNCTION: {
				return disjunctionToSql(runtime, context, expression, definition);
			}
			default: {
				return everyToSql(runtime, context, expression, definition);
			}
		}
	}

	/**
	 * Transform the given conjunction by using the given SQL definition
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
	public ISqlDefinition conjunctionToSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		/*
		 * get last selection
		 */
		ISelection selection = definition.getLastSelection();
		/*
		 * copy SQL definition
		 */
		ISqlDefinition def = new SqlDefinition();
		def.addSelection(selection);
		def.setInternalAliasIndex(definition.getInternalAliasIndex());		
		/*
		 * create new SQL intersect
		 */
		Intersect intersect = null;
		for (IExpression ex : expression.getExpressions()) {
			ISqlDefinition newDefinition = toSql(runtime, context, ex, def);
			newDefinition.clearSelection();
			newDefinition.addSelection(selection);
			if (intersect == null) {
				intersect = new Intersect((SqlDefinition) newDefinition);
			} else {
				intersect.intersect(newDefinition);
			}
		}
		return intersect;
	}

	/**
	 * Transform the given disjunction by using the given SQL definition
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
	public ISqlDefinition disjunctionToSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		/*
		 * get last selection
		 */
		ISelection selection = definition.getLastSelection();
		/*
		 * copy SQL definition
		 */
		ISqlDefinition def = new SqlDefinition();
		def.addSelection(selection);
		def.setInternalAliasIndex(definition.getInternalAliasIndex());
		/*
		 * create new SQL intersect
		 */
		Union union = null;
		for (IExpression ex : expression.getExpressions()) {
			ISqlDefinition newDefinition = toSql(runtime, context, ex, def);
			newDefinition.clearSelection();
			newDefinition.addSelection(selection);
			if (union == null) {
				union = new Union((SqlDefinition) newDefinition);
			} else {
				union.union(newDefinition);
			}
		}
		return union;
	}

	/**
	 * Transform the given boolean primitive by using the given SQL definition
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
	public ISqlDefinition primitveToSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		return TranslatorRegistry.getTranslator(BooleanPrimitive.class).toSql(runtime, context, expression.getExpressions().get(0), definition);
	}

	/**
	 * Transform the given FORALL clause by using the given SQL definition
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

}
