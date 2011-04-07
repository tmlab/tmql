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
import de.topicmapslab.tmql4j.util.TmdmSubjectIdentifier;

/**
 * @author Sven Krosse
 * 
 */
public class CharacteristicsAxisTranslator extends AxisTranslatorImpl {

	/**
	 * {@inheritDoc}
	 */
	@Override
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
		if (TmdmSubjectIdentifier.isTmdmName(optionalType)) {
			fromPart = new FromPart(ISchema.Names.TABLE, result.getAlias(), true);
			table = SqlTables.NAME;
		} else if (TmdmSubjectIdentifier.isTmdmOccurrence(optionalType)) {
			fromPart = new FromPart(ISchema.Occurrences.TABLE, result.getAlias(), true);
			table = SqlTables.OCCURRENCE;
		} else {
			fromPart = new FromPart(ISchema.Characteristics.NAMES_AND_OCCURRENCES, result.getAlias(), false);
			table = SqlTables.CHARACTERISTICS;
		}

		result.addFromPart(fromPart);
		/*
		 * append condition as connection to incoming SQL definition
		 */
		ISelection selection = definition.getLastSelection();
		String condition = ConditionalUtils.equal(selection, fromPart.getAlias(), ISchema.Constructs.ID_PARENT);
		result.add(condition);
		/*
		 * add new selection
		 */
		ISelection sel = new Selection(ISchema.Constructs.ID, fromPart.getAlias());
		sel.setCurrentTable(table);
		result.addSelection(sel);
		/*
		 * check for optional type
		 */
		if (!fromPart.isTable()) {
			TranslatorUtils.addOptionalTypeArgument(runtime, context, optionalType, result, ISchema.Typeables.ID_TYPE, fromPart.getAlias());
		}
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
		IFromPart fromPart = new FromPart(ISchema.Constructs.TABLE, result.getAlias(), true);
		result.addFromPart(fromPart);
		/*
		 * append condition as connection to incoming SQL definition
		 */
		ISelection selection = definition.getLastSelection();
		String condition = ConditionalUtils.equal(selection, fromPart.getAlias(), ISchema.Constructs.ID);
		result.add(condition);
		/*
		 * add new selection
		 */
		ISelection sel = new Selection(ISchema.Constructs.ID_PARENT, fromPart.getAlias());
		sel.setCurrentTable(SqlTables.TOPIC);
		result.addSelection(sel);
		TranslatorUtils.addOptionalTopicTypeArgument(runtime, context, optionalType, definition, ISchema.Constructs.ID_PARENT, fromPart.getAlias());
		return result;
	}

}
