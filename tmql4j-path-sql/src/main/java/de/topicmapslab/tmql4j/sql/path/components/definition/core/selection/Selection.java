/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.definition.core.selection;

import java.text.MessageFormat;

import de.topicmapslab.tmql4j.path.grammar.lexical.As;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.path.grammar.lexical.Dot;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;

/**
 * @author Sven Krosse
 * 
 */
public class Selection implements ISelection {

	/**
	 * 
	 */
	private static final String WS = " ";

	private static final String CAST = "CAST ( {0} AS {1} )";

	private final String column;
	private final String alias;
	private final String selection;
	private String cast;
	private boolean isColumn = false;
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
		this.selection = selection;
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
		if (alias != null) {
			this.selection = alias + Dot.TOKEN + column;
		} else {
			this.selection = column;
		}
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
		if (alias != null) {
			if (isColumn) {
				this.selection = alias + Dot.TOKEN + column;
			} else {
				this.selection = BracketRoundOpen.TOKEN + WS + column + WS + BracketRoundClose.TOKEN + WS + As.TOKEN + WS + alias;
			}
		} else if (isColumn) {
			this.selection = column;
		} else {
			this.selection = BracketRoundOpen.TOKEN + WS + column + WS + BracketRoundClose.TOKEN;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getColumn() {
		return column;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getSelection() {
		return selection;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		if (this.cast != null) {
			return MessageFormat.format(CAST, this.selection, this.cast);
		}
		return selection;
	}

	/**
	 * {@inheritDoc}
	 */
	public void cast(String type) {
		this.cast = type;
	}

	/**
	 * {@inheritDoc}
	 */
	public SqlTables getCurrentTable() {
		return sqlTable;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setCurrentTable(SqlTables table) {
		this.sqlTable = table;
	}

}
