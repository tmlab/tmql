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
import de.topicmapslab.tmql4j.sql.path.components.definition.core.FromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.InCriterion;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.SqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.utils.TranslatorUtils;

/**
 * @author Sven Krosse
 * 
 */
public class TraverseAxisTranslator extends AxisTranslatorImpl {

	static final String ASSOCIATIONS = "associations";
	static final String TABLE = "roles";
	static final String FORWARD_SELECTION = "id_player";
	static final String BACKWARD_SELECTION = "id_parent";
	static final String PARENT_CONDITION = "{0}.id = {1}.id_parent";
	static final String PLAYER_CONTITION = "{0}.id_player = {1}";
	static final String TYPE = "id_type";
	static final String FORWARD_CONDITION_DIFFERENT_PLAYER = "{0} != {1}.id_player";
	static final String PARENT = "id_parent";
	static final String PLAYER = "id_player";

	static final String BACKWARD_PARENT_CONDITION = "{0}.id_parent = {1}";
	static final String BACKWARD_CONDITION_DIFFERENT_PLAYER = "{0} != {1}.id_parent";
	static final String BACKWARD_CONDITION_TRAVERSE = "{0}.id_player IN ( SELECT id_player FROM roles WHERE id_parent = ( {1} ) )";

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
		/*
		 * append condition as connection to incoming SQL definition
		 */
		ISelection selection = definition.getLastSelection();
		result.add(MessageFormat.format(FORWARD_CONDITION_DIFFERENT_PLAYER, selection.getSelection(), fromPart.getAlias()));

		/*
		 * create condition for traversal step In condition for co-players
		 */
		ISqlDefinition inDef = new SqlDefinition();
		inDef.setInternalAliasIndex(result.getInternalAliasIndex());
		IFromPart inDefFromPart = new FromPart(TABLE, result.getAlias(), true);
		inDef.addFromPart(inDefFromPart);
		inDef.addSelection(new Selection(PARENT, inDefFromPart.getAlias()));
		inDef.add(MessageFormat.format(PLAYER_CONTITION, inDefFromPart.getAlias(), selection.getSelection()));
		/*
		 * optional type condition
		 */
		if (optionalType != null) {
			/*
			 * add from part and binding for association table
			 */
			IFromPart fromPartAssociation = new FromPart("associations", result.getAlias(), true);
			inDef.addFromPart(fromPartAssociation);
			inDef.add(MessageFormat.format(PARENT_CONDITION, fromPartAssociation.getAlias(), inDefFromPart.getAlias()));
			/*
			 * add type condition
			 */
			ISqlDefinition inTypeBySi = TranslatorUtils.topicBySubjectIdentifier(result, runtime.getConstructResolver().toAbsoluteIRI(context, optionalType));
			inDef.add(new InCriterion(TYPE, fromPartAssociation.getAlias(), inTypeBySi));
		}
		result.add(new InCriterion(PARENT, fromPart.getAlias(), inDef));
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
		/*
		 * append condition as connection to incoming SQL definition
		 */
		ISelection selection = definition.getLastSelection();
		result.add(MessageFormat.format(BACKWARD_CONDITION_DIFFERENT_PLAYER, selection.getSelection(), fromPart.getAlias()));

		/*
		 * create condition for traversal step In condition for co-players
		 */
		ISqlDefinition inDef = new SqlDefinition();
		inDef.setInternalAliasIndex(result.getInternalAliasIndex());
		IFromPart inDefFromPart = new FromPart(TABLE, result.getAlias(), true);
		inDef.addFromPart(inDefFromPart);
		inDef.addSelection(new Selection(PLAYER, inDefFromPart.getAlias()));
		/*
		 * direct association mapping if current node is an association
		 */
		if (result.getCurrentTable() == SqlTables.TOPIC) {
			ISqlDefinition associationsOfType = TranslatorUtils.generateSqlDefinitionForTypeables(runtime, context, selection.getSelection(), definition.getInternalAliasIndex());
			inDef.add(new InCriterion(PARENT,inDefFromPart.getAlias(), associationsOfType));
		}
		/*
		 * indirect association mapping over type is current node is a topic
		 */
		else{
			inDef.add(MessageFormat.format(BACKWARD_PARENT_CONDITION, inDefFromPart.getAlias(), selection.getSelection()));	
		}
		/*
		 * optional type condition
		 */
		if (optionalType != null) {
			/*
			 * add from part and binding for association table
			 */
			IFromPart fromPartAssociation = new FromPart("associations", result.getAlias(), true);
			inDef.addFromPart(fromPartAssociation);
			inDef.add(MessageFormat.format(PARENT_CONDITION, fromPartAssociation.getAlias(), inDefFromPart.getAlias()));
			/*
			 * add type condition
			 */
			ISqlDefinition inTypeBySi = TranslatorUtils.topicBySubjectIdentifier(result, runtime.getConstructResolver().toAbsoluteIRI(context, optionalType));
			inDef.add(new InCriterion(TYPE, fromPartAssociation.getAlias(), inTypeBySi));
		}
		result.add(new InCriterion(PLAYER, fromPart.getAlias(), inDef));
		/*
		 * add new selection
		 */
		result.addSelection(new Selection(BACKWARD_SELECTION, fromPart.getAlias()));
		result.setCurrentTable(SqlTables.ASSOCIATION);
		return result;
	}
}
