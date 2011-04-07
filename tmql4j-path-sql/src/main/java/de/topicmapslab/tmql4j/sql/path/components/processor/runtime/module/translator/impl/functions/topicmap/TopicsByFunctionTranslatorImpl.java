/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.topicmap;

import java.util.List;

import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketSquareClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketSquareOpen;
import de.topicmapslab.tmql4j.path.grammar.lexical.Comma;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.SqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.from.FromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.where.InCriterion;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.FunctionTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.utils.ConditionalUtils;
import de.topicmapslab.tmql4j.sql.path.utils.ISchema;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants;

/**
 * @author Sven Krosse
 * 
 */
public abstract class TopicsByFunctionTranslatorImpl extends FunctionTranslatorImpl {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISelection getSelection(ISqlDefinition definition, List<ISqlDefinition> parameters, List<IFromPart> fromParts) {
		/*
		 * creates the array of string references
		 */
		StringBuilder builder = new StringBuilder();
		builder.append(ISqlConstants.ISqlFunctions.UNNEST);
		builder.append(BracketRoundOpen.TOKEN);
		builder.append(ISqlConstants.ISqlKeywords.ARRAY);
		builder.append(BracketSquareOpen.TOKEN);
		boolean first = true;
		for (ISqlDefinition parameter : parameters) {
			if (!first) {
				builder.append(Comma.TOKEN);
			}
			builder.append(parameter.getLastSelection().getColumn());
			first = false;
		}
		builder.append(BracketSquareClose.TOKEN);
		builder.append(BracketRoundClose.TOKEN);
		/*
		 * create selection for all string references
		 */
		ISqlDefinition innerDef = new SqlDefinition();
		innerDef.addSelection(new Selection(builder.toString(), null));

		/*
		 * create from part for relational column and locators column
		 */
		IFromPart relation = new FromPart(getRelationName(), definition.getAlias(), true);
		definition.addFromPart(relation);
		IFromPart table = new FromPart(ISchema.Locators.TABLE, definition.getAlias(), true);
		definition.addFromPart(table);

		/*
		 * add criterion
		 */
		InCriterion criterion = new InCriterion(ISchema.Locators.REFERENCE, table.getAlias(), innerDef);
		definition.add(criterion);
		String condition = ConditionalUtils.equal(relation.getAlias(), ISchema.RelItemIdentifiers.ID_LOCATOR, table.getAlias(), ISchema.Locators.ID);
		definition.add(condition);

		/*
		 * create selection of relational column
		 */
		ISelection selection = new Selection(getRelationColumn(), relation.getAlias());
		selection.setCurrentTable(SqlTables.TOPIC);
		return selection;
	}

	/**
	 * Returns the relation table name
	 * 
	 * @return the table name
	 */
	protected abstract String getRelationName();

	/**
	 * Returns the relation column name
	 * 
	 * @return the relational column
	 */
	protected abstract String getRelationColumn();

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addFromParts(ISqlDefinition definition, List<IFromPart> fromParts) {
		// VOID
	}

}
