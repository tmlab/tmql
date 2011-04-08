/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl;

import java.text.MessageFormat;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.prepared.IPreparedStatement;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.grammar.productions.PreparedExpression;
import de.topicmapslab.tmql4j.path.grammar.lexical.Scope;
import de.topicmapslab.tmql4j.path.grammar.lexical.ShortcutAxisInstances;
import de.topicmapslab.tmql4j.path.grammar.productions.Anchor;
import de.topicmapslab.tmql4j.path.grammar.productions.BooleanExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.FilterPostfix;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.SqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.CountSelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.where.InCriterion;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.TmqlSqlTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.TranslatorRegistry;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * @author Sven Krosse
 * 
 */
public class FilterPostfixTranslator extends TmqlSqlTranslatorImpl<FilterPostfix> {

	/**
	 * SQL query for type-filters
	 */
	private static final String SQL_TYPE_FILTER = " {0} IN ( SELECT t.id FROM ( SELECT id_topic FROM locators AS l, rel_subject_identifiers, topics AS t WHERE t.id = id_topic AND l.id = id_locator AND reference = ''{1}'' AND id_topicmap = {2}) AS type, ( SELECT id_type, id_instance AS id FROM rel_instance_of UNION SELECT id_type, id FROM typeables ) AS t WHERE t.id_type = type.id_topic )";
	/**
	 * SQL query for scope filters
	 */
	private static final String SQL_SCOPE_FILTER = " {0} IN ( SELECT s.id FROM scopeables AS s, rel_themes AS rt, locators AS l, rel_subject_identifiers AS r WHERE l.id = r.id_locator AND l.reference = ''{1}'' AND rt.id_theme = id_topic AND s.id_scope = rt.id_scope )";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISqlDefinition toSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		/*
		 * switch by grammar type
		 */
		switch (expression.getGrammarType()) {
			/*
			 * index filters
			 */
			case FilterPostfix.TYPE_BOUNDS_FILTER:
			case FilterPostfix.TYPE_SHORTCUT_BOUNDS_FILTER: {
				return rangeFilterToSql(runtime, context, expression, definition);
			}
			case FilterPostfix.TYPE_INDEX_FILTER:
			case FilterPostfix.TYPE_SHORTCUT_INDEX_FILTER: {
				return indexFilterToSql(runtime, context, expression, definition);
			}
				/*
				 * scope filters
				 */
			case FilterPostfix.TYPE_SCOPE_FILTER:
			case FilterPostfix.TYPE_SHORTCUT_SCOPE_FILTER: {
				return scopeFilterToSql(runtime, context, expression, definition);
			}
				/*
				 * type filters
				 */
			case FilterPostfix.TYPE_TYPE_FILTER:
			case FilterPostfix.TYPE_SHORTCUT_TYPE_FILTER: {
				return typeFilterToSql(runtime, context, expression, definition);
			}
				/*
				 * boolean filters
				 */
			default: {
				return booleanFilterToSql(runtime, context, expression, definition);
			}
		}
	}

	/**
	 * Transform the given type filter by using the given SQL definition
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
	public ISqlDefinition typeFilterToSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		/*
		 * get current selection
		 */
		ISelection selection = definition.getLastSelection();
		/*
		 * get type reference
		 */
		int index = 2;
		/*
		 * is shortcut type filter
		 */
		if (expression.getTmqlTokens().get(0).equals(ShortcutAxisInstances.class)) {
			index = 1;
		}
		final String typeRef = runtime.getConstructResolver().toAbsoluteIRI(context, expression.getTokens().get(index));
		/*
		 * generate condition
		 */
		final String condition = MessageFormat.format(SQL_TYPE_FILTER, selection.getSelection(), typeRef, context.getQuery().getTopicMap().getId());
		/*
		 * update SQL definition
		 */
		definition.add(condition);
		return definition;
	}

	/**
	 * Transform the given scope filter by using the given SQL definition
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
	public ISqlDefinition scopeFilterToSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		/*
		 * get current selection
		 */
		ISelection selection = definition.getLastSelection();
		/*
		 * get type reference
		 */
		int index = 2;
		/*
		 * is shortcut scope filter
		 */
		if (expression.getTmqlTokens().get(0).equals(Scope.class)) {
			index = 1;
		}
		final String themeRef = runtime.getConstructResolver().toAbsoluteIRI(context, expression.getTokens().get(index));
		/*
		 * generate condition
		 */
		final String condition = MessageFormat.format(SQL_SCOPE_FILTER, selection.getSelection(), themeRef);
		/*
		 * update SQL definition
		 */
		definition.add(condition);
		return definition;
	}

	/**
	 * Transform the given index range filter by using the given SQL definition
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
	public ISqlDefinition rangeFilterToSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		int offset = getNumericValue(runtime, context, expression.getExpressions().get(0));
		if (offset < 0) {
			offset = 0;
		}
		definition.setOffset(offset);
		int limit = getNumericValue(runtime, context, expression.getExpressions().get(1));
		limit -= offset;
		if (limit < 0) {
			limit = 0;
		}
		definition.setLimit(limit);
		return definition;
	}

	/**
	 * Returns the numerical value extracted from the given TMQL Query
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param expression
	 *            the expression ( {@link Anchor} )
	 * @return the numeric value or <code>-1</code>
	 */
	private int getNumericValue(ITMQLRuntime runtime, IContext context, IExpression expression) {
		if (expression.contains(PreparedExpression.class)) {
			Object value = ((IPreparedStatement) context.getQuery()).get(expression.getExpressions().get(0));
			return LiteralUtils.asInteger(value);
		}
		return LiteralUtils.asInteger(expression.getTokens().get(0)).intValue();
	}

	/**
	 * Transform the given index filter by using the given SQL definition
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
	public ISqlDefinition indexFilterToSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		int offset = getNumericValue(runtime, context, expression.getExpressions().get(0));
		if (offset < 0) {
			offset = Integer.MAX_VALUE;
		}
		definition.setOffset(offset);
		return definition;
	}

	/**
	 * Transform the given boolean filter by using the given SQL definition
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
	public ISqlDefinition booleanFilterToSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		/*
		 * filter definition
		 */
		ISqlDefinition innerDefinition = definition.clone();
		innerDefinition.clearFromParts();
		/*
		 * get current selection
		 */
		ISelection selection = definition.getLastSelection();
		/*
		 * call boolean-expression translator
		 */
		ISqlDefinition newDefinition = TranslatorRegistry.getTranslator(BooleanExpression.class).toSql(runtime, context, expression.getExpressions().get(0), innerDefinition);

		InCriterion in;
		if (newDefinition.getLastSelection().getCurrentTable() == SqlTables.BOOLEAN) {
			in = new InCriterion(Boolean.toString(true), newDefinition);
		} else if (newDefinition.getLastSelection().getCurrentTable() == SqlTables.STRING) {
			ISqlDefinition cnt = new SqlDefinition();
			cnt.addFromPart(newDefinition.toString(), definition.getAlias(), false);
			cnt.addSelection(new CountSelection(new Selection(ISqlConstants.ANY, null), null));
			in = new InCriterion(Integer.toString(0), cnt);
			in.negate(true);
		} else {
			in = new InCriterion(selection.getSelection(), newDefinition);
		}
		/*
		 * update SQL definition
		 */
		definition.add(in);
		return definition;
	}

}
