/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.axis;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.from.FromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.utils.ConditionalUtils;
import de.topicmapslab.tmql4j.sql.path.utils.ISchema;

/**
 * @author Sven Krosse
 * 
 */
public abstract class IdentityAxisTranslator extends AxisTranslatorImpl {

	/**
	 * {@inheritDoc}
	 */
	@Override
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
		String condition = ConditionalUtils.equal(selection.getSelection(), fromPartRel.getAlias(), getRelationColumn());
		result.add(condition);
		/*
		 * add new selection
		 */

		ISelection sel = new Selection(ISchema.RelItemIdentifiers.ID_LOCATOR, fromPartRel.getAlias());
		sel.setCurrentTable(SqlTables.LOCATOR);
		result.addSelection(sel);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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
		if (selection.getCurrentTable() == SqlTables.STRING) {
			IFromPart fromPart = new FromPart(ISchema.Locators.TABLE, result.getAlias(), true);
			result.addFromPart(fromPart);
			String condition = ConditionalUtils.equal(selection.getCurrentTable() == SqlTables.STRING ? selection.getColumn() : selection.getSelection(), fromPart.getAlias(),
					ISchema.Locators.REFERENCE);
			result.add(condition);
			condition = ConditionalUtils.equal(fromPart.getAlias(), ISchema.Constructs.ID, fromPartRel.getAlias(), ISchema.RelItemIdentifiers.ID_LOCATOR);
			result.add(condition);
		}
		/*
		 * current nodes are locators
		 */
		else {
			String condition = ConditionalUtils.equal(selection.getCurrentTable() == SqlTables.STRING ? selection.getColumn() : selection.getSelection(), fromPartRel.getAlias(), getRelationColumn());
			result.add(condition);
		}
		/*
		 * add new selection
		 */
		ISelection sel = new Selection(getRelationColumn(), fromPartRel.getAlias());
		sel.setCurrentTable(SqlTables.TOPIC);
		result.addSelection(sel);
		return result;
	}

	/**
	 * returns the relational table name
	 * 
	 * @return the relational table name
	 */
	protected abstract String getRelationTable();

	/**
	 * Returns the column name of the relation table
	 * 
	 * @return the column name
	 */
	protected abstract String getRelationColumn();
}
