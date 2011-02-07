/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.definition.core;

import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;

/**
 * A selection entry for Case Selection
 * @author Sven Krosse
 *
 */
public class CaseSelection extends Selection {

	/**
	 * 
	 */
	public static final String IS_NULL_VALUE_IN_SQL = "IS_NULL_VALUE_IN_SQL";
	private ISqlDefinition innerDef;
		
	/**
	 * @param selection
	 */
	public CaseSelection(ISqlDefinition innerDef, String alias) {
		super("", alias);
		this.innerDef = innerDef;
		
//		StringBuilder builder = new StringBuilder();
//		builder.append("CASE WHEN COUNT ( ( SELECT(")
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		
		SqlTables table = innerDef.getCurrentTable();
		String type = table == SqlTables.STRING ? "ARRAY['" + IS_NULL_VALUE_IN_SQL + "']::varchar[]" : "ARRAY[-1]::bigint[]";
		
		return "unnest ( CASE WHEN array_upper( ARRAY ( ( " + innerDef.toString() + " ) ) , 1 ) > 0 THEN ARRAY( " + innerDef.toString() + " ) ELSE " + type + " END ) AS " + getAlias();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object arg0) {
		if ( arg0 instanceof CaseSelection ){
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
