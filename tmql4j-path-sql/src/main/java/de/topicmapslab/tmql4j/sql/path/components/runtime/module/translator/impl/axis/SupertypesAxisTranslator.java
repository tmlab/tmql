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
import de.topicmapslab.tmql4j.sql.path.components.definition.core.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.utils.TranslatorUtils;
import de.topicmapslab.tmql4j.util.TmdmSubjectIdentifier;

/**
 * @author Sven Krosse
 * 
 */
public class SupertypesAxisTranslator extends AxisTranslatorImpl {

	static final String CONDITION_ASSOCTYPE_REF = "{0}.reference = '" + TmdmSubjectIdentifier.TMDM_SUPERTYPE_SUBTYPE_ASSOCIATION + "'";
	static final String CONDITION_SUBTYPE_REF = "{0}.reference = '" + TmdmSubjectIdentifier.TMDM_SUBTYPE_ROLE_TYPE + "'";
	static final String CONDITION_SUPERTYPE_REF = "{0}.reference = '" + TmdmSubjectIdentifier.TMDM_SUPERTYPE_ROLE_TYPE + "'";
	static final String CONDITION_REL_LOCATOR = "{0}.id = {1}.id_locator";
	static final String CONDITION_PARENT = "{0}.id = {1}.id_parent";
	static final String CONDITION_TYPE = "{0}.id_type = {1}.id_topic";
	static final String CONDITION_PLAYER = "{0} = {1}.id_player";

	static final String FORWARD_SELECTION = "id_supertype";
	static final String BACKWARD_SELECTION = "id_subtype";
//	static final String TABLE = "rel_kind_of";
	static final String TABLE_LOCATORS = "locators";
	static final String TABLE_REL_SI = "rel_subject_identifiers";
	static final String TABLE_ASSOCIATIONS = "associations";
	static final String TABLE_ROLES = "roles";
	static final String FORWARD_CONDITION = "{0} = {1}.id_subtype";

	static final String BACKWARD_CONDITION = "{0} = {1}.id_supertype";

	/**
	 * {@inheritDoc}
	 */
	protected ISqlDefinition forward(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition) throws TMQLRuntimeException {
		ISqlDefinition result = definition.clone();
		result.clearSelection();
		/*
		 * append from clauses
		 */
		IFromPart fromPart = new FromPart(TranslatorUtils.generateSupertypeSubtypeSet(runtime, context), result.getAlias(), false);
		result.addFromPart(fromPart);	
		/*
		 * append condition as connection to incoming SQL definition
		 */
		ISelection selection = definition.getLastSelection();
		result.add(MessageFormat.format(FORWARD_CONDITION, selection.getSelection(), fromPart.getAlias()));	
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
		IFromPart fromPart = new FromPart(TranslatorUtils.generateSupertypeSubtypeSet(runtime, context), result.getAlias(), false);
		result.addFromPart(fromPart);
		/*
		 * append condition as connection to incoming SQL definition
		 */
		ISelection selection = definition.getLastSelection();
		result.add(MessageFormat.format(BACKWARD_CONDITION, selection.getSelection(), fromPart.getAlias()));
		/*
		 * add new selection
		 */
		result.addSelection(new Selection(BACKWARD_SELECTION, fromPart.getAlias()));
		result.setCurrentTable(SqlTables.TOPIC);
		return result;
	}

}
