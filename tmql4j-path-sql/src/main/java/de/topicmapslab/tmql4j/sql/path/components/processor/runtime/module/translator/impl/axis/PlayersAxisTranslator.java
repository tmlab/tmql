/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.axis;

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
public class PlayersAxisTranslator extends AxisTranslatorImpl {
	
	static final String ASSOCIATIONS = "associations";
	static final String FORWARD_SELECTION = "id_player";
	static final String BACKWARD_SELECTION = "id";
	static final String TABLE = "roles";
	static final String FORWARD_CONDITION_ROLE = "{0} = {1}.id";
	static final String FORWARD_CONDITION_TOPIC = "{0} = {1}.id_type";
	static final String PARENT_CONDITION = "{0}.id = {1}.id_parent";
	static final String BACKWARD_CONDITION = "{0} = {1}.id_player";
	static final String TYPE = "id_type";

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
		/*
		 * current node is topic -> all associations with this type
		 */
		if (selection.getCurrentTable() == SqlTables.TOPIC) {
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
		}
		/*
		 * current node is anything else
		 */
		else {
			/*
			 * append condition
			 */
			result.add(MessageFormat.format(FORWARD_CONDITION_ROLE, selection.getSelection(), fromPart.getAlias()));
		}
		/*
		 * add new selection
		 */
		ISelection sel = new Selection(FORWARD_SELECTION, fromPart.getAlias());
		result.addSelection(sel);
		sel.setCurrentTable(SqlTables.TOPIC);
		/*
		 * add optional type argument if necessary
		 */
		TranslatorUtils.addOptionalTopicTypeArgument(runtime, context, optionalType, result, FORWARD_SELECTION, fromPart.getAlias());
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
		/*
		 * append condition as connection to incoming SQL definition
		 */
		ISelection selection = definition.getLastSelection();
		result.add(MessageFormat.format(BACKWARD_CONDITION, selection.getSelection(), fromPart.getAlias()));
		/*
		 * add new selection
		 */
		ISelection sel = new Selection(BACKWARD_SELECTION, fromPart.getAlias());
		result.addSelection(sel);
		sel.setCurrentTable(SqlTables.ROLE);
		/*
		 * add optional type argument if necessary
		 */
		TranslatorUtils.addOptionalTypeArgument(runtime, context, optionalType, result, TYPE, fromPart.getAlias());
		return result;
	}
}
