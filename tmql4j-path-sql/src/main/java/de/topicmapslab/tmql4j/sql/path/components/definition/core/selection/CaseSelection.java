/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.definition.core.selection;

import de.topicmapslab.tmql4j.path.grammar.lexical.And;
import de.topicmapslab.tmql4j.path.grammar.lexical.As;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketSquareClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketSquareOpen;
import de.topicmapslab.tmql4j.path.grammar.lexical.Colon;
import de.topicmapslab.tmql4j.path.grammar.lexical.Comma;
import de.topicmapslab.tmql4j.path.grammar.lexical.GreaterThan;
import de.topicmapslab.tmql4j.path.grammar.lexical.In;
import de.topicmapslab.tmql4j.path.grammar.lexical.Not;
import de.topicmapslab.tmql4j.path.grammar.lexical.Null;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants;

/**
 * A selection entry for Case Selection
 * 
 * @author Sven Krosse
 * 
 */
public class CaseSelection extends Selection {

	private final ISqlDefinition thenSql;
	private ISqlDefinition elseSql;
	private ISqlDefinition condition;
	private final boolean isConditional;

	/**
	 * @param selection
	 */
	public CaseSelection(ISqlDefinition thenSql, String alias) {
		super("", alias);
		this.thenSql = thenSql;
		this.isConditional = false;
	}

	/**
	 * @param selection
	 */
	public CaseSelection(ISqlDefinition condition, ISqlDefinition thenSql, ISqlDefinition elseSql, String alias) {
		super("", alias);
		this.thenSql = thenSql;
		this.isConditional = true;
		this.condition = condition;
		this.elseSql = elseSql;
	}

	/**
	 * Creates an empty else statement depend from the current selection values
	 * 
	 * @return the empty else
	 */
	private String getEmptyElse() {
		/*
		 * get default value and array type by selected values
		 */
		SqlTables table = thenSql.getLastSelection().getCurrentTable();
		String arrayType;
		String arrayValue;
		if (table == SqlTables.STRING) {
			arrayValue = ISqlConstants.SINGLEQUOTE + ISqlConstants.IS_NULL_VALUE_IN_SQL + ISqlConstants.SINGLEQUOTE;
			arrayType = ISqlConstants.ISqlTypes.VARCHAR;
		} else if (table == SqlTables.BOOLEAN) {
			arrayValue = Boolean.toString(false);
			arrayType = ISqlConstants.ISqlTypes.BOOLEAN;
		} else if (table.isRecord()) {
			arrayValue = "";
			arrayType = ISqlConstants.ISqlTypes.RECORD;
		} else {
			arrayValue = Integer.toString(-1);
			arrayType = ISqlConstants.ISqlTypes.BIGINT;
		}
		/*
		 * build string part
		 */
		StringBuilder builder = new StringBuilder();
		builder.append(ISqlConstants.ISqlKeywords.ARRAY);
		builder.append(BracketSquareOpen.TOKEN);
		builder.append(arrayValue);
		builder.append(BracketSquareClose.TOKEN);
		builder.append(Colon.TOKEN);
		builder.append(Colon.TOKEN);
		builder.append(arrayType);
		builder.append(BracketSquareOpen.TOKEN);
		builder.append(BracketSquareClose.TOKEN);
		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		if (isConditional) {
			return conditionalToString();
		}
		return nonConditionalToString();
	}

	/**
	 * Creates the string representation for a case selection with condition and
	 * else part
	 * 
	 * @return the string
	 */
	private String conditionalToString() {
		/*
		 * get default array value and type dependent from current table
		 */

		StringBuilder builder = new StringBuilder();
		/*
		 * create case part
		 */
		builder.append(ISqlConstants.ISqlFunctions.UNNEST);
		builder.append(BracketRoundOpen.TOKEN);
		builder.append(ISqlConstants.ISqlKeywords.CASE);
		builder.append(ISqlConstants.WHITESPACE);
		builder.append(ISqlConstants.ISqlKeywords.WHEN);
		builder.append(ISqlConstants.WHITESPACE);
		if (condition != null) {
			builder.append(Boolean.toString(true));
			builder.append(ISqlConstants.WHITESPACE);
			builder.append(In.TOKEN);
			builder.append(ISqlConstants.WHITESPACE);
			builder.append(BracketRoundOpen.TOKEN);
			builder.append(condition.toString());
			builder.append(BracketRoundClose.TOKEN);
		} else {
			builder.append(getAsArrayUpperCondition(thenSql.toString()));
		}
		builder.append(ISqlConstants.WHITESPACE);
		/*
		 * create THEN part
		 */
		builder.append(ISqlConstants.ISqlKeywords.THEN);
		builder.append(ISqlConstants.WHITESPACE);
		builder.append(ISqlConstants.ISqlKeywords.ARRAY);
		builder.append(BracketRoundOpen.TOKEN);
		builder.append(thenSql.toString());
		builder.append(BracketRoundClose.TOKEN);
		builder.append(ISqlConstants.WHITESPACE);
		/*
		 * create ELSE part
		 */
		builder.append(ISqlConstants.ISqlKeywords.ELSE);
		builder.append(ISqlConstants.WHITESPACE);
		if (elseSql == null) {
			builder.append(getEmptyElse());
		} else {
			builder.append(ISqlConstants.ISqlKeywords.ARRAY);
			builder.append(BracketRoundOpen.TOKEN);
			builder.append(elseSql.toString());
			builder.append(BracketRoundClose.TOKEN);
			builder.append(ISqlConstants.WHITESPACE);
		}
		/*
		 * finalize case
		 */
		builder.append(ISqlConstants.ISqlKeywords.END);
		builder.append(BracketRoundClose.TOKEN);
		builder.append(As.TOKEN);
		builder.append(ISqlConstants.WHITESPACE);
		builder.append(getAlias());
		return builder.toString();
	}

