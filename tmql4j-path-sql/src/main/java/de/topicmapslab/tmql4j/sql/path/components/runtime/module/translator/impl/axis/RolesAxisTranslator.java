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
import de.topicmapslab.tmql4j.sql.path.utils.TranslatorUtils;

/**
 * @author Sven Krosse
 * 
 */
public class RolesAxisTranslator extends AxisTranslatorImpl {
	
	static final String TYPE = "id_type";
	static final String FORWARD_SELECTION = TYPE;
	static final String BACKWARD_SELECTION = "id";
	static final String TABLE = "roles";
	static final String ASSOCIATIONS = "associations";
	static final String PARENT_CONDITION = "{0}.id = {1}.id_parent";
	static final String FORWARD_CONDITION_TOPIC = "{0} = {1}.id_type";
	static final String FORWARD_CONDITION = "{0} = {1}.id_parent";
	static final String BACKWARD_CONDITION = "{0} = {1}.id_type";

	/**
	 * {@inheritDoc}
	 */
	protected ISqlDefinition forward(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition) throws TMQLRuntimeException {
		ISqlDefinition result = definition.clone();
		result.clearSelection();
		/*
		 * append from clause for characteristics
		 */
		IFromPart fromPart = new FromPart(TABLE, result.getAlias(), true);
		result.addFromPart(fromPart);
		ISelection selection = definition.getLastSelection();
		if (definition.getCurrentTable() == SqlTables.TOPIC) {
			/*
			 * add associations to from part
			 */
			IFromPart associationsFromPart = new FromPart(ASSOCIATIONS, result.getAlias(), true);
			result.addFromPart(associationsFromPart);
			/*
			 * append condition
			 */
			result.add(MessageFormat.format(FORWARD_CONDITION_TOPIC, selection.getSelection(), associationsFromPart.getAlias()));
			result.add(MessageFormat.format(PARENT_CONDITION, associationsFromPart.getAlias(), fromPart.getAlias()));
		} else {
			/*
			 * append condition as connection to incoming SQL definition
			 */
			result.add(MessageFormat.format(FORWARD_CONDITION, selection.getSelection(), fromPart.getAlias()));
		}
		/*
		 * add new selection
		 */
		result.addSelection(new Selection(FORWARD_SELECTION, fromPart.getAlias()));
		result.setCurrentTable(SqlTables.TOPIC);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	protected ISqlDefinition backward(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition) throws TMQLRuntimeException {
		ISqlDefinition result = definition.clone();
		result.clearSelection();
		/*
		 * append from clause for characteristics
		 */
		IFromPart fromPart = new FromPart(TABLE, result.getAlias(), true);
		result.addFromPart(fromPart);
		IFromPart fromPartAssociation = new FromPart(ASSOCIATIONS, result.getAlias(), true);
		result.addFromPart(fromPartAssociation);
		/*
		 * append condition as connection to incoming SQL definition
		 */
		ISelection selection = definition.getLastSelection();
		result.add(MessageFormat.format(BACKWARD_CONDITION, selection.getSelection(), fromPart.getAlias()));
		result.add(MessageFormat.format(PARENT_CONDITION, fromPartAssociation.getAlias(), fromPart.getAlias()));
		/*
		 * add new selection
		 */
		result.addSelection(new Selection(BACKWARD_SELECTION, fromPartAssociation.getAlias()));
		result.setCurrentTable(SqlTables.ANY);
		/*
		 * add optional type argument if necessary
		 */
		TranslatorUtils.addOptionalTypeArgument(runtime, context, optionalType, definition, TYPE, fromPartAssociation.getAlias());
		return result;
	}
}
