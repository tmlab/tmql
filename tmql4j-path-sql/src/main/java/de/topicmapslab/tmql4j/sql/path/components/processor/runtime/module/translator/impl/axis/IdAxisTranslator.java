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
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants;

/**
 * @author Sven Krosse
 * 
 */
public class IdAxisTranslator extends AxisTranslatorImpl {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISqlDefinition forward(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition) throws TMQLRuntimeException {
		definition.getLastSelection().setCurrentTable(SqlTables.STRING);
		definition.getLastSelection().cast(ISqlConstants.ISqlTypes.VARCHAR);
		return definition;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISqlDefinition backward(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition) throws TMQLRuntimeException {
		if (definition.getLastSelection().getCurrentTable() != SqlTables.STRING) {
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
		IFromPart from = new FromPart(ISchema.Constructs.TABLE, newDefinition.getAlias(), true);
		newDefinition.addFromPart(from);
		/*
		 * add condition
		 */
		ISelection lastSelection = definition.getLastSelection();
		String condition = ConditionalUtils.equal(lastSelection.getCurrentTable() == SqlTables.STRING ? lastSelection.getColumn() : lastSelection.getSelection(), from.getAlias(),
				ISchema.Constructs.ID);
		newDefinition.add(condition);
		/*
		 * add selection part
		 */
		ISelection sel = new Selection(ISchema.Constructs.ID, from.getAlias());
		newDefinition.addSelection(sel);
		sel.setCurrentTable(SqlTables.ANY);
		return newDefinition;
	}
}