	/**
	 * Returns the content as array not empty condition
	 * 
	 * @param content
	 *            the array content
	 * @return the condition
	 */
	private String getAsArrayUpperCondition(String content) {
		StringBuilder array = new StringBuilder();
		array.append(ISqlConstants.ISqlFunctions.ARRAY_UPPER);
		array.append(BracketRoundOpen.TOKEN);
		array.append(ISqlConstants.ISqlKeywords.ARRAY);
		array.append(BracketRoundOpen.TOKEN);
		array.append(BracketRoundOpen.TOKEN);
		array.append(content);
		array.append(BracketRoundClose.TOKEN);
		array.append(BracketRoundClose.TOKEN);
		array.append(Comma.TOKEN);
		array.append(Integer.toString(1));
		array.append(BracketRoundClose.TOKEN);
		array.append(GreaterThan.TOKEN);
		array.append(Integer.toString(0));
		array.append(ISqlConstants.WHITESPACE);
		array.append(And.TOKEN);
		array.append(ISqlConstants.WHITESPACE);
		array.append(ISqlConstants.ISqlKeywords.ARRAY);
		array.append(BracketRoundOpen.TOKEN);
		array.append(BracketRoundOpen.TOKEN);
		array.append(content);
		array.append(BracketRoundClose.TOKEN);
		array.append(BracketRoundClose.TOKEN);
		array.append(ISqlConstants.ISqlKeywords.IS);
		array.append(ISqlConstants.WHITESPACE);
		array.append(Not.TOKEN);
		array.append(ISqlConstants.WHITESPACE);
		array.append(Null.TOKEN);
		return array.toString();
	}

	/**
	 * Creates the string representation for a case selection without any
	 * condition and else part
	 * 
	 * @return the string
	 */
	private String nonConditionalToString() {
		/*
		 * get default array value and type dependent from current table
		 */

		StringBuilder builder = new StringBuilder();
		/*
		 * create case part
		 */
		builder.append(ISqlConstants.ISqlFunctions.UNNEST);
		builder.append(BracketRoundOpen.TOKEN);
		builder.append(ISqlConstants.ISqlKeywords.CASE);
		builder.append(ISqlConstants.WHITESPACE);
		builder.append(ISqlConstants.ISqlKeywords.WHEN);
		builder.append(ISqlConstants.WHITESPACE);
		builder.append(getAsArrayUpperCondition(thenSql.toString()));
		builder.append(ISqlConstants.WHITESPACE);

		/*
		 * create THEN part
		 */
		builder.append(ISqlConstants.ISqlKeywords.THEN);
		builder.append(ISqlConstants.WHITESPACE);
		builder.append(ISqlConstants.ISqlKeywords.ARRAY);
		builder.append(BracketRoundOpen.TOKEN);
		builder.append(thenSql.toString());
		builder.append(BracketRoundClose.TOKEN);
		builder.append(ISqlConstants.WHITESPACE);
		/*
		 * create default array part as ELSE part
		 */
		builder.append(ISqlConstants.ISqlKeywords.ELSE);
		builder.append(ISqlConstants.WHITESPACE);
		builder.append(getEmptyElse());
		/*
		 * finalize case
		 */
		builder.append(ISqlConstants.ISqlKeywords.END);
		builder.append(BracketRoundClose.TOKEN);
		builder.append(As.TOKEN);
		builder.append(ISqlConstants.WHITESPACE);
		builder.append(getAlias());
		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof CaseSelection) {
			return ((CaseSelection) arg0).thenSql.equals(thenSql);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return this.thenSql.hashCode();
	}

}
