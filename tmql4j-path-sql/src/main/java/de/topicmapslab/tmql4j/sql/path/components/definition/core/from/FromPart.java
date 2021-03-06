/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.definition.core.from;

import de.topicmapslab.tmql4j.path.grammar.lexical.As;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants;

/**
 * @author Sven Krosse
 * 
 */
public class FromPart implements IFromPart {

	private final String content;
	private final String alias;
	private boolean isTable;

	/**
	 * constructor
	 * 
	 * @param content
	 *            the content
	 * @param alias
	 *            the alias
	 * @param flag
	 *            indicates if the content is only a table
	 */
	public FromPart(final String content, final String alias, boolean isTable) {
		this.content = content;
		this.alias = alias;
		this.isTable = isTable;
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
	public String getTableOrContent() {
		return content;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (isTable) {
			builder.append(ISqlConstants.WHITESPACE);
			builder.append(getTableOrContent());
			builder.append(ISqlConstants.WHITESPACE);
		} else {
			builder.append(ISqlConstants.WHITESPACE);
			builder.append(BracketRoundOpen.TOKEN);
			builder.append(ISqlConstants.WHITESPACE);
			builder.append(getTableOrContent());
			builder.append(ISqlConstants.WHITESPACE);
			builder.append(BracketRoundClose.TOKEN);
			builder.append(ISqlConstants.WHITESPACE);
		}
		builder.append(As.TOKEN);
		builder.append(ISqlConstants.WHITESPACE);
		builder.append(getAlias());
		builder.append(ISqlConstants.WHITESPACE);
		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isTable() {
		return isTable;
	}
}
