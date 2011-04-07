/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.string;

import java.text.MessageFormat;
import java.util.List;

import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.functions.FunctionTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants;

/**
 * @author Sven Krosse
 * 
 */
public abstract class ComparisonStringFunctionTranslatorImpl extends FunctionTranslatorImpl {

	private static final String CRITERION = " CAST ( {0}.{1} AS varchar ) {2}  CAST ( {3}.{4} AS varchar ) ";

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISelection getSelection(ISqlDefinition definition, List<ISqlDefinition> parameters, List<IFromPart> fromParts) {
		ISelection selection = new Selection(parameters.get(0).getLastSelection().getAlias(), fromParts.get(0).getAlias());
		selection.cast(ISqlConstants.ISqlTypes.VARCHAR);
		selection.setCurrentTable(SqlTables.STRING);
		return selection;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getCriterion(ISqlDefinition definition, List<ISqlDefinition> parameters, List<IFromPart> fromParts) {
		return MessageFormat.format(CRITERION, fromParts.get(0).getAlias(), parameters.get(0).getLastSelection().getAlias(), getOperator(), fromParts.get(1).getAlias(), parameters.get(1)
				.getLastSelection().getAlias());

		// StringBuilder builder = new StringBuilder();
		// builder.append(ISqlConstants.ISqlKeywords.CAST);
		// builder.append(BracketRoundOpen.TOKEN);
		// builder.append(ISqlConstants.WHITESPACE);
		// builder.append(fromParts.get(0).getAlias());
		// builder.append(Dot.TOKEN);
		// builder.append(parameters.get(0).getLastSelection().getAlias());
		//
		// builder.append(BracketRoundOpen.TOKEN);
		// builder.append(ISqlConstants.WHITESPACE);
		// builder.append(getOperator());
		// builder.append(ISqlConstants.WHITESPACE);
		// builder.append(fromParts.get(1).getAlias());
		// builder.append(Dot.TOKEN);
		// builder.append(parameters.get(1).getLastSelection().getAlias());
		// return builder.toString();
	}

	protected abstract String getOperator();

}
