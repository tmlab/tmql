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
import de.topicmapslab.tmql4j.util.TmdmSubjectIdentifier;

/**
 * @author Sven Krosse
 * 
 */
public class TypedAxisTranslator extends AxisTranslatorImpl {

	private static final String ROLES = "roles";
	private static final String ASSOCIATIONS = "associations";
	private static final String NAMES = "names";
	private static final String OCCURRENCES = "occurrences";
	static final String FORWARD_SELECTION = "id";
	static final String TABLE = "typeables";
	static final String FORWARD_CONDITION = "{0} = {1}.id_type";

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
		IFromPart fromPart = null;
		if (optionalType == null) {
			fromPart = new FromPart(TABLE, result.getAlias(), true);
		} else if (TmdmSubjectIdentifier.isTmdmName(optionalType)) {
			fromPart = new FromPart(NAMES, result.getAlias(), true);
		} else if (TmdmSubjectIdentifier.isTmdmOccurrence(optionalType)) {
			fromPart = new FromPart(OCCURRENCES, result.getAlias(), true);
		} else if (TmdmSubjectIdentifier.isTmdmAssociation(optionalType)) {
			fromPart = new FromPart(ASSOCIATIONS, result.getAlias(), true);
		} else if (TmdmSubjectIdentifier.isTmdmRole(optionalType)) {
			fromPart = new FromPart(ROLES, result.getAlias(), true);
		} else {
			throw new TMQLRuntimeException("Unsupported type for optional argument of typed axis!");
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
		result.addSelection(sel);
		sel.setCurrentTable(SqlTables.ANY);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISqlDefinition backward(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition) throws TMQLRuntimeException {
		return new TypesAxisTranslator().forward(runtime, context, optionalType, definition);
	}
}
