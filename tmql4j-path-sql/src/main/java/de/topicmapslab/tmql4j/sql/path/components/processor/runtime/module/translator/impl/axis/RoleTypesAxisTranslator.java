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
public class RoleTypesAxisTranslator extends AbstractRolesAxisTranslator {

	private static final String TYPE = "id_type";
	private static final String BACKWARD_SELECTION = "id";
	private static final String TABLE = "roles";
	private static final String ASSOCIATIONS = "associations";
	private static final String PARENT_CONDITION = "{0}.id = {1}.id_parent";
	private static final String BACKWARD_CONDITION = "{0} = {1}.id_type";

	@Override
	protected String getForkwardSelectionColumn() {
		return TYPE;
	}

	@Override
	protected SqlTables getForwardSelectionType() {
		return SqlTables.TOPIC;
	}

	@Override
	protected void handleOptionalTypeArgument(ITMQLRuntime runtime, IContext context, ISqlDefinition result, String optionalType, IFromPart fromPart) {
		ISelection sel = result.getLastSelection();
		TranslatorUtils.addOptionalTopicTypeArgument(runtime, context, optionalType, result, sel.getColumn(), sel.getAlias());
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
		ISelection sel = new Selection(BACKWARD_SELECTION, fromPartAssociation.getAlias());
		result.addSelection(sel);
		sel.setCurrentTable(SqlTables.ANY);
		/*
		 * add optional type argument if necessary
		 */
		TranslatorUtils.addOptionalTypeArgument(runtime, context, optionalType, definition, TYPE, fromPartAssociation.getAlias());
		return result;
	}
}
