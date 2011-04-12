/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl;

import java.util.ArrayList;
import java.util.List;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.PredicateInvocationRolePlayerExpression;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.SqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.ISqlTranslator;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.TmqlSqlTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.TranslatorRegistry;

/**
 * @author Sven Krosse
 * 
 */
public class PredicateInvocationRolePlayerExpressionTranslator extends TmqlSqlTranslatorImpl<PredicateInvocationRolePlayerExpression> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISqlDefinition toSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {

		ISqlDefinition def = new SqlDefinition();

		List<ISqlDefinition> parts = new ArrayList<ISqlDefinition>();
		for (IExpression ex : expression.getExpressions()) {
			ISqlTranslator<?> translator = TranslatorRegistry.getTranslator(ex.getClass());
			if (translator == null) {
				throw new TMQLRuntimeException("Missing translator implementation of expression type '" + ex.getClass().getSimpleName() + "'");
			}
			ISqlDefinition part = translator.toSql(runtime, context, ex, definition);
			parts.add(part);
		}

		ISelection selection = new Selection("ARRAY[(" + parts.get(0) + "),(" + parts.get(1) + ")]", null, true);
		def.addSelection(selection);
		return def;
	}

}
