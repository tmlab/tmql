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
import java.util.Collection;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.path.grammar.lexical.By;
import de.topicmapslab.tmql4j.path.grammar.lexical.Order;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.SqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.from.FromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.where.InCriterion;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.axis.TypesAxisTranslator;

/**
 * @author Sven Krosse
 * 
 */
public class TranslatorUtils {

	/**
	 * 
	 */
	private static final String INDEX = "index";

	/**
	 * Utility method to select a locator by its reference and returns the alias
	 * of the locators FROM part
	 * 
	 * @param definition
	 *            the definition, the selection should add to
	 * @param reference
	 *            the reference
	 * @return the alias of locators FROM part
	 */
	public static final String addLocatorSelection(final ISqlDefinition definition, final String reference) {
		/*
		 * add from parts
		 */
		IFromPart locators = new FromPart(ISchema.Locators.TABLE, definition.getAlias(), true);
		definition.addFromPart(locators);
		/*
		 * add condition
		 */
		String condition = ConditionalUtils.equal(reference, locators.getAlias(), ISchema.Locators.REFERENCE);
		definition.add(condition);
		/*
		 * return alias of locator id
		 */
		return locators.getAlias();
	}

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
		IFromPart locators = new FromPart(ISchema.Locators.TABLE, newDefinition.getAlias(), true);
		newDefinition.addFromPart(locators);
		IFromPart relation = new FromPart(ISchema.RelSubjectIdentifiers.TABLE, newDefinition.getAlias(), true);
		newDefinition.addFromPart(relation);
		/*
		 * add condition
		 */
		String condition = ConditionalUtils.equal(locators.getAlias(), ISchema.Locators.ID, relation.getAlias(), ISchema.RelSubjectIdentifiers.ID_LOCATOR);
		newDefinition.add(condition);
		condition = ConditionalUtils.equal(ISqlConstants.SINGLEQUOTE + reference + ISqlConstants.SINGLEQUOTE, locators.getAlias(), ISchema.Locators.REFERENCE);
		newDefinition.add(condition);
		/*
		 * add selection
		 */
		ISelection selection = new Selection(ISchema.RelSubjectIdentifiers.ID_TOPIC, relation.getAlias());
		selection.setCurrentTable(SqlTables.TOPIC);
		newDefinition.addSelection(selection);
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
		return generateSqlDefinitionForTypeables(runtime, context, definition, typeSelection);
	}

	/**
	 * Utility method generates a SQL query to get all typed constructs for the
	 * given topic type as SQL selection
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param definition
	 *            the definition the selection should add to
	 * @param typeSelection
	 *            the type selection
	 * @return the SQL definition
	 */
	public static final ISqlDefinition generateSqlDefinitionForTypeables(ITMQLRuntime runtime, IContext context, ISqlDefinition definition, String typeSelection) {
		/*
		 * create from part
		 */
		IFromPart fromPart = new FromPart(ISchema.Typeables.TABLE, definition.getAlias(), true);
		definition.addFromPart(fromPart);

		/*
		 * create condition
		 */
		String condition = ConditionalUtils.equal(typeSelection, fromPart.getAlias(), ISchema.Typeables.ID_TYPE);
		definition.add(condition);
		/*
		 * add selection
		 */
		definition.addSelection(new Selection(ISchema.Constructs.ID, fromPart.getAlias()));
		return definition;
	}

	/**
	 * Utility method generates a SQL query to get all typed constructs for the
	 * given topic reference as SQL selection
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @param reference
	 *            the reference
	 * @param initialIndex
	 *            the initial index
	 * @return the SQL definition
	 */
	public static final ISqlDefinition generateSqlDefinitionForTypeablesByReference(ITMQLRuntime runtime, IContext context, String reference, final int initialIndex) {
		/*
		 * create SQL definition
		 */
		ISqlDefinition definition = new SqlDefinition();
		definition.setInternalAliasIndex(initialIndex);
		/*
		 * create from part
		 */
		IFromPart fromPart = new FromPart(ISchema.Typeables.TABLE, definition.getAlias(), true);
		definition.addFromPart(fromPart);
		/*
		 * get type reference
		 */
		final String ref = runtime.getConstructResolver().toAbsoluteIRI(context, reference);
		/*
		 * create new SQL definition to selection typing topic
		 */
		ISqlDefinition inDef = TranslatorUtils.topicBySubjectIdentifier(definition, ref);
		/*
		 * create condition
		 */
		definition.add(new InCriterion(ISchema.Typeables.ID_TYPE, fromPart.getAlias(), inDef));
		/*
		 * add selection
		 */
		definition.addSelection(new Selection(ISchema.Constructs.ID, fromPart.getAlias()));
		return definition;
	}

	private static final String SUPERTYPE_SUBTYPE = "SELECT id_supertype, id_subtype FROM rel_kind_of UNION SELECT ( SELECT id_player FROM roles AS r, rel_subject_identifiers AS rl, locators AS l WHERE id_locator = l.id AND reference = ''http://psi.topicmaps.org/iso13250/model/supertype'' AND r.id_type = id_topic AND r.id_parent = a.id ) AS id_supertype, ( SELECT id_player FROM roles AS r, rel_subject_identifiers AS rl, locators AS l WHERE id_locator = l.id AND reference = ''http://psi.topicmaps.org/iso13250/model/subtype'' AND r.id_type = id_topic AND r.id_parent = a.id ) AS id_subtype FROM associations AS a, rel_subject_identifiers AS rl, locators AS l WHERE id_locator = l.id AND reference = ''http://psi.topicmaps.org/iso13250/model/supertype-subtype'' AND a.id_type = id_topic AND a.id_topicmap = {0} ";

	/**
	 * Generates the query to extract all supertype-subtype parts as association
	 * and relation
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @return the generated query
	 */
	public static final String generateSupertypeSubtypeSet(ITMQLRuntime runtime, IContext context) {
		return MessageFormat.format(SUPERTYPE_SUBTYPE, context.getQuery().getTopicMap().getId());
	}

	private static final String TYPE_INSTANCE = "SELECT id_type, id_instance FROM rel_instance_of UNION SELECT ( SELECT id_player FROM roles AS r, rel_subject_identifiers AS rl, locators AS l WHERE id_locator = l.id AND reference = ''http://psi.topicmaps.org/iso13250/model/type'' AND r.id_type = id_topic AND r.id_parent = a.id ) AS id_type, ( SELECT id_player FROM roles AS r, rel_subject_identifiers AS rl, locators AS l WHERE id_locator = l.id AND reference = ''http://psi.topicmaps.org/iso13250/model/instance'' AND r.id_type = id_topic AND r.id_parent = a.id ) AS id_instance FROM associations AS a, rel_subject_identifiers AS rl, locators AS l WHERE id_locator = l.id AND reference = ''http://psi.topicmaps.org/iso13250/model/type-instance'' AND a.id_type = id_topic AND a.id_topicmap = {0} ";

	/**
	 * Generates the query to extract all type-instance parts as association and
	 * relation
	 * 
	 * @param runtime
	 *            the runtime
	 * @param context
	 *            the context
	 * @return the generated query
	 */
	public static final String generateTypeInstanceSet(ITMQLRuntime runtime, IContext context) {
		return MessageFormat.format(TYPE_INSTANCE, context.getQuery().getTopicMap().getId());
	}

	/**
	 * Creates a new from clause containing an union over all given SQL
	 * definitions
	 * 
	 * @param definitions
	 *            the definitions
	 * @param fromAlias
	 *            the alias of the from clause
	 * @param resultAlias
	 *            the alias to set as result alias
	 * @param stableOrder
	 *            flag indicates if a second column should be selection
	 *            containing the index of the selection to keep order stable
	 * @return the from clause
	 */
	public static final IFromPart asUnion(Collection<ISqlDefinition> definitions, String fromAlias, String resultAlias, boolean stableOrder) {
		return asSetOperation(definitions, fromAlias, resultAlias, ISqlConstants.ISqlOperators.UNION, stableOrder);
	}

	/**
	 * Creates a new from clause containing an intersection over all given SQL
	 * definitions
	 * 
	 * @param definitions
	 *            the definitions
	 * @param fromAlias
	 *            the alias of the from clause
	 * @param resultAlias
	 *            the alias to set as result alias
	 * @param stableOrder
	 *            flag indicates if a second column should be selection
	 *            containing the index of the selection to keep order stable
	 * @return the from clause
	 */
	public static final IFromPart asIntersection(Collection<ISqlDefinition> definitions, String fromAlias, String resultAlias, boolean stableOrder) {
		return asSetOperation(definitions, fromAlias, resultAlias, ISqlConstants.ISqlOperators.INTERSECT, stableOrder);
	}

	/**
	 * Creates a new from clause containing a set operation over all given SQL
	 * definitions
	 * 
	 * @param definitions
	 *            the definitions
	 * @param fromAlias
	 *            the alias of the from clause
	 * @param resultAlias
	 *            the alias to set as result alias
	 * @param operator
	 *            the operator
	 * @param stableOrder
	 *            flag indicates if a second column should be selection
	 *            containing the index of the selection to keep order stable
	 * @return the from clause
	 */
	public static final IFromPart asSetOperation(Collection<ISqlDefinition> definitions, String fromAlias, String resultAlias, String operator, boolean stableOrder) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		int i = 0;
		for (ISqlDefinition definition : definitions) {
			if (!first) {
				builder.append(ISqlConstants.WHITESPACE);
				builder.append(operator);
				builder.append(ISqlConstants.WHITESPACE);
			}
			/*
			 * translate last selection alias
			 */
			ISelection selection = definition.getLastSelection();
			if (resultAlias != null) {
				selection.setAlias(resultAlias);
			}
			/*
			 * add index to definition to keep order stable
			 */
			if (stableOrder) {
				definition.addSelection(new Selection(Integer.toString(i++), INDEX, false));
			}
			builder.append(definition.toString());
			first = false;
		}
		if (stableOrder) {
			builder.append(ISqlConstants.WHITESPACE);
			builder.append(Order.TOKEN);
			builder.append(ISqlConstants.WHITESPACE);
			builder.append(By.TOKEN);
			builder.append(ISqlConstants.WHITESPACE);
			builder.append(INDEX);
		}
		return new FromPart(builder.toString(), fromAlias, false);
	}

	/**
	 * Creates a selection clause containing a function call with the given
	 * argument
	 * 
	 * @param function
	 *            the function name
	 * @param argument
	 *            the argument
	 * @param selectionAlias
	 *            the selection alias or <code>null</code>
	 * @return the selection
	 */
	public static final ISelection getFunctionCall(final String function, ISelection argument, String selectionAlias) {
		StringBuilder builder = new StringBuilder();
		builder.append(function);
		builder.append(BracketRoundOpen.TOKEN);
		builder.append(argument.getSelection());
		builder.append(BracketRoundClose.TOKEN);
		ISelection selection = new Selection(builder.toString(), selectionAlias, false);
		return selection;
	}

	/**
	 * Creates a selection clause containing a function call with the given
	 * argument <code>columnAlias.column</code>
	 * 
	 * @param function
	 *            the function name
	 * @param column
	 *            the column
	 * @param columnAlias
	 *            the column alias or <code>null</code>
	 * @param cast
	 *            an optional cast for arguments
	 * @param selectionAlias
	 *            the selection alias or <code>null</code>
	 * @return the selection
	 */
	public static final ISelection getFunctionCall(final String function, String column, String columnAlias, String cast, String selectionAlias) {
		ISelection selection = new Selection(column, columnAlias);
		selection.cast(cast);
		return getFunctionCall(function, selection, selectionAlias);
	}

}
