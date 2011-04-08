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
import java.util.Map;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.grammar.lexical.Intersect;
import de.topicmapslab.tmql4j.path.grammar.lexical.Substraction;
import de.topicmapslab.tmql4j.path.grammar.lexical.Union;
import de.topicmapslab.tmql4j.path.grammar.productions.Content;
import de.topicmapslab.tmql4j.path.grammar.productions.PathExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.QueryExpression;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.SqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.CaseSelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.ISqlTranslator;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.TmqlSqlTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.TranslatorRegistry;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants.ISqlOperators;
import de.topicmapslab.tmql4j.sql.path.utils.TranslatorUtils;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class ContentTranslator extends TmqlSqlTranslatorImpl<Content> {

	private static final Map<Class<? extends IToken>, String> operators = HashUtil.getHashMap();
	static {
		operators.put(Union.class, ISqlOperators.UNION);
		operators.put(Intersect.class, ISqlOperators.INTERSECT);
		operators.put(Substraction.class, ISqlOperators.EXCEPT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISqlDefinition toSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		if (expression.getGrammarType() == Content.TYPE_QUERY_EXPRESSION) {
			return TranslatorRegistry.getTranslator(QueryExpression.class).toSql(runtime, context, expression.getExpressions().get(0), definition);
		} else if (expression.getGrammarType() == Content.TYPE_SET_OPERATION) {
			return setOperationToSql(runtime, context, expression, definition);
		} else if (expression.getGrammarType() == Content.TYPE_NONCANONICAL_CONDITIONAL_EXPRESSION || expression.getGrammarType() == Content.TYPE_CONDITIONAL_EXPRESSION) {
			return conditionalToSql(runtime, context, expression, definition);
		}
		throw new TMQLRuntimeException("Unsupported expression type for SQL translator.");
	}

	/**
	 * Method to translate a TMQL conditional operation to a SQL set operation
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param expression
	 *            the expression containing the set operation
	 * @param definition
	 *            the definition
	 * @return the SQL definition
	 * @throws TMQLRuntimeException
	 *             thrown if anything fails
	 */
	public ISqlDefinition conditionalToSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		ISqlDefinition condition = null, thenSql = null, elseSql = null;
		ISqlTranslator<?> translator = TranslatorRegistry.getTranslator(PathExpression.class);
		if (expression.getGrammarType() == Content.TYPE_NONCANONICAL_CONDITIONAL_EXPRESSION) {
			thenSql = translator.toSql(runtime, context, expression.getExpressions().get(0), definition);
			elseSql = translator.toSql(runtime, context, expression.getExpressions().get(1), definition);
		} else {
			condition = translator.toSql(runtime, context, expression.getExpressions().get(0), definition);
			thenSql = toSql(runtime, context, expression.getExpressions().get(1), definition);
			if (expression.getExpressions().size() == 3) {
				elseSql = toSql(runtime, context, expression.getExpressions().get(2), definition);
			}
		}
		ISqlDefinition def = new SqlDefinition();
		CaseSelection caseSelection = new CaseSelection(condition, thenSql, elseSql, definition.getAlias());
		caseSelection.setCurrentTable(thenSql.getLastSelection().getCurrentTable());
		def.addSelection(caseSelection);
		return def;
	}

	/**
	 * Method to translate a TMQL set operation to a SQL set operation
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param expression
	 *            the expression containing the set operation
	 * @param definition
	 *            the definition
	 * @return the SQL definition
	 * @throws TMQLRuntimeException
	 *             thrown if anything fails
	 */
	public ISqlDefinition setOperationToSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		Content c = (Content) expression;
		Class<? extends IToken> operator = c.getTmqlTokens().get(c.getIndexOfOperator());

		List<ISqlDefinition> definitions = new ArrayList<ISqlDefinition>();
		for (IExpression ex : expression.getExpressions()) {
			definitions.add(toSql(runtime, context, ex, definition));
		}
		IFromPart fromPart = TranslatorUtils.asSetOperation(definitions, definition.getAlias(), null, operators.get(operator), false);

		ISqlDefinition set = new SqlDefinition();
		set.addFromPart(fromPart);
		ISelection selection = new Selection(ISqlConstants.ANY, null);
		selection.setCurrentTable(definitions.get(0).getLastSelection().getCurrentTable());
		set.addSelection(selection);

		return set;
	}

}
