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
public class RolesAxisTranslator extends AbstractRolesAxisTranslator {

	@Override
	protected String getForkwardSelectionColumn() {
		return ISchema.Constructs.ID;
	}

	@Override
	protected SqlTables getForwardSelectionType() {
		return SqlTables.ROLE;
	}

	@Override
	protected void handleOptionalTypeArgument(ITMQLRuntime runtime, IContext context, ISqlDefinition result, String optionalType, IFromPart fromPart) {
		TranslatorUtils.addOptionalTypeArgument(runtime, context, optionalType, result, ISchema.Typeables.ID_TYPE, fromPart.getAlias());
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
		 * check last selection to append condition as connection to incoming
		 * SQL definition
		 */
		ISelection selection = definition.getLastSelection();
		if (selection.getCurrentTable() == SqlTables.TOPIC) {
			/*
			 * select all typed constructs by type
			 */
			ISqlDefinition inSqlDef = TranslatorUtils.generateSqlDefinitionForTypeables(runtime, context, selection.getSelection(), result.getInternalAliasIndex() + 1);
			/*
			 * create IN criterion and add to selection
			 */
			InCriterion criterion = new InCriterion(ISchema.Constructs.ID, fromPart.getAlias(), inSqlDef);
			result.add(criterion);
		} else {
			/*
			 * append condition as connection to incoming SQL definition
			 */
			String condition = ConditionalUtils.equal(selection, fromPart.getAlias(), ISchema.Constructs.ID);
			result.add(condition);
		}
		/*
		 * add new selection
		 */
		ISelection sel = new Selection(ISchema.Constructs.ID_PARENT, fromPart.getAlias());
		result.addSelection(sel);
		sel.setCurrentTable(SqlTables.ASSOCIATION);
		/*
		 * add optional type argument if necessary
		 */
		if (optionalType != null) {
			/*
			 * select all typed constructs by type
			 */
			ISqlDefinition inSqlDef = TranslatorUtils.generateSqlDefinitionForTypeablesByReference(runtime, context, optionalType, result.getInternalAliasIndex() + 1);
			/*
			 * create IN criterion and add to selection
			 */
			InCriterion criterion = new InCriterion(ISchema.Constructs.ID_PARENT, fromPart.getAlias(), inSqlDef);
			result.add(criterion);
		}
		return result;
	}
}
