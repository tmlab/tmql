/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl;

import org.tmapi.core.Construct;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.prepared.IPreparedStatement;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.grammar.productions.PreparedExpression;
import de.topicmapslab.tmql4j.query.IQuery;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.SqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.TmqlSqlTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants;

/**
 * @author Sven Krosse
 * 
 */
public class PreparedExpressionTranslator extends TmqlSqlTranslatorImpl<PreparedExpression> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISqlDefinition toSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlDefinition definition) throws TMQLRuntimeException {
		IQuery q = context.getQuery();
		if (!(q instanceof IPreparedStatement)) {
			throw new TMQLRuntimeException("Query should be a prepared statement!");
		}
		/*
		 * get value and transfrom to SQL selection
		 */
		Object value = ((IPreparedStatement) q).get(expression);
		ISelection selection;
		if (value instanceof Construct) {
			selection = new Selection(((Construct) value).getId(), definition.getAlias(), false);
		} else if (value instanceof String) {
			selection = new Selection(ISqlConstants.SINGLEQUOTE + value + ISqlConstants.SINGLEQUOTE, definition.getAlias(), false);
		} else {
			selection = new Selection(value.toString(), definition.getAlias(), false);
		}

		ISqlDefinition def = new SqlDefinition();
		def.addSelection(selection);
		return def;
	}

}
