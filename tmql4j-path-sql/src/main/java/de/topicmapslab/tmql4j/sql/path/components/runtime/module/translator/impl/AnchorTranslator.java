/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl;

import java.text.MessageFormat;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.Anchor;
import de.topicmapslab.tmql4j.path.grammar.productions.SimpleContent;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.FromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.SqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TmqlSqlTranslatorImpl;
import de.topicmapslab.tmql4j.util.LiteralUtils;
import de.topicmapslab.tmql4j.util.TmdmSubjectIdentifier;

/**
 * @author Sven Krosse
 * 
 */
public class AnchorTranslator extends TmqlSqlTranslatorImpl<SimpleContent> {

	private static final String SELECTION = "id";
	private static final String TOPICS = "topics";
	private static final String LOCATORS = "locators";
	private static final String REL_SUBJECT_IDENTIFIERS = "rel_subject_identifiers";
	private static final String TOPICMAP_CONDITION = "{0}.id_topicmap = {1}";
	private static final String CONDITION_LOCATOR_REL = "{0}.id = {1}.id_locator";
	private static final String CONDITION_REL_TOPIC = "{0}.id_topic = {1}.id";
	private static final String CONDITION_REFERENCE = "{0}.reference = ''{1}''";

	/**
	 * {@inheritDoc}
	 */
	public ISqlDefinition toSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		switch (expression.getGrammarType()) {
			case Anchor.TYPE_TOPICREF: {
				final String token = expression.getTokens().get(0);
				ISqlDefinition newDefinition = definition.clone();
				newDefinition.clearSelection();
				/*
				 * create from part for topics table
				 */
				IFromPart part = new FromPart(TOPICS, newDefinition.getAlias(), true);
				newDefinition.addFromPart(part);
				/*
				 * create condition
				 */
				newDefinition.add(MessageFormat.format(TOPICMAP_CONDITION, part.getAlias(), context.getQuery().getTopicMap().getId()));
				if (TmdmSubjectIdentifier.isTmdmSubject(token)) {
					// NOTHING TO DO ANYMORE
				} else {
					/*
					 * add additional tables
					 */
					IFromPart fromPartLocs = new FromPart(LOCATORS, newDefinition.getAlias(), true);
					newDefinition.addFromPart(fromPartLocs);
					IFromPart fromPartRel = new FromPart(REL_SUBJECT_IDENTIFIERS, newDefinition.getAlias(), true);
					newDefinition.addFromPart(fromPartRel);
					/*
					 * add additional condition
					 */
					newDefinition.add(MessageFormat.format(CONDITION_LOCATOR_REL, fromPartLocs.getAlias(), fromPartRel.getAlias()));
					newDefinition.add(MessageFormat.format(CONDITION_REL_TOPIC, fromPartRel.getAlias(), part.getAlias()));
					newDefinition.add(MessageFormat.format(CONDITION_REFERENCE, fromPartLocs.getAlias(), runtime.getConstructResolver().toAbsoluteIRI(context, token)));

				}
				newDefinition.addSelection(new Selection(SELECTION, part.getAlias()));
				newDefinition.setCurrentTable(SqlTables.TOPIC);
				return newDefinition;
			}
			case Anchor.TYPE_DOT: {
				return definition;
			}
			case Anchor.TYPE_LITERAL: {
				ISqlDefinition def = new SqlDefinition();
				def.addSelection(new Selection("'" + LiteralUtils.asString(expression.getTokens().get(0)) + "'", null));
				return def;
			}
		}
		throw new TMQLRuntimeException("Unsupported expression type for SQL translator.");
	}

}
