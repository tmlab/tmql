/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.axis;

import java.text.MessageFormat;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.from.FromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;

/**
 * @author Sven Krosse
 * 
 */
public class IdAxisTranslator extends AxisTranslatorImpl {
		
	private static final String TABLE = "constructs";
	private static final String COLUMN = "id";
	private static final String CONDITION = "{0}.id = {1}";
	private static final String VARCHAR = "character varying";
	
	/**
	 * {@inheritDoc}
	 */
	protected ISqlDefinition forward(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition) throws TMQLRuntimeException {
		definition.setCurrentTable(SqlTables.STRING);
		definition.getLastSelection().cast(VARCHAR);
		return definition;
	}

	/**
	 * {@inheritDoc}
	 */
	protected ISqlDefinition backward(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition) throws TMQLRuntimeException {
		if (definition.getCurrentTable() != SqlTables.STRING) {
			return definition;
		}
		/*
		 * clone definition
		 */
		ISqlDefinition newDefinition = definition.clone();
		newDefinition.clearSelection();
		/*
		 * add from part
		 */
		IFromPart from = new FromPart(TABLE, newDefinition.getAlias(), true);
		newDefinition.addFromPart(from);
		/*
		 * add condition
		 */
		ISelection lastSelection = definition.getLastSelection();
		newDefinition.add(MessageFormat.format(CONDITION, from.getAlias(), lastSelection.getSelection()));
		/*
		 * add selection part
		 */
		newDefinition.addSelection(new Selection(COLUMN, from.getAlias()));
		newDefinition.setCurrentTable(SqlTables.ANY);
		return newDefinition;
	}

}
