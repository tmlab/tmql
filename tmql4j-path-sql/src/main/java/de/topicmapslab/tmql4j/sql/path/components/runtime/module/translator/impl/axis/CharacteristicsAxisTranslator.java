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
import de.topicmapslab.tmql4j.util.TmdmSubjectIdentifier;

/**
 * @author Sven Krosse
 * 
 */
public class CharacteristicsAxisTranslator extends AxisTranslatorImpl {

	static final String FORWARD_SELECTION = "id";
	static final String COL_TYPE = "id_type";
	static final String COL_PARENT = "id_parent";
	static final String BACKWARD_SELECTION = "id_parent";
	static final String CONSTRUCTS = "constructs";
	static final String CHARACTERISTICS = "SELECT id_parent, id, value, id_type FROM names UNION SELECT id_parent, id, value, id_type FROM occurrences";
	static final String NAMES = "names";
	static final String OCCURRENCES = "occurrences";
	static final String FORWARD_CONDITION = "{0} = {1}.id_parent";
	static final String BACKWARD_CONDITION = "{0} = {1}.id";

	/**
	 * {@inheritDoc}
	 */
	protected ISqlDefinition forward(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition) throws TMQLRuntimeException {
		ISqlDefinition result = definition.clone();
		result.clearSelection();
		SqlTables table;
		/*
		 * append from clause for characteristics
		 */
		IFromPart fromPart;
		/*
		 * check for tm:name and tm:occurrence
		 */
		if (TmdmSubjectIdentifier.isTmdmName(optionalType)){
			fromPart = new FromPart(NAMES, result.getAlias(), true);
			table = SqlTables.NAME;	
		}else if (TmdmSubjectIdentifier.isTmdmOccurrence(optionalType)){
			fromPart = new FromPart(OCCURRENCES, result.getAlias(), true);
			table = SqlTables.OCCURRENCE;	
		}else{
			fromPart = new FromPart(CHARACTERISTICS, result.getAlias(), false);
			table = SqlTables.CHARACTERISTICS;	
		}
		
		result.addFromPart(fromPart);
		/*
		 * append condition as connection to incoming SQL definition
		 */
		ISelection selection = definition.getLastSelection();
		result.add(MessageFormat.format(FORWARD_CONDITION, selection.getSelection(), fromPart.getAlias()));
		/*
		 * add new selection
		 */
		ISelection sel = new Selection(FORWARD_SELECTION, fromPart.getAlias());
		sel.setCurrentTable(table);
		result.addSelection(sel);	
		/*
		 * check for optional type
		 */
		if ( !fromPart.isTable()){
			TranslatorUtils.addOptionalTypeArgument(runtime, context, optionalType, result, COL_TYPE, fromPart.getAlias());
		}
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
		IFromPart fromPart = new FromPart(CONSTRUCTS, result.getAlias(), true);
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
		sel.setCurrentTable(SqlTables.TOPIC);
		result.addSelection(sel);
		TranslatorUtils.addOptionalTopicTypeArgument(runtime, context, optionalType, definition, COL_PARENT, fromPart.getAlias());
		return result;
	}

}
