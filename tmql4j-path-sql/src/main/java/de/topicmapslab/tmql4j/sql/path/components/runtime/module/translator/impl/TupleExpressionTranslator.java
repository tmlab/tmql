/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.AliasValueExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.TupleExpression;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.CaseSelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.SqlDefinition;
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
		def.setCurrentTable(definition.getCurrentTable());
		def.setInternalAliasIndex(definition.getInternalAliasIndex());
		def.addSelection(definition.getLastSelection());	
		
		SqlDefinition result = (SqlDefinition)definition.clone();
		result.clearSelection();
		
		ISqlTranslator<?> translator = TranslatorRegistry.getTranslator(AliasValueExpression.class);
		for (IExpression ex : expression.getExpressions()) {
			ISqlDefinition valueDefinition = translator.toSql(runtime, context, ex, def);
			result.addSelection(new CaseSelection(valueDefinition, result.getAlias()));
		}
		return result;
				
//		SqlDefinition definition_ = (SqlDefinition)definition.clone();
//		definition_.clearSelection();
//		ISqlTranslator<?> translator = TranslatorRegistry.getTranslator(AliasValueExpression.class);
//		for (IExpression ex : expression.getExpressions()) {
//			ISqlDefinition valueDefinition = translator.toSql(runtime, context, ex, def);
//			definition_.mergeIn((SqlDefinition) valueDefinition);
//			def.setInternalAliasIndex(definition_.getInternalAliasIndex());
//		}
//		return definition_;
		
		
	}
}
