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

import de.topicmapslab.tmql4j.path.grammar.lexical.Dot;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;

/**
 * @author Sven Krosse
 * 
 */
public class Selection implements ISelection {
	
	private static final String CAST = "CAST ( {0} AS {1} )";

	private final String column;
	private final String alias;
	private final String selection;
	private String cast;

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
		if ( this.cast != null ){
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

}
