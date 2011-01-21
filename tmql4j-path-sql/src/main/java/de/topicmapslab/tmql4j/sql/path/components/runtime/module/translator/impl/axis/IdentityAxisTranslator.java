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
import de.topicmapslab.tmql4j.sql.path.components.definition.core.FromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;

/**
 * @author Sven Krosse
 * 
 */
public abstract class IdentityAxisTranslator extends AxisTranslatorImpl {

	static final String LOCATORS = "locators";

	static final String FORWARD_SELECTION = "id";

	static final String CONDITION = "{0}.id = {1}.id_locator";
	static final String CONDITION_WITHOUT_ALIAS = "{0} = {1}.{2}";
	static final String FORWARD_CONDITION = "{0} = {1}.{2}";
	static final String BACKWARD_CONDITION = "{0} = {1}.reference";

	/**
	 * {@inheritDoc}
	 */
	protected ISqlDefinition forward(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition) throws TMQLRuntimeException {
		ISqlDefinition result = definition.clone();
		result.clearSelection();
		/*
		 * append from clauses
		 */
		IFromPart fromPartRel = new FromPart(getRelationTable(), result.getAlias(), true);
		result.addFromPart(fromPartRel);
		/*
		 * append condition as connection to incoming SQL definition
		 */
		ISelection selection = definition.getLastSelection();
		result.add(MessageFormat.format(FORWARD_CONDITION, selection.getSelection(), fromPartRel.getAlias(), getRelationColumn()));
		/*
		 * add new selection
		 */
		result.addSelection(new Selection(FORWARD_SELECTION, fromPartRel.getAlias()));
		result.setCurrentTable(SqlTables.LOCATOR);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	protected ISqlDefinition backward(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition) throws TMQLRuntimeException {
		ISqlDefinition result = definition.clone();
		result.clearSelection();
		/*
		 * append from clauses
		 */
		IFromPart fromPartRel = new FromPart(getRelationTable(), result.getAlias(), true);
		result.addFromPart(fromPartRel);
		/*
		 * append condition as connection to incoming SQL definition
		 */
		ISelection selection = definition.getLastSelection();
		/*
		 * current nodes are strings
		 */
		if (definition.getCurrentTable() == SqlTables.STRING) {
			IFromPart fromPart = new FromPart(LOCATORS, result.getAlias(), true);
			result.addFromPart(fromPart);
			result.add(MessageFormat.format(BACKWARD_CONDITION, selection.getSelection(), fromPart.getAlias()));
			result.add(MessageFormat.format(CONDITION, fromPart.getAlias(), fromPartRel.getAlias()));
		}
		/*
		 * current nodes are locators
		 */
		else {
			result.add(MessageFormat.format(CONDITION_WITHOUT_ALIAS, selection.getSelection(), fromPartRel.getAlias(), getRelationColumn()));
		}
		/*
		 * add new selection
		 */
		result.addSelection(new Selection(getRelationColumn(), fromPartRel.getAlias()));
		result.setCurrentTable(SqlTables.TOPIC);
		return result;
	}

	/**
	 * returns the relational table name
	 * @return the relational table name
	 */
	protected abstract String getRelationTable();
	
	/**
	 * Returns the column name of the relation table
	 * @return the column name
	 */
	protected abstract String getRelationColumn();
}
