/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl;

import java.util.ArrayList;
import java.util.List;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.BooleanExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.BooleanPrimitive;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.SqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.TmqlSqlTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.TranslatorRegistry;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants;
import de.topicmapslab.tmql4j.sql.path.utils.TranslatorUtils;

/**
 * @author Sven Krosse
 * 
 */
public class BooleanExpressionTranslator extends TmqlSqlTranslatorImpl<BooleanExpression> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISqlDefinition toSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		switch (expression.getGrammarType()) {
			case BooleanExpression.TYPE_BOOLEAN_PRIMITIVE: {
				return primitveToSql(runtime, context, expression, definition);
			}
			case BooleanExpression.TYPE_CONJUNCTION: {
				return booleanOperationToSql(runtime, context, expression, definition, ISqlConstants.ISqlOperators.INTERSECT);
			}
			case BooleanExpression.TYPE_DISJUNCTION: {
				return booleanOperationToSql(runtime, context, expression, definition, ISqlConstants.ISqlOperators.UNION);
			}
			default: {
				return everyToSql(runtime, context, expression, definition);
			}
		}
	}

	/**
	 * Transform the given boolean operation by using the given SQL definition
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the current context
	 * @param expression
	 *            the expression to transform
	 * @param definition
	 *            the current SQL definition
	 * @param operator
	 *            the SQL operator
	 * @return the new SQL definition
	 * @throws TMQLRuntimeException
	 *             thrown if anything fails
	 */
	public ISqlDefinition booleanOperationToSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition, String operator) throws TMQLRuntimeException {
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
		List<ISqlDefinition> intersects = new ArrayList<ISqlDefinition>();
		for (IExpression ex : expression.getExpressions()) {
			ISqlDefinition intersect = toSql(runtime, context, ex, def);
			intersects.add(intersect);
		}
		ISqlDefinition intersection = new SqlDefinition();
		String resultAlias = definition.getAlias();
		IFromPart fromPart = TranslatorUtils.asSetOperation(intersects, intersection.getAlias(), resultAlias, operator, false);
		intersection.addFromPart(fromPart);
		Selection selectionPart = new Selection(resultAlias, null, true);
		intersection.addSelection(selectionPart);
		selectionPart.setCurrentTable(intersects.get(0).getSelectionParts().get(0).getCurrentTable());
		return intersection;
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
