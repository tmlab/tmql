/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.aggregate;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.SqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.from.FromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.ISqlTranslator;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.FunctionTranslatorImpl;

/**
 * @author Sven Krosse
 * 
 */
public abstract class AggregateFunctionTranslatorImpl extends FunctionTranslatorImpl {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISelection getSelection(ISqlDefinition definition, List<ISqlDefinition> parameters, List<IFromPart> fromParts) {
		/*
		 * get the SQL definition of content to aggregate
		 */
		ISqlDefinition aggregate = parameters.get(1);
		/*
		 * generate selection for aggregation function ( MAX or MIN )
		 */
		StringBuilder builder = new StringBuilder();
		builder.append(getFunctionName());
		builder.append(BracketRoundOpen.TOKEN);
		builder.append(aggregate.getLastSelection().getAlias());
		builder.append(BracketRoundClose.TOKEN);
		/*
		 * create from part for the aggregation function call
		 */
		IFromPart fromPart = new FromPart(aggregate.toString(), definition.getAlias(), false);
		definition.addFromPart(fromPart);
		/*
		 * create selection using aggregation function
		 */
		ISelection selection = new Selection(builder.toString(), null);
		selection.setCurrentTable(SqlTables.INTEGER);
		return selection;
	}

	/**
	 * Returns the name of the aggregation function to call ( MAX or MIN )
	 * 
	 * @return the aggregation function
	 */
	protected abstract String getFunctionName();

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISqlDefinition parameterToSql(ITMQLRuntime runtime, IContext context, IExpression expression, ISqlTranslator<?> translator, ISqlDefinition definition,
			List<ISqlDefinition> leftHandParameters) throws TMQLRuntimeException {
		/*
		 * is context selection
		 */
		if (leftHandParameters.isEmpty()) {
			return super.parameterToSql(runtime, context, expression, translator, definition, leftHandParameters);
		}
		/*
		 * is aggregation argument selection
		 */
		ISqlDefinition contextSql = leftHandParameters.get(0);
		ISqlDefinition newDefinition = new SqlDefinition();
		ISelection contextSelection = contextSql.getLastSelection();
		newDefinition.addSelection(contextSelection);
		ISqlDefinition aggregate = super.parameterToSql(runtime, context, expression, translator, newDefinition, leftHandParameters);
		/*
		 * set context as from part and aggregation as selection
		 */
		ISqlDefinition parameter = contextSql.clone();
		parameter.clearSelection();
		parameter.addSelection(new Selection(aggregate.toString(), definition.getAlias(), false));
		return parameter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addFromParts(ISqlDefinition definition, List<IFromPart> fromParts) {
		// VOID
	}

}
