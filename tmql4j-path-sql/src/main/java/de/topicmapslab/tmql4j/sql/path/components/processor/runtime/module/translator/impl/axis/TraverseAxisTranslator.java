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
import de.topicmapslab.tmql4j.sql.path.components.definition.core.SqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.from.FromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.where.InCriterion;
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
public class TraverseAxisTranslator extends AxisTranslatorImpl {

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
		/*
		 * append condition as connection to incoming SQL definition
		 */
		ISelection selection = definition.getLastSelection();
		String condition = ConditionalUtils.unequal(selection, fromPart.getAlias(), ISchema.Roles.ID_PLAYER);
		result.add(condition);

		/*
		 * create condition for traversal step In condition for co-players
		 */
		ISqlDefinition inDef = new SqlDefinition();
		inDef.setInternalAliasIndex(result.getInternalAliasIndex());
		IFromPart inDefFromPart = new FromPart(ISchema.Roles.TABLE, result.getAlias(), true);
		inDef.addFromPart(inDefFromPart);
		inDef.addSelection(new Selection(ISchema.Constructs.ID_PARENT, inDefFromPart.getAlias()));
		condition = ConditionalUtils.equal(selection, inDefFromPart.getAlias(), ISchema.Roles.ID_PLAYER);
		inDef.add(condition);
		/*
		 * optional type condition
		 */
		if (optionalType != null) {
			/*
			 * add from part and binding for association table
			 */
			IFromPart fromPartAssociation = new FromPart(ISchema.Associations.TABLE, result.getAlias(), true);
			inDef.addFromPart(fromPartAssociation);
			condition = ConditionalUtils.equal(fromPartAssociation.getAlias(), ISchema.Constructs.ID, inDefFromPart.getAlias(), ISchema.Constructs.ID_PARENT);
			inDef.add(condition);
			/*
			 * add type condition
			 */
			ISqlDefinition inTypeBySi = TranslatorUtils.topicBySubjectIdentifier(result, runtime.getConstructResolver().toAbsoluteIRI(context, optionalType));
			inDef.add(new InCriterion(ISchema.Typeables.ID_TYPE, fromPartAssociation.getAlias(), inTypeBySi));
		}
		result.add(new InCriterion(ISchema.Constructs.ID_PARENT, fromPart.getAlias(), inDef));
		/*
		 * add new selection
		 */
		ISelection sel = new Selection(ISchema.Roles.ID_PLAYER, fromPart.getAlias());
		result.addSelection(sel);
		sel.setCurrentTable(SqlTables.TOPIC);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISqlDefinition backward(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition) throws TMQLRuntimeException {
		ISqlDefinition result = definition.clone();
		ISelection lastSelection = result.getLastSelection();
		result.clearSelection();
		if (lastSelection.getCurrentTable() == SqlTables.TOPIC) {
			result = TranslatorUtils.generateSqlDefinitionForTypeables(runtime, context, result, lastSelection.getSelection());
			lastSelection = result.getLastSelection();
			result.clearSelection();
		}
		IFromPart roles = new FromPart(ISchema.Roles.TABLE, result.getAlias(), true);
		result.addFromPart(roles);

		/*
		 * create inner definition
		 */
		ISqlDefinition inDef = new SqlDefinition();
		IFromPart innerFrom = new FromPart(ISchema.Roles.TABLE, inDef.getAlias(), true);
		inDef.addFromPart(innerFrom);
		String condition = ConditionalUtils.equal(lastSelection, innerFrom.getAlias(), ISchema.Constructs.ID_PARENT);
		inDef.add(condition);
		/*
		 * optional filter
		 */
		if (optionalType != null) {
			TranslatorUtils.addOptionalTopicTypeArgument(runtime, context, optionalType, inDef, ISchema.Roles.ID_PLAYER, innerFrom.getAlias());
		}
		ISelection innerSel = new Selection(ISchema.Roles.ID_PLAYER, innerFrom.getAlias());
		inDef.addSelection(innerSel);

		result.add(new InCriterion(ISchema.Roles.ID_PLAYER, roles.getAlias(), inDef));
		condition = ConditionalUtils.unequal(lastSelection, roles.getAlias(), ISchema.Constructs.ID_PARENT);
		result.add(condition);

		ISelection sel = new Selection(ISchema.Constructs.ID_PARENT, roles.getAlias());
		sel.setCurrentTable(SqlTables.ASSOCIATION);
		result.addSelection(sel);

		return result;
	}
}
