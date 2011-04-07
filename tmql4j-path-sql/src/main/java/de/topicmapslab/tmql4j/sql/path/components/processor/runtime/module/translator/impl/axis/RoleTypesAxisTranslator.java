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
public class RoleTypesAxisTranslator extends AbstractRolesAxisTranslator {

	@Override
	protected String getForkwardSelectionColumn() {
		return ISchema.Typeables.ID_TYPE;
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
		IFromPart fromPart = new FromPart(ISchema.Roles.TABLE, result.getAlias(), true);
		result.addFromPart(fromPart);
		IFromPart fromPartAssociation = new FromPart(ISchema.Associations.TABLE, result.getAlias(), true);
		result.addFromPart(fromPartAssociation);
		/*
		 * append condition as connection to incoming SQL definition
		 */
		ISelection selection = definition.getLastSelection();
		String condition = ConditionalUtils.equal(selection, fromPart.getAlias(), ISchema.Typeables.ID_TYPE);
		result.add(condition);
		condition = ConditionalUtils.equal(fromPartAssociation.getAlias(), ISchema.Constructs.ID, fromPart.getAlias(), ISchema.Constructs.ID_PARENT);
		result.add(condition);
		/*
		 * add new selection
		 */
		ISelection sel = new Selection(ISchema.Constructs.ID, fromPartAssociation.getAlias());
		result.addSelection(sel);
		sel.setCurrentTable(SqlTables.ANY);
		/*
		 * add optional type argument if necessary
		 */
		TranslatorUtils.addOptionalTypeArgument(runtime, context, optionalType, definition, ISchema.Typeables.ID_TYPE, fromPartAssociation.getAlias());
		return result;
	}
}
