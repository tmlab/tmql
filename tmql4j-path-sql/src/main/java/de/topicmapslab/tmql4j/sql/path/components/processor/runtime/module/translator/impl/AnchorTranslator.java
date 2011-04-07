/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.components.interpreter.AnchorInterpreter;
import de.topicmapslab.tmql4j.path.grammar.productions.Anchor;
import de.topicmapslab.tmql4j.path.grammar.productions.SimpleContent;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.SqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.from.FromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.where.InCriterion;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.TmqlSqlTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.utils.ConditionalUtils;
import de.topicmapslab.tmql4j.sql.path.utils.ISchema;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants;
import de.topicmapslab.tmql4j.sql.path.utils.TranslatorUtils;
import de.topicmapslab.tmql4j.util.LiteralUtils;
import de.topicmapslab.tmql4j.util.TmdmSubjectIdentifier;

/**
 * @author Sven Krosse
 * 
 */
public class AnchorTranslator extends TmqlSqlTranslatorImpl<SimpleContent> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISqlDefinition toSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		switch (expression.getGrammarType()) {
			case Anchor.TYPE_TOPICREF: {
				final String token = expression.getTokens().get(0);
				ISqlDefinition newDefinition = definition.clone();
				newDefinition.clearSelection();
				/*
				 * create from part for topics table
				 */
				IFromPart part = new FromPart(ISchema.Topics.TABLE, newDefinition.getAlias(), true);
				newDefinition.addFromPart(part);
				SqlTables table;
				/*
				 * create condition
				 */
				String condition = ConditionalUtils.equal(context.getQuery().getTopicMap().getId(), part.getAlias(), ISchema.Constructs.ID_TOPICMAP);
				newDefinition.add(condition);
				if (TmdmSubjectIdentifier.isTmdmSubject(token)) {
					table = SqlTables.TMSUBJECT;
				} else {
					/*
					 * fetch topic by subject-identifier
					 */
					ISqlDefinition innerDefinition = TranslatorUtils.topicBySubjectIdentifier(newDefinition, runtime.getConstructResolver().toAbsoluteIRI(context, token));
					InCriterion in = new InCriterion(ISchema.Constructs.ID, part.getAlias(), innerDefinition);
					newDefinition.add(in);
					table = SqlTables.TOPIC;
					// /*
					// * add additional tables
					// */
					// IFromPart fromPartLocs = new
					// FromPart(ISchema.Locators.TABLE,
					// newDefinition.getAlias(), true);
					// newDefinition.addFromPart(fromPartLocs);
					// IFromPart fromPartRel = new
					// FromPart(ISchema.RelSubjectIdentifiers.TABLE,
					// newDefinition.getAlias(), true);
					// newDefinition.addFromPart(fromPartRel);
					// /*
					// * add additional condition
					// */
					//
					// newDefinition.add(MessageFormat.format(CONDITION_LOCATOR_REL,
					// fromPartLocs.getAlias(), fromPartRel.getAlias()));
					// newDefinition.add(MessageFormat.format(CONDITION_REL_TOPIC,
					// fromPartRel.getAlias(), part.getAlias()));
					// newDefinition.add(MessageFormat.format(CONDITION_REFERENCE,
					// fromPartLocs.getAlias(),
					// runtime.getConstructResolver().toAbsoluteIRI(context,
					// token)));
				}
				ISelection sel = new Selection(ISchema.Constructs.ID, part.getAlias());
				newDefinition.addSelection(sel);
				sel.setCurrentTable(table);
				return newDefinition;
			}
			case Anchor.TYPE_DOT: {
				ISqlDefinition def = new SqlDefinition();
				def.setInternalAliasIndex(definition.getInternalAliasIndex());
				def.addSelection(definition.getLastSelection());
				return def;
			}
			case Anchor.TYPE_LITERAL: {
				ISqlDefinition def = new SqlDefinition();
				String token = expression.getTokens().get(0);
				SqlTables table = SqlTables.ANY;
				try {
					if (LiteralUtils.isString(token)) {
						token = ISqlConstants.SINGLEQUOTE + LiteralUtils.asString(token) + ISqlConstants.SINGLEQUOTE;
						table = SqlTables.STRING;
					} else if (LiteralUtils.isInteger(token)) {
						table = SqlTables.INTEGER;
					} else if (LiteralUtils.isDecimal(token)) {
						table = SqlTables.DECIMAL;
					} else if (LiteralUtils.isDate(token) || LiteralUtils.isTime(token) || LiteralUtils.isDateTime(token)) {
						table = SqlTables.DATETIME;
					} else if (LiteralUtils.isBoolean(token)) {
						table = SqlTables.BOOLEAN;
					}
				} catch (Exception ex) {
					throw new TMQLRuntimeException("Cannot found element for given reference '" + token + "'!");
				}
				ISelection sel = new Selection(token, def.getAlias(), false);
				def.addSelection(sel);
				sel.setCurrentTable(table);
				return def;
			}
			case Anchor.TYPE_VARIABLE: {
				final String variable = expression.getTokens().get(0);
				if (AnchorInterpreter.VARIABLE_TOPIC_MAP.equalsIgnoreCase(variable)) {
					ISqlDefinition def = new SqlDefinition();
					ISelection sel = new Selection(context.getQuery().getTopicMap().getId(), null);
					def.addSelection(sel);
					sel.setCurrentTable(SqlTables.STRING);
					return def;
				}
			}
		}
		throw new TMQLRuntimeException("Unsupported expression type for SQL translator.");
	}

}
