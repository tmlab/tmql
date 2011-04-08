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

import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;

/**
 * @author Sven Krosse
 * 
 */
public class CountSelection implements ISelection {

	private static final String FORMAT = "COUNT ( {0} ) AS {1}";
	private final ISelection selection;
	private String alias;
	private SqlTables table;

	/**
	 * constructor
	 * 
	 * @param selection
	 *            the inner selection
	 * @param alias
	 *            the alias
	 */
	public CountSelection(ISelection selection, String alias) {
		this.selection = selection;
		this.alias = alias;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return MessageFormat.format(FORMAT, selection.toString(), alias);
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
		return getAlias();
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
	public void cast(String type) {
		// VOID
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCurrentTable(SqlTables table) {
		this.table = table;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SqlTables getCurrentTable() {
		return table;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isColumn() {
		return false;
	}
}
