/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.functions.string;

import java.util.List;

import de.topicmapslab.tmql4j.path.grammar.lexical.Dot;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.functions.FunctionTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants;

/**
 * @author Sven Krosse
 * 
 */
public abstract class ComparisonStringFunctionTranslatorImpl extends FunctionTranslatorImpl {

	/**
	 * {@inheritDoc}
	 */
	protected ISelection getSelection(ISqlDefinition definition, List<ISqlDefinition> parameters, List<IFromPart> fromParts) {
		ISelection selection = new Selection(parameters.get(0).getLastSelection().getAlias(), fromParts.get(0).getAlias());
		selection.setCurrentTable(SqlTables.STRING);
		return selection;
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected String getCriterion(ISqlDefinition definition, List<ISqlDefinition> parameters, List<IFromPart> fromParts) {
		StringBuilder builder = new StringBuilder();
		builder.append(fromParts.get(0).getAlias());
		builder.append(Dot.TOKEN);
		builder.append(parameters.get(0).getLastSelection().getAlias());
		builder.append(ISqlConstants.WHITESPACE);
		builder.append(getOperator());
		builder.append(ISqlConstants.WHITESPACE);
		builder.append(fromParts.get(1).getAlias());
		builder.append(Dot.TOKEN);
		builder.append(parameters.get(1).getLastSelection().getAlias());
		return builder.toString();
	}

	protected abstract String getOperator();

}
