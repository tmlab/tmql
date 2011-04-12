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
import de.topicmapslab.tmql4j.grammar.productions.PreparedExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.PredicateInvocation;
import de.topicmapslab.tmql4j.path.grammar.productions.PredicateInvocationRolePlayerExpression;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.SqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.from.FromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.CountSelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.where.InCriterion;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.ISqlTranslator;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.TmqlSqlTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.TranslatorRegistry;
import de.topicmapslab.tmql4j.sql.path.utils.ConditionalUtils;
import de.topicmapslab.tmql4j.sql.path.utils.ISchema;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants;
import de.topicmapslab.tmql4j.sql.path.utils.TranslatorUtils;

/**
 * @author Sven Krosse
 * 
 */
public class PredicateInvocationTranslator extends TmqlSqlTranslatorImpl<PredicateInvocation> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISqlDefinition toSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {

		ISqlDefinition associationType;
		ISqlDefinition def = new SqlDefinition();
		/*
		 * get association type contents
		 */
		if (expression.contains(PreparedExpression.class)) {
			associationType = TranslatorRegistry.getTranslator(PreparedExpression.class).toSql(runtime, context, expression.getExpressionFilteredByType(PreparedExpression.class).get(0), definition);
		} else {
			String token = expression.getTokens().get(0);
			associationType = TranslatorUtils.topicBySubjectIdentifier(definition, runtime.getConstructResolver().toAbsoluteIRI(context, token));
		}
		/*
		 * select all associations of the given type
		 */
		IFromPart associationsPart = new FromPart(ISchema.Associations.TABLE, definition.getAlias(), true);
		def.addFromPart(associationsPart);
		def.add(new InCriterion(ISchema.Typeables.ID_TYPE, associationsPart.getAlias(), associationType));
		/*
		 * select two-dimensional arrays of role-type and player combinations of all associations of the given type
		 */
		ISqlDefinition predicates = new SqlDefinition();
		IFromPart rolesPart = new FromPart(ISchema.Roles.TABLE, definition.getAlias(), true);
		predicates.addFromPart(rolesPart);
		predicates.add(ConditionalUtils.equal(rolesPart.getAlias(), ISchema.Constructs.ID_PARENT, associationsPart.getAlias(), ISchema.Constructs.ID));
		ISelection array = new Selection("ARRAY[" + rolesPart.getAlias() + "." + ISchema.Typeables.ID_TYPE + "," + rolesPart.getAlias() + "." + ISchema.Roles.ID_PLAYER + "]", definition.getAlias(),
				false);
		predicates.addSelection(array);

		/*
		 * iterate over all role-player constraints and add new conditional parts to where clause
		 */
		ISqlTranslator<?> translator = TranslatorRegistry.getTranslator(PredicateInvocationRolePlayerExpression.class);
		boolean strict = true;
		int cnt = 0;
		for (PredicateInvocationRolePlayerExpression rolePlayer : expression.getExpressionFilteredByType(PredicateInvocationRolePlayerExpression.class)) {
			if (rolePlayer.getGrammarType() == PredicateInvocationRolePlayerExpression.TYPE_ELLIPSIS) {
				strict = false;
			} else {
				/*
				 * ARRAY[id_type,id_player] must be known by the association
				 */
				ISqlDefinition rolePlayerDef = translator.toSql(runtime, context, rolePlayer, definition);
				ISelection sel = rolePlayerDef.getLastSelection();
				def.add(new InCriterion(sel.toString(), predicates));
				cnt++;
			}
		}

		/*
		 * add criterion to count roles and check to number of role-player-constraints
		 */
		if (strict) {
			ISqlDefinition count = new SqlDefinition();
			IFromPart countPart = new FromPart(ISchema.Roles.TABLE, definition.getAlias(), true);
			count.addFromPart(countPart);
			count.add(ConditionalUtils.equal(countPart.getAlias(), ISchema.Constructs.ID_PARENT, associationsPart.getAlias(), ISchema.Constructs.ID));
			count.addSelection(new CountSelection(new Selection(ISqlConstants.ANY, null), null));
			def.add(new InCriterion(Integer.toString(cnt), count));
		}

		ISelection selection = new Selection(ISchema.Constructs.ID, associationsPart.getAlias());
		selection.setCurrentTable(SqlTables.ASSOCIATION);
		def.addSelection(selection);
		return def;
	}

}
