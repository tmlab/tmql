/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl;

import java.text.MessageFormat;
import java.util.Map;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.components.parser.ParserUtils;
import de.topicmapslab.tmql4j.path.grammar.lexical.Asc;
import de.topicmapslab.tmql4j.path.grammar.lexical.Equality;
import de.topicmapslab.tmql4j.path.grammar.lexical.GreaterEquals;
import de.topicmapslab.tmql4j.path.grammar.lexical.GreaterThan;
import de.topicmapslab.tmql4j.path.grammar.lexical.LowerEquals;
import de.topicmapslab.tmql4j.path.grammar.lexical.LowerThan;
import de.topicmapslab.tmql4j.path.grammar.lexical.Minus;
import de.topicmapslab.tmql4j.path.grammar.lexical.Modulo;
import de.topicmapslab.tmql4j.path.grammar.lexical.Percent;
import de.topicmapslab.tmql4j.path.grammar.lexical.Plus;
import de.topicmapslab.tmql4j.path.grammar.lexical.RegularExpression;
import de.topicmapslab.tmql4j.path.grammar.lexical.ShortcutAxisAtomifyMoveForward;
import de.topicmapslab.tmql4j.path.grammar.lexical.ShortcutAxisLocators;
import de.topicmapslab.tmql4j.path.grammar.lexical.Star;
import de.topicmapslab.tmql4j.path.grammar.lexical.Unequals;
import de.topicmapslab.tmql4j.path.grammar.productions.Content;
import de.topicmapslab.tmql4j.path.grammar.productions.ValueExpression;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.from.FromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.orderBy.OrderBy;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.ISqlTranslator;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TmqlSqlTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TranslatorRegistry;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class ValueExpressionTranslator extends TmqlSqlTranslatorImpl<ValueExpression> {

	private static final String INFIX = "({0}) {1} ({2})";
	
	private static final String CONCAT = "||";
	
	private static Map<Class<? extends IToken>, String> operators = HashUtil.getHashMap();
	static{
		operators.put(Plus.class, Plus.TOKEN);
		operators.put(Minus.class, Minus.TOKEN);
		operators.put(Star.class, Star.TOKEN);
		operators.put(RegularExpression.class, "~");
		operators.put(Modulo.class, Percent.TOKEN);
		operators.put(Percent.class, ShortcutAxisAtomifyMoveForward.TOKEN);
		operators.put(Equality.class, ShortcutAxisLocators.TOKEN);
		operators.put(Unequals.class, Unequals.TOKEN);
		operators.put(GreaterThan.class, GreaterThan.TOKEN);
		operators.put(GreaterEquals.class, GreaterEquals.TOKEN);
		operators.put(LowerThan.class, LowerThan.TOKEN);
		operators.put(LowerEquals.class, LowerEquals.TOKEN);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ISqlDefinition toSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		switch (expression.getGrammarType()) {
			case ValueExpression.TYPE_CONTENT: {
				return contentToSql(runtime, context, expression, definition);
			}
			case ValueExpression.TYPE_INFIX_OPERATOR: {
				return infixToSql(runtime, context, (ValueExpression) expression, definition);
			}
			case ValueExpression.TYPE_PREFIX_OPERATOR: {
				return prefixToSql(runtime, context, expression, definition);
			}
		}
		throw new TMQLRuntimeException("Unsupported expression type for SQL translator.");
	}

	/**
	 * Translates the content expression contained by this value expression to
	 * its corresponding SQL part
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param expression
	 *            the expression
	 * @param definition
	 *            the definition
	 * @return the translated SQL definition
	 * @throws TMQLRuntimeException
	 *             thrown if anything fails
	 */
	@SuppressWarnings("unchecked")
	private ISqlDefinition contentToSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		ISqlDefinition def = TranslatorRegistry.getTranslator(Content.class).toSql(runtime, context, expression.getExpressions().get(0), definition);
		if (((ValueExpression) expression).isAscOrDescOrdering()) {
			OrderBy orderBy = new OrderBy(def.getLastSelection().getSelection(), ParserUtils.containsTokens(expression.getTmqlTokens(), Asc.class));
			def.addOrderByPart(orderBy);
		}
		return def;
	}

	/**
	 * Translates the infix expression contained by this value expression to its
	 * corresponding SQL part
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param expression
	 *            the expression
	 * @param definition
	 *            the definition
	 * @return the translated SQL definition
	 * @throws TMQLRuntimeException
	 *             thrown if anything fails
	 */
	private ISqlDefinition infixToSql(ITMQLRuntime runtime, IContext context, ValueExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		ISqlTranslator<?> translator = TranslatorRegistry.getTranslator(ValueExpression.class);
		/*
		 * translate left hand operation
		 */
		ISqlDefinition leftHandDef = definition.clone();
		ValueExpression leftHand = (ValueExpression) expression.getExpressions().get(0);
		leftHandDef = translator.toSql(runtime, context, leftHand, leftHandDef);
		IFromPart leftHandFromPart = new FromPart(leftHandDef.toString(), definition.getAlias(), false);
		/*
		 * translate right hand operation
		 */
		ISqlDefinition rightHandDef = definition.clone();
		ValueExpression rightHand = (ValueExpression) expression.getExpressions().get(1);
		rightHandDef = translator.toSql(runtime, context, rightHand, rightHandDef);
		IFromPart rightHandFromPart = new FromPart(rightHandDef.toString(), definition.getAlias(), false);
		/*
		 * create result
		 */
		ISqlDefinition concat = definition.clone();
		concat.addFromPart(leftHandFromPart);
		concat.addFromPart(rightHandFromPart);

		SqlTables table = getResultTable(leftHandDef.getLastSelection().getCurrentTable(), rightHandDef.getLastSelection().getCurrentTable());
			
		Class<? extends IToken> op = expression.getTmqlTokens().get(expression.getIndexOfOperator());
		String operator = operators.get(op);
		if ( Plus.class.equals(op)& table == SqlTables.STRING ){
			operator = CONCAT;			
		}
		/*
		 * modify to boolean
		 */
		if ( GreaterThan.class.equals(op) || GreaterEquals.class.equals(op) || Equality.class.equals(op) || LowerEquals.class.equals(op) || LowerThan.class.equals(op) || RegularExpression.class.equals(op) || Unequals.class.equals(op)){
			table = SqlTables.BOOLEAN;
		}
		
		String content = MessageFormat.format(INFIX, leftHandDef.getLastSelection().getColumn(), operator, rightHandDef.getLastSelection().getColumn());		
		ISelection selection = new Selection(content, definition.getAlias(), false);
		concat.clearSelection();
		concat.addSelection(selection);
		
		ISqlDefinition result = definition.clone();
		IFromPart part = new FromPart(concat.toString(), result.getAlias(), false);
		result.addFromPart(part);
		ISelection sel = new Selection(selection.getAlias(), part.getAlias());
		result.clearSelection();
		result.addSelection(sel);		
		sel.setCurrentTable(table);
		
		return result;
	}
	
	private SqlTables getResultTable(SqlTables left, SqlTables right){
		if ( left == SqlTables.STRING || right == SqlTables.STRING){
			return SqlTables.STRING;
		}
		if ( left == SqlTables.DATETIME || right == SqlTables.DATETIME){
			return SqlTables.DATETIME;
		}
		if ( left == SqlTables.DECIMAL || right == SqlTables.DECIMAL){
			return SqlTables.DECIMAL;
		}
		if ( left == SqlTables.INTEGER || right == SqlTables.INTEGER){
			return SqlTables.INTEGER;
		}
		return left;
	}

	/**
	 * Translates the prefix expression contained by this value expression to
	 * its corresponding SQL part
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param expression
	 *            the expression
	 * @param definition
	 *            the definition
	 * @return the translated SQL definition
	 * @throws TMQLRuntimeException
	 *             thrown if anything fails
	 */
	private ISqlDefinition prefixToSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		return null;
	}

}
