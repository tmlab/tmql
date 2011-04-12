/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.utils;

import java.text.MessageFormat;

import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;

/**
 * @author Sven Krosse
 * 
 */
public class ConditionalUtils {

	private static final String EQUALITY_WITH_ALIAS = "{0}.{1} = {2}.{3}";

	private static final String EQUALITY_WITH_SELECTION = "{0} = {1}.{2}";

	private static final String NON_EQUALITY_WITH_ALIAS = "{0}.{1} != {2}.{3}";

	private static final String NON_EQUALITY_WITH_SELECTION = "{0} != {1}.{2}";

	private final static String COMPARISON = "{0} {1} {2}";

	private static final String NON_EMPTY = "COUNT (*) > 0 ";

	public static final String equal(ISelection selection, String alias, String column) {
		return MessageFormat.format(EQUALITY_WITH_SELECTION, selection.getSelection(), alias, column);
	}

	public static final String equal(String value, String alias, String column) {
		return MessageFormat.format(EQUALITY_WITH_SELECTION, value, alias, column);
	}

	public static final String equal(String aliasA, String columnA, String aliasB, String columnB) {
		return MessageFormat.format(EQUALITY_WITH_ALIAS, aliasA, columnA, aliasB, columnB);
	}

	public static final String unequal(ISelection selection, String alias, String column) {
		return MessageFormat.format(NON_EQUALITY_WITH_SELECTION, selection.toString(), alias, column);
	}

	public static final String unequal(String aliasA, String columnA, String aliasB, String columnB) {
		return MessageFormat.format(NON_EQUALITY_WITH_ALIAS, aliasA, columnA, aliasB, columnB);
	}

	public static final String compare(ISqlDefinition left, String operator, ISqlDefinition right) {
		return MessageFormat.format(COMPARISON, left.toString(), operator, right.toString());
	}

	public static final ISelection nonEmpty() {
		return new Selection(NON_EMPTY, null);
	}
}
