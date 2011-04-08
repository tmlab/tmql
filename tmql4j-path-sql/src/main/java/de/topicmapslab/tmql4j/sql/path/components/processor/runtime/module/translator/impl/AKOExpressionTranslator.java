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
import de.topicmapslab.tmql4j.path.grammar.productions.AKOExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.SimpleContent;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.SqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.from.FromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.where.InCriterion;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.ISqlTranslator;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.TmqlSqlTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.TranslatorRegistry;
import de.topicmapslab.tmql4j.sql.path.utils.ISchema;
import de.topicmapslab.tmql4j.sql.path.utils.TranslatorUtils;

/**
 * @author Sven Krosse
 * 
 */
public class AKOExpressionTranslator extends TmqlSqlTranslatorImpl<AKOExpression> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISqlDefinition toSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		/*
		 * get translator
		 */
		ISqlTranslator<?> translator = TranslatorRegistry.getTranslator(SimpleContent.class);
		/*
		 * get supertype selection
		 */
		ISqlDefinition supertype = translator.toSql(runtime, context, expression.getExpressions().get(1), new SqlDefinition());
		/*
		 * get from content as UNION for all supertypes ( by association and
		 * relation )
		 */
		String union = TranslatorUtils.generateSupertypeSubtypeSet(runtime, context);
		/*
		 * generate new SQL definition
		 */
		ISqlDefinition ako = new SqlDefinition();
		IFromPart fromPart = new FromPart(union, definition.getAlias(), false);
		ako.addFromPart(fromPart);
		/*
		 * supertype must be member of all supertypes set
		 */
		ako.add(new InCriterion(ISchema.RelKindOf.ID_SUPERTYPE, fromPart.getAlias(), supertype));
		ISelection selection = new Selection(ISchema.RelKindOf.ID_SUBTYPE, fromPart.getAlias());
		selection.setCurrentTable(SqlTables.TOPIC);
		ako.addSelection(selection);
		return ako;
	}

}
