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
import de.topicmapslab.tmql4j.sql.path.utils.TranslatorUtils;

/**
 * @author Sven Krosse
 * 
 */
public class PlayersAxisTranslator extends AxisTranslatorImpl {

	static final String FORWARD_CONDITION_ROLE = "{0} = {1}.id";
	static final String FORWARD_CONDITION_TOPIC = "{0} = {1}.id_type";
	static final String PARENT_CONDITION = "{0}.id = {1}.id_parent";
	static final String BACKWARD_CONDITION = "{0} = {1}.id_player";

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
		IFromPart fromPart = new FromPart(ISchema.Roles.TABLE, result.getAlias(), true);
		result.addFromPart(fromPart);
		ISelection selection = definition.getLastSelection();
		/*
		 * current node is topic -> all associations with this type
		 */
		if (selection.getCurrentTable() == SqlTables.TOPIC) {
			/*
			 * append condition
			 */
			String condition = ConditionalUtils.equal(selection.getSelection(), fromPart.getAlias(), ISchema.Typeables.ID_TYPE);
			result.add(condition);
		}
		/*
		 * current node is anything else
		 */
		else {
			/*
			 * append condition
			 */
			String condition = ConditionalUtils.equal(selection.getSelection(), fromPart.getAlias(), ISchema.Constructs.ID);
			result.add(condition);
		}
		/*
		 * add new selection
		 */
		ISelection sel = new Selection(ISchema.Roles.ID_PLAYER, fromPart.getAlias());
		result.addSelection(sel);
		sel.setCurrentTable(SqlTables.TOPIC);
		/*
		 * add optional type argument if necessary
		 */
		TranslatorUtils.addOptionalTopicTypeArgument(runtime, context, optionalType, result, ISchema.Roles.ID_PLAYER, fromPart.getAlias());
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
		 * append from clause for characteristics
		 */
		IFromPart fromPart = new FromPart(ISchema.Roles.TABLE, result.getAlias(), true);
		result.addFromPart(fromPart);
		/*
		 * append condition as connection to incoming SQL definition
		 */
		ISelection selection = definition.getLastSelection();
		String condition = ConditionalUtils.equal(selection.getSelection(), fromPart.getAlias(), ISchema.Roles.ID_PLAYER);
		result.add(condition);
		/*
		 * add new selection
		 */
		ISelection sel = new Selection(ISchema.Constructs.ID, fromPart.getAlias());
		result.addSelection(sel);
		sel.setCurrentTable(SqlTables.ROLE);
		/*
		 * add optional type argument if necessary
		 */
		TranslatorUtils.addOptionalTypeArgument(runtime, context, optionalType, result, ISchema.Typeables.ID_TYPE, fromPart.getAlias());
		return result;
	}
}
