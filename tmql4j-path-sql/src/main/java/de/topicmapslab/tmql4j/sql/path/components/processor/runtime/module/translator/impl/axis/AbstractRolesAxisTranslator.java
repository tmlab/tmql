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
import de.topicmapslab.tmql4j.sql.path.components.definition.core.where.InCriterion;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.utils.TranslatorUtils;

/**
 * @author Sven Krosse
 * 
 */
public abstract class AbstractRolesAxisTranslator extends AxisTranslatorImpl {
	
	private static final String ID_PARENT = "id_parent";
	private static final String TYPE = "id_type";
	private static final String ID = "id";
	private static final String BACKWARD_SELECTION = "id";
	private static final String TABLE = "roles";
	private static final String ASSOCIATIONS = "associations";
	private static final String PARENT_CONDITION = "{0}.id = {1}.id_parent";
	private static final String FORWARD_CONDITION = "{0} = {1}.id_parent";
	private static final String BACKWARD_CONDITION = "{0} = {1}.id";

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
		if (selection.getCurrentTable() == SqlTables.TOPIC) {
			/*
			 * select all typed constructs by type
			 */
			ISqlDefinition inSqlDef = TranslatorUtils.generateSqlDefinitionForTypeables(runtime, context, selection.getSelection(), result.getInternalAliasIndex()+1);
			/*
			 * create IN criterion and add to selection
			 */
			InCriterion criterion = new InCriterion(ID_PARENT, fromPart.getAlias(), inSqlDef);
			result.add(criterion);			
		} else {
			/*
			 * append condition as connection to incoming SQL definition
			 */
			result.add(MessageFormat.format(FORWARD_CONDITION, selection.getSelection(), fromPart.getAlias()));
		}
		/*
		 * add new selection
		 */
		ISelection sel = new Selection(getForkwardSelectionColumn(), fromPart.getAlias());
		result.addSelection(sel);
		sel.setCurrentTable(getForwardSelectionType());
		/*
		 * add optional type argument if necessary
		 */
		handleOptionalTypeArgument(runtime, context, result, optionalType, fromPart);
		return result;
	}

	protected abstract void handleOptionalTypeArgument(ITMQLRuntime runtime, IContext context, ISqlDefinition result, String optionalType, IFromPart fromPart);
	protected abstract SqlTables getForwardSelectionType();
	protected abstract String getForkwardSelectionColumn();
	
	
}
