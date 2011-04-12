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
import de.topicmapslab.tmql4j.path.grammar.lexical.Dot;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants;

/**
 * @author Sven Krosse
 * 
 */
public class Selection implements ISelection {

	private final String column;
	private String alias;
	private String cast;
	private boolean isColumn = true;
	private SqlTables sqlTable;

	/**
	 * constructor
	 * 
	 * @param selection
	 *            the selection with alias
	 */
	public Selection(final String selection) {
		int index = selection.indexOf(Dot.TOKEN);
		if (index != -1) {
			alias = selection.substring(0, index);
			column = selection.substring(index + 1);
		} else {
			alias = null;
			column = selection;
		}
	}

	/**
	 * constructor
	 * 
	 * @param column
	 *            the column
	 * @param alias
	 *            the alias
	 */
	public Selection(final String column, final String alias) {
		this.alias = alias;
		this.column = column;
	}

	/**
	 * constructor
	 * 
	 * @param content
	 *            the content
	 * @param alias
	 *            the alias
	 * @param isColumn
	 *            flag indicates if the value is a column or complex content
	 */
	public Selection(final String content, final String alias, final boolean isColumn) {
		this.alias = alias;
		this.column = content;
		this.isColumn = isColumn;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAlias() {
		return alias;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getColumn() {
		return column;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSelection() {
		return toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		/*
		 * add cast if variable is set
		 */
		if (cast != null) {
			builder.append(ISqlConstants.ISqlKeywords.CAST);
			builder.append(BracketRoundOpen.TOKEN);
		}
		/*
		 * add from-part alias if content is a column name
		 */
		if (isColumn && alias != null) {
			builder.append(alias);
			builder.append(Dot.TOKEN);
		}
		/*
		 * add bracket to protect non-column selection
		 */
		if (!isColumn) {
			builder.append(BracketRoundOpen.TOKEN);
		}
		builder.append(column);
		/*
		 * add bracket to protect non-column selection
		 */
		if (!isColumn) {
			builder.append(BracketRoundClose.TOKEN);
		}
		/*
		 * end cast if cast is set
		 */
		if (cast != null) {
			builder.append(ISqlConstants.WHITESPACE);
			builder.append(As.TOKEN);
			builder.append(ISqlConstants.WHITESPACE);
			builder.append(cast);
			builder.append(BracketRoundClose.TOKEN);
		}
		/*
		 * add alias if content is not a column
		 */
		if (!isColumn && alias != null) {
			builder.append(ISqlConstants.WHITESPACE);
			builder.append(As.TOKEN);
			builder.append(ISqlConstants.WHITESPACE);
			builder.append(alias);
		}
		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void cast(String type) {
		this.cast = type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SqlTables getCurrentTable() {
		return sqlTable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCurrentTable(SqlTables table) {
		this.sqlTable = table;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isColumn() {
		return isColumn;
	}

}
