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
public class TypesAxisTranslator extends AxisTranslatorImpl {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISqlDefinition forward(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition) throws TMQLRuntimeException {
		ISqlDefinition result = definition.clone();
		result.clearSelection();
		/*
		 * append from clause for characteristics
		 */
		IFromPart fromPart = new FromPart(definition.getLastSelection().getCurrentTable() == SqlTables.TOPIC ? ISchema.RelInstanceOf.TABLE : ISchema.Typeables.TABLE, result.getAlias(), true);
		result.addFromPart(fromPart);
		/*
		 * append condition as connection to incoming SQL definition
		 */
		ISelection selection = definition.getLastSelection();
		String condition = ConditionalUtils.equal(selection, fromPart.getAlias(), definition.getLastSelection().getCurrentTable() == SqlTables.TOPIC ? ISchema.RelInstanceOf.ID_INSTANCE
				: ISchema.Constructs.ID);
		result.add(condition);
		/*
		 * add new selection
		 */
		ISelection sel = new Selection(ISchema.RelInstanceOf.ID_TYPE, fromPart.getAlias());
		result.addSelection(sel);
		sel.setCurrentTable(SqlTables.TOPIC);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISqlDefinition backward(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition) throws TMQLRuntimeException {
		/*
		 * special handling for tm:subject >> instances and tm:subject << types
		 */
		if (definition.getLastSelection().getCurrentTable() == SqlTables.TMSUBJECT) {
			definition.getLastSelection().setCurrentTable(SqlTables.TOPIC);
			return definition;
		}
		ISqlDefinition result = definition.clone();
		result.clearSelection();
		/*
		 * append from clause for characteristics
		 */
		IFromPart fromPart = new FromPart(ISchema.RelInstanceOf.TABLE, result.getAlias(), true);
		result.addFromPart(fromPart);
		/*
		 * append condition as connection to incoming SQL definition
		 */
		ISelection selection = definition.getLastSelection();
		String condition = ConditionalUtils.equal(selection, fromPart.getAlias(), ISchema.RelInstanceOf.ID_TYPE);
		result.add(condition);
		/*
		 * add new selection
		 */
		ISelection sel = new Selection(ISchema.RelInstanceOf.ID_INSTANCE, fromPart.getAlias());
		result.addSelection(sel);
		sel.setCurrentTable(SqlTables.TOPIC);
		return result;
	}
}
