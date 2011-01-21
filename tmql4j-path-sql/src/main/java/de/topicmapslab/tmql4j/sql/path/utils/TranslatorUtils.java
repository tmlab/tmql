/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.utils;

import java.text.MessageFormat;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.FromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.InCriterion;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.SqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.axis.TypesAxisTranslator;

/**
 * @author Sven Krosse
 * 
 */
public class TranslatorUtils {

	private static final String TABLE_LOCATORS = "locators";
	private static final String TABLE_REL_SUBJECTIDENTIFIER = "rel_subject_identifiers";
	private static final String CONDITION_RELATION = "{0}.id = {1}.id_locator";
	private static final String CONDITION_REFERENCE = "{0}.reference = ''{1}''";
	private static final String SELECTION = "id_topic";

	/**
	 * Utility method create a SQL definition to select a topic by subject
	 * identifier
	 * 
	 * @param definition
	 *            the incoming SQL definition
	 * @param reference
	 *            the reference
	 * @return the new SQL definition
	 */
	public static final ISqlDefinition topicBySubjectIdentifier(final ISqlDefinition definition, final String reference) {
		ISqlDefinition newDefinition = new SqlDefinition();
		newDefinition.setInternalAliasIndex(definition.getInternalAliasIndex());
		/*
		 * add from parts
		 */
		IFromPart locators = new FromPart(TABLE_LOCATORS, newDefinition.getAlias(), true);
		newDefinition.addFromPart(locators);
		IFromPart relation = new FromPart(TABLE_REL_SUBJECTIDENTIFIER, newDefinition.getAlias(), true);
		newDefinition.addFromPart(relation);
		/*
		 * add condition
		 */
		newDefinition.add(MessageFormat.format(CONDITION_RELATION, locators.getAlias(), relation.getAlias()));
		newDefinition.add(MessageFormat.format(CONDITION_REFERENCE, locators.getAlias(), reference));
		/*
		 * add selection
		 */
		newDefinition.addSelection(new Selection(SELECTION, relation.getAlias()));
		return newDefinition;
	}

	/**
	 * Utility method to add the optional type matching to the SQL definition if
	 * necessary
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param optionalType
	 *            the optional type
	 * @param definition
	 *            the definition
	 * @param column
	 *            the column for typing
	 * @param alias
	 *            the alias for typing
	 * @throws TMQLRuntimeException
	 *             thrown if anything fails
	 */
	public static final void addOptionalTypeArgument(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition, final String column, final String alias)
			throws TMQLRuntimeException {
		/*
		 * has optional type argument
		 */
		if (optionalType != null) {
			/*
			 * get type reference
			 */
			final String reference = runtime.getConstructResolver().toAbsoluteIRI(context, optionalType);
			/*
			 * create new SQL definition to selection typing topic
			 */
			ISqlDefinition inDef = TranslatorUtils.topicBySubjectIdentifier(definition, reference);
			/*
			 * add IN criterion
			 */
			definition.add(new InCriterion(column, alias, inDef));
		}
	}

	/**
	 * Utility method to add the optional type matching to the SQL definition if
	 * necessary
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param optionalType
	 *            the optional type
	 * @param definition
	 *            the definition
	 * @param column
	 *            the column for typing
	 * @param alias
	 *            the alias for typing
	 * @throws TMQLRuntimeException
	 *             thrown if anything fails
	 */
	public static final void addOptionalTopicTypeArgument(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition, final String column, final String alias)
			throws TMQLRuntimeException {
		/*
		 * has optional type argument
		 */
		if (optionalType != null) {
			/*
			 * get type reference
			 */
			final String reference = runtime.getConstructResolver().toAbsoluteIRI(context, optionalType);
			/*
			 * create new SQL definition to selection typing topic
			 */
			ISqlDefinition inDef = new TypesAxisTranslator().backward(runtime, null, null, TranslatorUtils.topicBySubjectIdentifier(definition, reference));
			/*
			 * add IN criterion
			 */
			definition.add(new InCriterion(column, alias, inDef));
		}
	}

	private static final String TYPEABLES = "typeables";
	private static final String COL_TYPE = "id_type";
	private static final String COL_ID = "id";
	private static final String TYPE_CONDITION = "{0}.id_type = {1}";

	/**
	 * Utility method generates a SQL query to get all typed constructs for the
	 * given topic type as SQL selection
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param typeSelection
	 *            the type selection
	 * @param initialIndex
	 *            the initial index
	 * @return the SQL definition
	 */
	public static final ISqlDefinition generateSqlDefinitionForTypeables(ITMQLRuntime runtime, IContext context, String typeSelection, final int initialIndex) {
		/*
		 * create SQL definition
		 */
		ISqlDefinition definition = new SqlDefinition();
		definition.setInternalAliasIndex(initialIndex);

		/*
		 * create from part
		 */
		IFromPart fromPart = new FromPart(TYPEABLES, definition.getAlias(), true);
		definition.addFromPart(fromPart);

		/*
		 * create condition
		 */
		definition.add(MessageFormat.format(TYPE_CONDITION, fromPart.getAlias(), typeSelection));
		/*
		 * add selection
		 */
		definition.addSelection(new Selection(COL_ID, fromPart.getAlias()));
		return definition;
	}

}
