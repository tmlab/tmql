/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.definition.core;

import java.text.MessageFormat;

import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;

/**
 * Criterion represents an boolean In condition.
 * @author Sven Krosse
 *
 */
public class InCriterion extends Criterion {

	private static final String ALIAS_STRING = "{0}.{1} IN ( {2} ) ";
	private static final String NOALIAS_STRING = "{0} IN ( {1} ) ";
	
	private final String column;
	private final String alias;
	private final ISqlDefinition definition;
	
	/**
	 * constructor
	 * @param column the column
	 * @param alias the alias
	 * @param definition the inner SQL definition
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
	public String toString() {
		if ( alias == null ){
			return MessageFormat.format(NOALIAS_STRING, column, definition.toString());
		}
		return MessageFormat.format(ALIAS_STRING, alias, column, definition.toString());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object obj) {
		if ( obj == this){
			return true;
		}
		if ( obj instanceof InCriterion ){
			InCriterion c = ((InCriterion) obj);
			boolean r = c.definition ==definition;
			r &= c.column.equalsIgnoreCase(column);
			r &= c.alias == null ? alias == null : c.alias.equalsIgnoreCase(alias);
			return r;
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		int hash = definition.hashCode();
		hash |= column.hashCode();
		hash |= alias == null ? 0 : alias.hashCode();
		return hash;
	}

}
