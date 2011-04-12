/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.definition.core.where;

import java.text.MessageFormat;

import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;

/**
 * Criterion represents an boolean In condition.
 * 
 * @author Sven Krosse
 * 
 */
public class InCriterion extends Criterion {

	private static final String ALIAS_STRING = "{0}.{1} IN ( {2} ) ";
	private static final String NOALIAS_STRING = "{0} IN ( {1} ) ";

	private static final String NON_ALIAS_STRING = "{0}.{1} NOT IN ( {2} ) ";
	private static final String NON_NOALIAS_STRING = "{0} NOT IN ( {1} ) ";

	private final String column;
	private final String alias;
	private final ISqlDefinition definition;
	private boolean negate = false;

	/**
	 * constructor
	 * 
	 * @param column
	 *            the column
	 * @param definition
	 *            the inner SQL definition
	 */
	public InCriterion(String column, ISqlDefinition definition) {
		super(column);
		this.column = column;
		this.alias = null;
		this.definition = definition;
	}

	/**
	 * constructor
	 * 
	 * @param column
	 *            the column
	 * @param alias
	 *            the alias
	 * @param definition
	 *            the inner SQL definition
	 */
	public InCriterion(String column, String alias, ISqlDefinition definition) {
		super(column);
		this.column = column;
		this.alias = alias;
		this.definition = definition;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		if (negate) {
			if (alias == null) {
				return MessageFormat.format(NON_NOALIAS_STRING, column, definition.toString());
			}
			return MessageFormat.format(NON_ALIAS_STRING, alias, column, definition.toString());
		} else if (alias == null) {
			return MessageFormat.format(NOALIAS_STRING, column, definition.toString());
		}
		return MessageFormat.format(ALIAS_STRING, alias, column, definition.toString());
	}

	/**
	 * Modify the internal flag of negation of this in criterion
	 * 
	 * @param negate
	 *            the flag
	 */
	public void negate(boolean negate) {
		this.negate = negate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof InCriterion) {
			InCriterion c = ((InCriterion) obj);
			boolean r = c.definition == definition;
			r &= c.column.equalsIgnoreCase(column);
			r &= c.alias == null ? alias == null : c.alias.equalsIgnoreCase(alias);
			r &= c.negate == negate;
			return r;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int hash = definition.hashCode();
		hash |= column.hashCode();
		hash |= alias == null ? 0 : alias.hashCode();
		hash |= negate ? 0 : 1;
		return hash;
	}

}
