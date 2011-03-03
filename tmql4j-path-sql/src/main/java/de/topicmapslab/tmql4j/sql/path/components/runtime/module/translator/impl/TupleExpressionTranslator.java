/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl;

import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.AliasValueExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.TupleExpression;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.SqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.CaseSelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.NullSelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.ISqlTranslator;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TmqlSqlTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TranslatorRegistry;

/**
 * @author Sven Krosse
 * 
 */
public class TupleExpressionTranslator extends TmqlSqlTranslatorImpl<TupleExpression> {

	/**
	 * {@inheritDoc}
	 */
	public ISqlDefinition toSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		ISqlDefinition def = new SqlDefinition();		
		def.setInternalAliasIndex(definition.getInternalAliasIndex());
		def.addSelection(definition.getLastSelection());

		SqlDefinition result = (SqlDefinition) definition.clone();
		result.clearSelection();

		/*
		 * is empty tuple-sequence
		 */
		if (expression.getExpressions().isEmpty()) {
			result.addSelection(NullSelection.getNullSelection());
		}
		/*
		 * is projection or anything else
		 */
		else {
			Context newContext = new Context(context);
			int i = 0;
			ISqlTranslator<?> translator = TranslatorRegistry.getTranslator(AliasValueExpression.class);
			for (IExpression ex : expression.getExpressions()) {
				newContext.setCurrentIndexInTuple(i++);
				ISqlDefinition valueDefinition = translator.toSql(runtime, newContext, ex, def);
				CaseSelection sel = new CaseSelection(valueDefinition, result.getAlias());
				sel.setCurrentTable(valueDefinition.getLastSelection().getCurrentTable());
				result.addSelection(sel);
			}
		}
		return result;
	}
}
