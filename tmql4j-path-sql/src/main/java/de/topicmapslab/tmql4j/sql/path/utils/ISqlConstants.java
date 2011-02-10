/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.utils;

/**
 * @author Sven Krosse
 * 
 */
public interface ISqlConstants {

	public static final String ALIAS_PREFIX = "alias";
	
	public static final String WHITESPACE = " ";
	
	public static final String SINGLEQUOTE = "'";
	
	public static final String IS_NULL_VALUE_IN_SQL = "IS_NULL_VALUE_IN_SQL";

	public interface ISqlOperators {

		public static final String CONCAT = "||";

		public static final String REGEXP = "~";

	}
	
	public interface ISqlFunctions {
		
		public static final String UNNEST = "unnest";
		
		public static final String ARRAY_UPPER = "array_upper";
		
		public static final String SUBSTRING = "substring";
		
	}

	public interface ISqlKeywords {

		public static final String SELECT = "SELECT";
		
		public static final String FROM = "FROM";
		
		public static final String ORDER_BY = "ORDER BY";
		
		public static final String CAST = "CAST";
		
		public static final String CASE = "CASE";
		
		public static final String WHEN = "WHEN";
		
		public static final String THEN = "THEN";
		
		public static final String ELSE = "ELSE";
		
		public static final String ARRAY = "ARRAY";
		
		public static final String END = "END";
		
		public static final String IN = "IN";

	}

	public interface ISqlTypes {

		public static final String BOOLEAN = "boolean";

		public static final String BIGINT = "bigint";

		public static final String VARCHAR = "varchar";
	}

}
