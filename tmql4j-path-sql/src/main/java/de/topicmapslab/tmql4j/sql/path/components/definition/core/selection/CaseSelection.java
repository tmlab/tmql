/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.definition.core.selection;

import de.topicmapslab.tmql4j.path.grammar.lexical.As;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketSquareClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketSquareOpen;
import de.topicmapslab.tmql4j.path.grammar.lexical.Colon;
import de.topicmapslab.tmql4j.path.grammar.lexical.Comma;
import de.topicmapslab.tmql4j.path.grammar.lexical.GreaterThan;
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

	private ISqlDefinition innerDef;

	/**
	 * @param selection
	 */
	public CaseSelection(ISqlDefinition innerDef, String alias) {
		super("", alias);
		this.innerDef = innerDef;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		SqlTables table = innerDef.getLastSelection().getCurrentTable();
		/*
		 * get default array value and type dependend from current table 
		 */
		String arrayType;
		String arrayValue;
		if (table == SqlTables.STRING) {
			arrayValue = ISqlConstants.SINGLEQUOTE + ISqlConstants.IS_NULL_VALUE_IN_SQL + ISqlConstants.SINGLEQUOTE;
			arrayType = ISqlConstants.ISqlTypes.VARCHAR;
		} else if (table == SqlTables.BOOLEAN) {
			arrayValue = Boolean.toString(false);
			arrayType = ISqlConstants.ISqlTypes.BOOLEAN;
		} else {
			arrayValue = Integer.toString(-1);
			arrayType = ISqlConstants.ISqlTypes.BIGINT;
		}		
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
		builder.append(ISqlConstants.ISqlFunctions.ARRAY_UPPER);
		builder.append(BracketRoundOpen.TOKEN);
		builder.append(ISqlConstants.ISqlKeywords.ARRAY);
		builder.append(BracketRoundOpen.TOKEN);
		builder.append(BracketRoundOpen.TOKEN);
		final String innerPart =innerDef.toString(); 
		builder.append(innerPart);
		builder.append(BracketRoundClose.TOKEN);
		builder.append(BracketRoundClose.TOKEN);
		builder.append(Comma.TOKEN);
		builder.append(Integer.toString(1));
		builder.append(BracketRoundClose.TOKEN);
		builder.append(GreaterThan.TOKEN);
		builder.append(Integer.toString(0));
		builder.append(ISqlConstants.WHITESPACE);
		/*
		 * create THEN part
		 */
		builder.append(ISqlConstants.ISqlKeywords.THEN);
		builder.append(ISqlConstants.WHITESPACE);
		builder.append(ISqlConstants.ISqlKeywords.ARRAY);
		builder.append(BracketRoundOpen.TOKEN);
		builder.append(innerPart);
		builder.append(BracketRoundClose.TOKEN);
		builder.append(ISqlConstants.WHITESPACE);		
		/*
		 * create default array part as ELSE part
		 */
		builder.append(ISqlConstants.ISqlKeywords.ELSE);
		builder.append(ISqlConstants.WHITESPACE);	
		builder.append(ISqlConstants.ISqlKeywords.ARRAY);
		builder.append(BracketSquareOpen.TOKEN);
		builder.append(arrayValue);
		builder.append(BracketSquareClose.TOKEN);
		builder.append(Colon.TOKEN);
		builder.append(Colon.TOKEN);
		builder.append(arrayType);
		builder.append(BracketSquareOpen.TOKEN);
		builder.append(BracketSquareClose.TOKEN);
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
	public boolean equals(Object arg0) {
		if (arg0 instanceof CaseSelection) {
			return ((CaseSelection) arg0).innerDef.equals(innerDef);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return this.innerDef.hashCode();
	}

}
